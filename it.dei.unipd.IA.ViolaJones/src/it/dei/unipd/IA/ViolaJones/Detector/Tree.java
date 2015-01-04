package it.dei.unipd.IA.ViolaJones.Detector;

/*
 * Import delle classi
 */
import java.util.ArrayList;

/**
 * Un semplice albero binario, necessario per organizzare la ricerca e i dati 
 * in nostro possesso.
 */
public class Tree {

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    private ArrayList<Feature> features;

    /**
     * Costruisce una lista di feature.
     */
    public Tree() {
        features = new ArrayList<Feature>();
    }

    /**
     * Aggiunge una feature alla lista che le contiene tutte, in sostanza
     * sovrascrive il metodo add di ArrayList attribuendogli un nome
     * maggiormente descrittivo per questo specifico utilizzo.
     *
     * @param feature
     */
    public void addFeature(Feature feature) {
        features.add(feature);
    }

    /**
     * 
     * 
     * @param grayIntegralImage
     * @param squareGrayIntegralImage
     * @param width
     * @param height
     * @param scale
     * @return currentNodeValue
     */
    public float getValue(int[][] grayIntegralImage, int[][] squareGrayIntegralImage, int width, int height, float scale) {
        /*
         * Parto dalla radice
         */
        Feature currentNode = features.get(0);
        while (true) {
            int position = currentNode.chooseRightOrLeftNode(grayIntegralImage, squareGrayIntegralImage, width, height, scale);
            if (position == LEFT) {
                if (currentNode.leftValuePresent) {
                    return currentNode.leftValue;
                } else {
                    /*
                     * Mi devo spostare nel figlio sinistro
                     */
                    currentNode = features.get(currentNode.leftNode);
                }
            } else {
                if (currentNode.rightValuePresent) {
                    return currentNode.rightValue;
                } else {
                    /*
                     * Se non sono sul nodo destro, mi sposto su di esso
                     */
                    currentNode = features.get(currentNode.rightNode);
                }
            }
        }
    }
}
