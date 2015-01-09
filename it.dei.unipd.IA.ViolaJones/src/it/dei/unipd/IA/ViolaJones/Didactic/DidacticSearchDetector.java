package it.dei.unipd.IA.ViolaJones.Didactic;

/*
 * Import delle classi
 */
import it.dei.unipd.IA.ViolaJones.Detector.Feature;
import it.dei.unipd.IA.ViolaJones.Detector.MyRectangle;
import it.dei.unipd.IA.ViolaJones.Detector.Stage;
import it.dei.unipd.IA.ViolaJones.Detector.Tree;
import it.dei.unipd.IA.ViolaJones.ImageUtil.GrayImage;
import it.dei.unipd.IA.ViolaJones.ImageUtil.ImageToMatrix;
import it.dei.unipd.IA.ViolaJones.ImageUtil.IntegralImage;
import it.dei.unipd.IA.ViolaJones.ImageUtil.NormalizeImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Import della libreria di supporto usata per leggere dal file .xml
 */
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * Questa classe costruisce il classificatore ed esegue la ricerca dei volti
 */
public class DidacticSearchDetector extends JFrame {

    private Point searchRectangleInitialSize;
    private ArrayList<Stage> stages;
    private ArrayList<Rectangle> allPossibleRectangles;
    private ArrayList<Integer> sizeChangePosition;
    private ArrayList<Rectangle> rectanglesList;
    private ArrayList<Rectangle> rectanglesUnitedList;
    private ArrayList<Stage> stagesList;
    private BufferedImage image;
    private Rectangle featurePart1;
    private Rectangle featurePart2;
    private Rectangle featurePart3;
    private Rectangle searchRectangle;

