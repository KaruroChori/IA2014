package it.dei.unipd.IA.ViolaJones.Detector;

/*
 * Import delle classi
 */
import java.util.ArrayList;

/**
 * Un semplice albero binario, necessario per organizzare la ricerca e i dati in
 * nostro possesso.
 */
public class Tree {

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    private ArrayList<Feature> featuresList;

    /**
     * Costruisce una lista di feature.
     */
    public Tree() {
        featuresList = new ArrayList<Feature>();
    }

    /**
     * Aggiunge una feature alla lista che le contiene tutte, in sostanza
     * sovrascrive il metodo add di ArrayList attribuendogli un nome
     * maggiormente descrittivo per questo specifico utilizzo.
     *
     * @param feature
     */
    public void addFeature(Feature feature) {
        featuresList.add(feature);
    }

    /**
     * Questo metodo ritorna il valore del nodo destro o sinistro in base al
     * risultato del metodo chooseRightOrLeftNode
     *
     * @param grayIntegralImage
     * @param squareGrayIntegralImage
     * @param x
     * @param y
     * @param scale
     * @return currentNodeValue
     */
    public float getValue(int[][] grayIntegralImage, int[][] squareGrayIntegralImage, int x, int y, float scale) {
        /*
         * Parto dalla radice
         */
        Feature currentNode = featuresList.get(0);
        while (true) {
            int position = currentNode.chooseRightOrLeftNode(grayIntegralImage, squareGrayIntegralImage, x, y, scale);
            if (position == LEFT) {
                if (currentNode.leftValuePresent) {
                    return currentNode.leftValue;
                } else {
                    /*
                     * Mi devo spostare nel figlio sinistro
                     */
                    currentNode = featuresList.get(currentNode.leftNode);
                }
            } else {
                if (currentNode.rightValuePresent) {
                    return currentNode.rightValue;
                } else {
                    /*
                     * Se non sono sul nodo destro, mi sposto su di esso
                     */
                    currentNode = featuresList.get(currentNode.rightNode);
                }
            }
        }
    }

    /**
     * Ritorna un'array contente tutte le feature per un determinato albero.
     *
     * @return featuresList
     */
    public ArrayList<Feature> getFeaturesList() {
        return featuresList;
    }
}
