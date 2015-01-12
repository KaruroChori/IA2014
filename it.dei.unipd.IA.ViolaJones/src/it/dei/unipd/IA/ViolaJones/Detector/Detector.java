package it.dei.unipd.IA.ViolaJones.Detector;

/*
 * Import delle classi
 */
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

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
public class Detector {

    private Point searchRectangleInitialSize;
    private ArrayList<Stage> stages;
    private ArrayList<Rectangle> allPossibleRectangles;
    private ArrayList<Integer> sizeChangePosition;
    private ArrayList<Rectangle> rectanglesList;
    private ArrayList<Rectangle> rectanglesUnitedList;

    /*
     * Il costruttore principale prende in input una stringa con il percorso del file
     * xml contenente i dati che andranno a costrutire il classificatore.
     */
    public Detector(String percorso) {
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
        buildClassifier(haarcascade_xml);
    }

    /**
     * Dato un documento .xml costruisce il corrispondente classifier.
     *
     * @param document
     */
    private void buildClassifier(Document document) {
        Element elementRoot = (Element) document.getRootElement().getChildren().get(0);
        Scanner scanner = new Scanner(elementRoot.getChild("size").getText());
        searchRectangleInitialSize = new Point(scanner.nextInt(), scanner.nextInt());
        Iterator iterator1 = elementRoot.getChild("stages").getChildren("_").iterator();
        while (iterator1.hasNext()) {
            Element elementStage = (Element) iterator1.next();
            float stage_threshold = Float.parseFloat(elementStage.getChild("stage_threshold").getText());
            Iterator iterator2 = elementStage.getChild("trees").getChildren("_").iterator();
            Stage stage = new Stage(stage_threshold);
            while (iterator2.hasNext()) {
                Element elementTree = ((Element) iterator2.next());
                Tree tree = new Tree();
                Iterator iterator3 = elementTree.getChildren("_").iterator();
                while (iterator3.hasNext()) {
                    Element elementFeature = (Element) iterator3.next();
                    float threshold = Float.parseFloat(elementFeature.getChild("threshold").getText());
                    int leftNode = -1;
                    float leftValue = 0;
                    boolean leftValuePresent = false;
                    int rightNode = -1;
                    float rightValue = 0;
                    boolean rightValuePresent = false;
                    Element elementNode;
                    if ((elementNode = elementFeature.getChild("left_val")) != null) {
                        leftValue = Float.parseFloat(elementNode.getText());
                        leftValuePresent = true;
                    } else {
                        leftNode = Integer.parseInt(elementFeature.getChild("left_node").getText());
                        leftValuePresent = false;
                    }
                    if ((elementNode = elementFeature.getChild("right_val")) != null) {
                        rightValue = Float.parseFloat(elementNode.getText());
                        rightValuePresent = true;
                    } else {
                        rightNode = Integer.parseInt(elementFeature.getChild("right_node").getText());
                        rightValuePresent = false;
                    }
                    Feature feature = new Feature(threshold, leftValue, rightValue, leftNode, rightNode, leftValuePresent,
                            rightValuePresent, searchRectangleInitialSize);
                    Iterator iterator4 = elementFeature.getChild("feature").getChild("rects").getChildren("_").iterator();
                    while (iterator4.hasNext()) {
                        String rectangleString = ((Element) iterator4.next()).getText().trim();
                        MyRectangle r = MyRectangle.convert(rectangleString);
                        feature.addRectangle(r);
                    }
                    tree.addFeature(feature);
                }
                stage.addTree(tree);
            }
            stages.add(stage);
        }
    }