    /*
     * Il costruttore principale prende in input una stringa con il percorso del file
     * xml contenente i dati che andranno a costrutire il classificatore.
     */
    public DidacticSearchDetector(File img, String percorso) {
        Document haarcascade_xml = null;
        SAXBuilder sxb = new SAXBuilder();
        try {
            haarcascade_xml = sxb.build(new File(percorso));
        } catch (IOException e) {
            System.out.println("C'è qualche problema con la lettura del file.");
        } catch (JDOMException ex) {
            System.out.println("La libreria JDOM2 ha smesso di funzionare.");
        }
        stages = new ArrayList<Stage>();
        allPossibleRectangles = new ArrayList<Rectangle>();
        sizeChangePosition = new ArrayList<Integer>();
        rectanglesList = new ArrayList<Rectangle>();
        rectanglesUnitedList = new ArrayList<Rectangle>();
        image = null;
        try {
            image = ImageIO.read(img);
            /*BufferedImage originalImage = ImageIO.read(img);
             int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
             BufferedImage resizeImageJpg = resizeImage(originalImage, BufferedImage.TYPE_INT_RGB, 800, 600);
             ImageIO.write(resizeImageJpg, "jpg", img);
             image = ImageIO.read(img);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        DrawPane d = new DrawPane();
        ImageToMatrix mtxImg = new ImageToMatrix(image);
        GrayImage grImg = new GrayImage(mtxImg.getMatrix());
        NormalizeImage nrm = new NormalizeImage(grImg.getGrayMatrixImage());
        nrm.ApplyFilter(3);
        try {
            ImageIO.write(nrm.getFilteredImage(), "JPG", new File("normalized.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(DidacticSearchDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
        IntegralImage intImg = new IntegralImage(nrm.getNormalizeMatrixIMage());
        intImg.getMatrixIntegralImage();
        buildClassifier(haarcascade_xml);

        setContentPane(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(image.getWidth(), image.getHeight());
        setResizable(false);
        setVisible(true);
        findFaces(intImg.getMatrixIntegralImageGray(),
                intImg.getSquaredMatrixIntegralImageGray(), 1.435f, 1.2f, .10f, 2);
    }

    /*
     * Costruttore secondario, invocato dal primo, accetta come parametro un jdom2.Document
     * costruito dal costruttore principale.
     */
    private void buildClassifier(Document document) {
        /*
         * Il rivelatore è costituito da fasi, ciascuna delle quali dice se la zona in esame rappresenta l'oggetto
         * con probabilità un po ' maggiore di 0,5. Se una zona passed tutte le fasi,
         * è considerato come rappresentante dell'oggetto
         */
        Element root = (Element) document.getRootElement().getChildren().get(0);
        Scanner scanner = new Scanner(root.getChild("size").getText());
        searchRectangleInitialSize = new Point(scanner.nextInt(), scanner.nextInt());
        Iterator iterator = root.getChild("stages").getChildren("_").iterator();
        while (iterator.hasNext()) {
            Element stage = (Element) iterator.next();
            float thres = Float.parseFloat(stage.getChild("stage_threshold").getText());
            Iterator iteratore2 = stage.getChild("trees").getChildren("_").iterator();
            Stage st = new Stage(thres);
            while (iteratore2.hasNext()) {
                Element tree = ((Element) iteratore2.next());
                Tree t = new Tree();
                Iterator it4 = tree.getChildren("_").iterator();
                while (it4.hasNext()) {
                    Element feature = (Element) it4.next();
                    float thres2 = Float.parseFloat(feature.getChild("threshold").getText());
                    int nodoSX = -1;
                    float valoreSX = 0;
                    boolean valoreSXCÈ = false;
                    int nodoDX = -1;
                    float valoreDX = 0;
                    boolean valoreDXCÈ = false;
                    Element e;
                    if ((e = feature.getChild("left_val")) != null) {
                        valoreSX = Float.parseFloat(e.getText());
                        valoreSXCÈ = true;
                    } else {
                        nodoSX = Integer.parseInt(feature.getChild("left_node").getText());
                        valoreSXCÈ = false;
                    }
                    if ((e = feature.getChild("right_val")) != null) {
                        valoreDX = Float.parseFloat(e.getText());
                        valoreDXCÈ = true;
                    } else {
                        nodoDX = Integer.parseInt(feature.getChild("right_node").getText());
                        valoreDXCÈ = false;
                    }
                    Feature f = new Feature(thres2, valoreSX, valoreDX, nodoSX, nodoDX, valoreSXCÈ,
                            valoreDXCÈ, searchRectangleInitialSize);
                    Iterator it3 = feature.getChild("feature").getChild("rects").getChildren("_").iterator();
                    while (it3.hasNext()) {
                        String s = ((Element) it3.next()).getText().trim();
                        MyRectangle r = MyRectangle.convert(s);
                        f.addRectangle(r);
                    }
                    t.addFeature(f);
                }
                st.addTree(t);
            }
            stages.add(st);
        }
    }

    /**
     * Riceve in input l'immagine integrale, l'immagine integrale quadratica, un
     * valore detto baseScale che definisce la grandezza della finestra di
     * ricerca, un valore scala_inc che è l'incremento dato ad ogni iterazione
     * del ciclo fino al valore massimo maxScale calcolato considerando il
     * valore minimo fra larghezza e altezza dell'immagine divise
     * rispettivamente per il valore x e y di searchRectangleInitialSize (che
     * altro non è che la descrizione di una finestra quadrata 24x24).
     *
     * @param grayIntegralImage
     * @param squaredGrayIntegralImage
     * @param baseScale
     * @param scala_inc
     * @param incrementa
     * @param minimalNumberOfRectanglesToBeConsideredNeighbors
     * @return rectanglesUnitedList
     */
    public ArrayList<Rectangle> findFaces(int[][] grayIntegralImage, int[][] squaredGrayIntegralImage,
            float baseScale, float scala_inc, float incrementa, int minimalNumberOfRectanglesToBeConsideredNeighbors) {
        int width = grayIntegralImage.length;
        int height = grayIntegralImage[0].length;
        /*
         * Calcolo quanto grande può essere al massimo il quadrato che effettua 
         * la ricerca, se è più grande dell'immagine stessa non ha senso continuare
         */
        float maxScale = (Math.min((float) (width) / searchRectangleInitialSize.x,
                (float) (height) / searchRectangleInitialSize.y));
        /*
         * Questa parte del codice sfrutta le informazioni calcolate in precedenza
         * per costruire un vero e proprio detector, costituito da un rettangolo, 
         * che scorre tutta l'imagine, aumentando le sue dimensioni ad ogni iterazione.
         */
        for (float scale = baseScale; scale < maxScale; scale *= scala_inc) {
            featurePart1 = null;
            featurePart2 = null;
            featurePart3 = null;
            int steps = (int) (scale * searchRectangleInitialSize.x * incrementa);
            int squareSize = (int) (scale * this.searchRectangleInitialSize.x);
            sizeChangePosition.add(allPossibleRectangles.size());
            /*
             * Scorre tutta l'immagine e per ogni rettangolo esaminato applica l'algoritmo 
             * di ricerca dei volti.
             */
            for (int w = 0; w < width - squareSize; w += steps) {
                for (int h = 0; h < height - squareSize; h += steps) {
                    boolean passed = true;
                    searchRectangle = new Rectangle(w, h, squareSize, squareSize);
                    allPossibleRectangles.add(searchRectangle);
                    featurePart1 = null;
                    featurePart2 = null;
                    featurePart3 = null;
                    /*
                     * Interroga l'immagine corrispondente ad ogni posizione del rettangolo di ricerca,
                     * se essa passa tutti gli stadi (costruiti nel metodo buildClassifier
                     * a partire dal file xml contente la cascata di classificatori) allora in quel caso 
                     * viene aggiunto il corrispondente rettangolo alla lista dei rettangoli contenti 
                     * un presunto volto.
                     */
                    for (int s = 0; s < stages.size(); s++) {
                        featurePart1 = null;
                        featurePart2 = null;
                        featurePart3 = null;
                        Stage stage = getStages().get(s);
                        ArrayList<Tree> treesList = stage.getTreesList();
                        for (int t = 0; t < treesList.size(); t++) {
                            featurePart1 = null;
                            featurePart2 = null;
                            featurePart3 = null;
                            ArrayList<Feature> featureList = treesList.get(t).getFeaturesList();
                            for (int f = 0; f < featureList.size(); f++) {
                                featurePart1 = null;
                                featurePart2 = null;
                                featurePart3 = null;
                                Feature feature = featureList.get(f);
                                if (feature.getScaledFeature()[0] != null) {
                                    featurePart1 = feature.getScaledFeature()[0].convert();
                                } else {
                                    featurePart1 = null;
                                }
                                if (feature.getScaledFeature()[1] != null) {
                                    featurePart2 = feature.getScaledFeature()[1].convert();
                                } else {
                                    featurePart2 = null;
                                }
                                if (feature.getScaledFeature()[2] != null) {
                                    featurePart3 = feature.getScaledFeature()[2].convert();
                                } else {
                                    featurePart3 = null;
                                }
                                getContentPane().repaint();
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        if (!stage.pass(grayIntegralImage, squaredGrayIntegralImage, w, h, scale)) {
                            passed = false;
                            break;
                        }

                    }
                    if (passed) {
                        rectanglesList.add(new Rectangle(w, h, squareSize, squareSize));
                    }
                }
            }
        }
        System.out.println("Numero di Rettangoli: " + rectanglesList.size());
        /*
         * Passa la lista appena ottenuta al metodo unify
         */
        return unify(rectanglesList, minimalNumberOfRectanglesToBeConsideredNeighbors);
    }

    public ArrayList<Rectangle> unify(List<Rectangle> rectangleList, int minimalNumberOfRectangleToBeConsideredNeighbors) {
        int[] rectangls = new int[rectangleList.size()];
        int numberOfGroups = 0;
        for (int i = 0; i < rectangleList.size(); i++) {
            boolean found = false;
            for (int j = 0; j < i; j++) {
                if (equals(rectangleList.get(j), rectangleList.get(i))) {
                    found = true;
                    rectangls[i] = rectangls[j];
                }
            }
            if (!found) {
                rectangls[i] = numberOfGroups;
                numberOfGroups++;
            }
        }
        int[] neighborsList = new int[numberOfGroups];
        Rectangle[] rect = new Rectangle[numberOfGroups];
        for (int i = 0; i < numberOfGroups; i++) {
            neighborsList[i] = 0;
            rect[i] = new Rectangle(0, 0, 0, 0);
        }
        for (int i = 0; i < rectangleList.size(); i++) {
            neighborsList[rectangls[i]]++;
            rect[rectangls[i]].x += rectangleList.get(i).x;
            rect[rectangls[i]].y += rectangleList.get(i).y;
            rect[rectangls[i]].height += rectangleList.get(i).height;
            rect[rectangls[i]].width += rectangleList.get(i).width;
        }
        System.out.println("Numero di raggruppamenti possibili " + numberOfGroups);
        for (int i = 0; i < numberOfGroups; i++) {
            int numberOfRectanglesOfIthGroup = neighborsList[i];
            if (numberOfRectanglesOfIthGroup >= minimalNumberOfRectangleToBeConsideredNeighbors) {
                Rectangle rectangle = new Rectangle(0, 0, 0, 0);
                rectangle.x = (rect[i].x * 2 + numberOfRectanglesOfIthGroup) / (2 * numberOfRectanglesOfIthGroup);
                rectangle.y = (rect[i].y * 2 + numberOfRectanglesOfIthGroup) / (2 * numberOfRectanglesOfIthGroup);
                rectangle.width = (rect[i].width * 2 + numberOfRectanglesOfIthGroup) / (2 * numberOfRectanglesOfIthGroup);
                rectangle.height = (rect[i].height * 2 + numberOfRectanglesOfIthGroup) / (2 * numberOfRectanglesOfIthGroup);
                rectanglesUnitedList.add(rectangle);
            }
        }
        System.out.println("Numero di rettangoli dopo l'unione: " + rectanglesUnitedList.size());
        return rectanglesUnitedList;
    }

    public boolean equals(Rectangle r1, Rectangle r2) {
        int distance = (int) (r1.width * 0.2);
        if (r2.x <= r1.x + distance
                && r2.x >= r1.x - distance
                && r2.y <= r1.y + distance
                && r2.y >= r1.y - distance
                && r2.width <= (int) (r1.width * 1.2)
                && r1.width <= (int) (r2.width * 1.2)) {
            return true;
        }
        if (r1.x >= r2.x
                && r1.x + r1.width <= r2.x + r2.width
                && r1.y >= r2.y
                && r1.y + r1.height <= r2.y + r2.height) {
            return true;
        }
        return false;
    }

    /**
     * Ritorna la lista di tutti le posizioni del rettangolo usato per la
     * ricerca
     *
     * @return allPossibleRectangles
     *
     */
    public ArrayList<Rectangle> getAllPossibleRect() {
        return allPossibleRectangles;
    }

    /**
     * Ritorna le posizioni in cui il rettangolo di ricerca ha cambiato la sua
     * dimensione
     *
     * @return sizeChangePosition
     */
    public ArrayList<Integer> getsizeChangePosition() {
        return sizeChangePosition;
    }

    /**
     * Ritorna la lista dei rettangoli in cui c'è un volto, un volto può essere
     * identificato più volte
     *
     * @return rectanglesList
     */
    public ArrayList<Rectangle> getRectangleList() {
        return rectanglesList;
    }

    /**
     * Ritorna la lista dei rettangoli in cui c'è un volto senza doppioni
     *
     * @return rectanglesUnitedList
     */
    public ArrayList<Rectangle> getRectangleUnitedList() {
        return rectanglesUnitedList;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    class DrawPane extends JPanel {

        int img_width, img_height;

        public DrawPane() {
            super();
            img_width = image.getWidth(null);
            img_height = image.getHeight(null);

        }

        @Override
        public void paintComponent(Graphics g) {
            g.clearRect(0, 0, getWidth(), getHeight());
            Graphics2D g1 = (Graphics2D) g;
            g1.setColor(Color.red);
            g1.setStroke(new BasicStroke(2f));
            if (image == null) {
                return;
            }
            Dimension dimension = getSize();
            g1.clearRect(0, 0, dimension.width, dimension.height);
            double scale_x = dimension.width * 1.f / img_width;
            double scale_y = dimension.height * 1.f / img_height;
            double scale = Math.min(scale_x, scale_y);
            int imageX = (dimension.width - (int) (img_width * scale)) / 2;
            int imageY = (dimension.height - (int) (img_height * scale)) / 2;
            g1.drawImage(image, imageX, imageY, (int) (img_width * scale), (int) (img_height * scale), null);
            int w = (int) (searchRectangle.width*scale);
            int h = (int) (searchRectangle.height*scale);
            int x = (int) (searchRectangle.x*scale) + imageX;
            int y = (int) (searchRectangle.y*scale) + imageY;
            g1.drawRect(x, y, w, h);
            for (int i = 0; i < rectanglesList.size(); i++) {
                int wf = (int) (rectanglesList.get(i).width * scale);
                int hf = (int) (rectanglesList.get(i).height * scale);
                int xf = (int) (rectanglesList.get(i).x * scale) + imageX;
                int yf = (int) (rectanglesList.get(i).y * scale) + imageY;
                g1.setColor(Color.blue);
                g1.drawRect(xf, yf, wf, hf);
            }
            for (int i = 0; i < rectanglesUnitedList.size(); i++) {
                int wf = (int) (rectanglesUnitedList.get(i).width * scale);
                int hf = (int) (rectanglesUnitedList.get(i).height * scale);
                int xf = (int) (rectanglesUnitedList.get(i).x * scale) + imageX;
                int yf = (int) (rectanglesUnitedList.get(i).y * scale) + imageY;
                g1.setColor(Color.orange);
                g1.drawRect(xf, yf, wf, hf);
            }
            if (featurePart1 != null) {
                int w1 = (int) (featurePart1.width);
                int h1 = (int) (featurePart1.height);
                int x1 = (int) (featurePart1.x) + imageX;
                int y1 = (int) (featurePart1.y) + imageY;
                g1.setColor(Color.white);
                g1.fillRect(x1, y1, w1, h1);
            }
            if (featurePart2 != null) {
                int w2 = (int) (featurePart2.width);
                int h2 = (int) (featurePart2.height);
                int x2 = (int) (featurePart2.x) + imageX;
                int y2 = (int) (featurePart2.y) + imageY;
                g1.setColor(Color.black);
                g1.fillRect(x2, y2, w2, h2);
            }
            if (featurePart3 != null) {
                int w3 = (int) (featurePart3.width);
                int h3 = (int) (featurePart3.height);
                int x3 = (int) (featurePart3.x) + imageX;
                int y3 = (int) (featurePart3.y) + imageY;
                g1.setColor(Color.black);
                g1.fillRect(x3, y3, w3, h3);
            }

        }

    }
    
    /*public static void main(String[] args) throws IOException {
        DidacticSearchDetector dd = new DidacticSearchDetector(new File("img.jpg"), "haarcascade_frontalface_alt2.xml");
    }*/
}
