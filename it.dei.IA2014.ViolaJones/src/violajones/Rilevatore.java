package violajones;

/*
 * Classi
 */
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

/*
 * Eccezioni
 */
import org.jdom2.JDOMException;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Rilevatore {

    private Point size;
    private LinkedList<Fasi> stages;
    private ArrayList<Rectangle> allPossibleRectangle;
    private ArrayList<Integer> sizeChangePosition;
    private ArrayList<Rectangle> rectangleList;
    private ArrayList<Rectangle> rectangleUnitedList;

    /*
     * Il costruttore principale prende in input una stringa con il percorso del file
     * xml contenente i dati.
     */
    public static Rilevatore create(String percorso) {
        Document haarcascade_xml = null;
        SAXBuilder sxb = new SAXBuilder();
        try {
            haarcascade_xml = sxb.build(new File(percorso));
        } catch (IOException e) {
            System.out.println("C'è qualche problema con la lettura del file.");
        } catch (JDOMException ex) {
            System.out.println("La libreria JDOM2 ha smesso di funzionare.");
        }
        return new Rilevatore(haarcascade_xml);
    }

    /*
     * Costruttore secondario, invocato dal primo, accetta come parametro un jdom2.Document
     * costruito dal costruttore principale.
     */
    public Rilevatore(Document document) {
        /*
         * Il rivelatore è costituito da fasi, ciascuna delle quali dice se la zona in esame rappresenta l'oggetto
         * con probabilità un po ' maggiore di 0,5. Se una zona passa tutte le fasi,
         * è considerato come rappresentante dell'oggetto
         */
        stages = new LinkedList<Fasi>();
        
        
        
        
        allPossibleRectangle = new ArrayList<Rectangle>();
        sizeChangePosition = new ArrayList<Integer>();
        rectangleList = new ArrayList<Rectangle>();
        rectangleUnitedList = new ArrayList<Rectangle>();
        /*
         * 
         */
        Element radice = (Element) document.getRootElement().getChildren().get(0);
        Scanner scanner = new Scanner(radice.getChild("size").getText());
        size = new Point(scanner.nextInt(), scanner.nextInt());
        Iterator iteratore = radice.getChild("stages").getChildren("_").iterator();
        while (iteratore.hasNext()) {
            Element stage = (Element) iteratore.next();
            float thres = Float.parseFloat(stage.getChild("stage_threshold").getText());
            Iterator iteratore2 = stage.getChild("trees").getChildren("_").iterator();
            //System.out.println(thres);///////////////////////////////////////////////////////////////////////////////////////////////////////
            Fasi st = new Fasi(thres);
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
                            valoreDXCÈ, size);
                    Iterator it3 = feature.getChild("feature").getChild("rects").getChildren("_").iterator();
                    while (it3.hasNext()) {
                        String s = ((Element) it3.next()).getText().trim();
                        System.out.println(s); ///////////////////////////////////////////////
                        Rettangolo r = Rettangolo.convert(s);
                        f.aggiungi(r);
                    }
                    t.addFeature(f);
                }
                st.addTree(t);
            }
            stages.add(st);
        }
    }
    
    /**
     * Riceve in input l'immagine integrale, l'immagine integrale quadratica, un valore detto scala_base 
     * che definisce la grandezza della finestra di ricerca, un valore scala_inc che è l'incremento dato 
     * ad ogni iterazione del ciclo fino al valore massimo scala_max calcolato considerando il valore minimo
     * fra larghezza e altezza dell'immagine divise rispettivamente per il valore x e y di size (che altro 
     * non è che la descrizione di una finestra quadrata 24x24). 
     * 
     * @param grayIntImg
     * @param sqrGrayIntImg
     * @param scala_base
     * @param scala_inc
     * @param incrementa
     * @param vicini
    */
    public ArrayList<Rectangle> trovaVolti(int[][] grayIntImg, int[][] sqrGrayIntImg,
            float scala_base, float scala_inc, float incrementa, int vicini) {
        int width = grayIntImg.length;
        int height = grayIntImg[0].length;
        /*
         * Calcolo quante volte ci sta il detector nell'immagine
         */
        float scala_max = (Math.min((float)(width) / size.x, (float)(height) / size.y));
        /*
         * Cuore dell'algoritmo
         */
        for (float scala = scala_base; scala < scala_max; scala *= scala_inc) {
            int passi = (int) (scala * size.x * incrementa);
            int dim = (int) (scala * this.size.x);
            sizeChangePosition.add(allPossibleRectangle.size()); //next position chaged size
            for (int w = 0; w < width - dim; w += passi) {
                for (int h = 0; h < height - dim; h += passi) {
                    boolean passa = true;
                    allPossibleRectangle.add(new Rectangle(w, h, dim, dim));
                    for (Fasi stg : stages) {
                        if (!stg.pass(grayIntImg, sqrGrayIntImg, w, h, scala)) {
                            passa = false;
                            //System.out.println("don't pass"); ////////////////////////////////////////////////////////////////////////////
                            break;
                        }
                    }
                    if (passa) {
                        rectangleList.add(new Rectangle(w, h, dim, dim));
                        //System.out.println("pass"); ///////////////////////////////////////////////////////////////////
                    }
                    //System.out.println(w + " " + h); ///////////////////////////////////////////////////////////
                }                
            }
        }
        System.out.println("Rettangoli: " + rectangleList.size()); ///////////////////////////////////////////////////////////////////////////////7
        return unifico(rectangleList, vicini);
    }

    //public List<Rectangle> unificati;    
    
    public ArrayList<Rectangle> unifico(List<Rectangle> lista_rettangoli, int vicini) {
        
        int[] rettangoli = new int[lista_rettangoli.size()];
        int numeroDiClassi = 0;
        for (int i = 0; i < lista_rettangoli.size(); i++) {
            boolean trovato = false;
            for (int j = 0; j < i; j++) {
                if (equals(lista_rettangoli.get(j), lista_rettangoli.get(i))) {
                    trovato = true;
                    rettangoli[i] = rettangoli[j];
                }
            }
            if (!trovato) {
                rettangoli[i] = numeroDiClassi;
                numeroDiClassi++;
            }
        }
        int[] lista_vicini = new int[numeroDiClassi];
        Rectangle[] ret = new Rectangle[numeroDiClassi];
        for (int i = 0; i < numeroDiClassi; i++) {
            lista_vicini[i] = 0;
            ret[i] = new Rectangle(0, 0, 0, 0);
        }
        for (int i = 0; i < lista_rettangoli.size(); i++) {
            lista_vicini[rettangoli[i]]++;
            ret[rettangoli[i]].x += lista_rettangoli.get(i).x;
            ret[rettangoli[i]].y += lista_rettangoli.get(i).y;
            ret[rettangoli[i]].height += lista_rettangoli.get(i).height;
            ret[rettangoli[i]].width += lista_rettangoli.get(i).width;
        }
        System.out.println("numero di classi " + numeroDiClassi);
        for (int i = 0; i < numeroDiClassi; i++) {
            int n = lista_vicini[i];
            if (n >= vicini) {
                Rectangle r = new Rectangle(0, 0, 0, 0);
                r.x = (ret[i].x * 2 + n) / (2 * n);
                r.y = (ret[i].y * 2 + n) / (2 * n);
                r.width = (ret[i].width * 2 + n) / (2 * n);
                r.height = (ret[i].height * 2 + n) / (2 * n);
                rectangleUnitedList.add(r);
            }
        }
        System.out.println("Rettangoli uniti: " + rectangleUnitedList.size());
        //unificati = rectangleUnitedList;
        return rectangleUnitedList;
    }

    public boolean equals(Rectangle r1, Rectangle r2) {
        int distanza = (int) (r1.width * 0.2);
        if (r2.x <= r1.x + distanza
                && r2.x >= r1.x - distanza
                && r2.y <= r1.y + distanza
                && r2.y >= r1.y - distanza
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
    
    
    public ArrayList<Rectangle> getAllPossibleRect() {
        return allPossibleRectangle;
    }    
    public ArrayList<Integer> getsizeChangePosition() {
        return sizeChangePosition;
    }
    public ArrayList<Rectangle> getlista_rettangoli() {
        return rectangleList;
    }
    public ArrayList<Rectangle> getrettangoli_uniti() {
        return rectangleUnitedList;
    }
}