    /**
     * Riceve in input l'immagine integrale, l'immagine integrale quadratica, un
     * valore detto baseScale che definisce la grandezza della finestra di
     * ricerca, un valore scala_inc che è l'incremento dato ad ogni iterazione
     * del ciclo fino al valore massimo maxScale calcolato considerando il
     * valore minimo fra larghezza elementNode altezza dell'immagine divise
     * rispettivamente per il valore x elementNode y di
     * searchRectangleInitialSize (che altro non è che la descrizione di una
     * finestra quadrata 24x24).
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
         * per costruire un vero elementNode proprio detector, costituito da un rettangolo, 
         * che scorre tutta l'imagine, aumentando le sue dimensioni ad ogni iterazione.
         */
        for (float scale = baseScale; scale < maxScale; scale *= scala_inc) {
            int steps = (int) (scale * searchRectangleInitialSize.x * incrementa);
            int squareSize = (int) (scale * this.searchRectangleInitialSize.x);
            sizeChangePosition.add(allPossibleRectangles.size());
            /*
             * Scorre tutta l'immagine elementNode per ogni rettangolo esaminato applica l'algoritmo 
             * di ricerca dei volti.
             */
            for (int w = 0; w < width - squareSize; w += steps) {
                for (int h = 0; h < height - squareSize; h += steps) {
                    boolean passed = true;
                    allPossibleRectangles.add(new Rectangle(w, h, squareSize, squareSize));
                    /*
                     * Interroga l'immagine corrispondente ad ogni posizione del rettangolo di ricerca,
                     * se essa passa tutti gli stadi (costruiti nel metodo buildClassifier
                     * a partire dal file xml contente la cascata di classificatori) allora in quel caso 
                     * viene aggiunto il corrispondente rettangolo alla lista dei rettangoli contenti 
                     * un presunto volto.
                     */
                    for (Stage stg : stages) {
                        if (!stg.pass(grayIntegralImage, squaredGrayIntegralImage, w, h, scale)) {
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

    /**
     * Unifica i rettangoli che identificano gli stessi volti, per farlo
     * confronta i rettangoli e se sono sufficientemente vicini da poter essere
     * considerati uguali aggiunge un raggruppamento.
     *
     * @param rectangleList
     * @param minimalNumberOfRectangleToBeConsideredNeighbors
     * @return
     */
    public ArrayList<Rectangle> unify(List<Rectangle> rectangleList, int minimalNumberOfRectangleToBeConsideredNeighbors) {
        /*
         * Il seguente array è un array di n elementi, tanti quanti sono i rettangoli in rectangleList,
         * in cui il rettangolo i-esimo fa parte del gruppo contenuto nella posizione i-esima dell'array.
         */
        int[] rectanglesCategory = new int[rectangleList.size()];
        int numberOfGroups = 0;
        for (int i = 0; i < rectangleList.size(); i++) {
            boolean found = false;
            for (int j = 0; j < i; j++) {
                /*
                 * Se sono uguali allora assegna al rettangolo i-esimo la categoria del rettangolo j-esimo
                 */
                if (equals(rectangleList.get(j), rectangleList.get(i))) {
                    found = true;
                    rectanglesCategory[i] = rectanglesCategory[j];
                }
            }
            /*
             * Se non sono uguali si attribuisce al rettangolo i-esimo un nuovo gruppo;
             */
            if (!found) {
                rectanglesCategory[i] = numberOfGroups;
                numberOfGroups++;
            }
        }
        /*
         * Popolo la lista delle posizioni dei rettangoli vicini neighborsList 
         * con tutti zeri, poi popolo l'array di rettangoli rect con rettangoli vuoti.
         */
        int[] neighborsList = new int[numberOfGroups];
        Rectangle[] rect = new Rectangle[numberOfGroups];
        for (int i = 0; i < numberOfGroups; i++) {
            neighborsList[i] = 0;
            rect[i] = new Rectangle(0, 0, 0, 0);
        }
        /*
         * Nel array rectanglesCategory per l'i-esimo rettangolo è salvata la sua categoria, 
         * ovvero la posizione del rettangolo n-esimo che può considerarsi equivalente e 
         * quindi da unire al rettangolo  i-esimo. Vengono quindi sovrascritti i rettangoli 
         * salvati in rect alla posizione fornita dalla posizione i dell'array rectanglesCategory
         * con i rettangoli alla posizione i della lista contente i rettangoli (contenti presubilmente volti) 
         * individuati dall'algoritmo findFaces.
         */
        for (int i = 0; i < rectangleList.size(); i++) {
            neighborsList[rectanglesCategory[i]]++;
            rect[rectanglesCategory[i]].x += rectangleList.get(i).x;
            rect[rectanglesCategory[i]].y += rectangleList.get(i).y;
            rect[rectanglesCategory[i]].height += rectangleList.get(i).height;
            rect[rectanglesCategory[i]].width += rectangleList.get(i).width;
        }
        /*
         * Ora viene popolata la lista di rettangoli che sarà ritorna come output dal programma. 
         * Questa operazione Le operazioni fatte sono necessarie per mediare il nuovo rettangolo
         */
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

    /**
     * Confronta due rettangoli decidendo se essi sono sufficientemente vicini
     * da essere considerati uguali.
     *
     * @param r1
     * @param r2
     * @return isEquals
     */
    public boolean equals(Rectangle r1, Rectangle r2) {
        int distance = (int) (r1.width * 0.2);
        boolean isEquals;
        if (r2.x <= r1.x + distance
                && r2.x >= r1.x - distance
                && r2.y <= r1.y + distance
                && r2.y >= r1.y - distance
                && r2.width <= (int) (r1.width * 1.2)
                && r1.width <= (int) (r2.width * 1.2)) {
            isEquals = true;
            return isEquals;
        }
        if (r1.x >= r2.x
                && r1.x + r1.width <= r2.x + r2.width
                && r1.y >= r2.y
                && r1.y + r1.height <= r2.y + r2.height) {
            isEquals = true;
            return isEquals;
        }
        isEquals = false;
        return isEquals;
    }

    /**
     * Ritorna la lista di tutti le posizioni del rettangolo usato per la
     * ricerca
     *
     * @return allPossibleRectangles
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

}
