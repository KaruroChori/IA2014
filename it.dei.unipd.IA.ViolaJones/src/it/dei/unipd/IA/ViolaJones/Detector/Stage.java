package it.dei.unipd.IA.ViolaJones.Detector;

import java.util.ArrayList;

/**
 * Ogni stage è fatto di diversi alberi e una threshold, ogni albero restituisce
 * un valore, se la somma dei valori restituiti supera la threshold, l'oggetto
 * cercato non é nella finestra.
 *
 */
public class Stage {

    private ArrayList<Tree> trees;
    private float threshold;

    /**
     * Questo costruttore riceve come parametro il valore si threshold threshold
     * per cui si considera superato questo stage.
     *
     * @param threshold
     */
    public Stage(float threshold) {
        this.threshold = threshold;
        trees = new ArrayList< Tree>();
    }

    /**
     * Aggiunge un albero alla lista che li contiene tutti, in sostanza
     * sovrascrive il metodo add di ArrayList attribuendogli un nome
     * maggiormente descrittivo per questo specifico utilizzo.
     *
     * @param tree
     */
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    /**
     * Calcola il valore che deve superare la soglia affinchè sia determinato
     * che nel rettangolo esaminato (questo metodo viene chiamato dal detector
     * al momento di determinare se nel rettangolo di ricerca c'è o meno una
     * faccia.
     *
     * @param grayIntImg
     * @param sqrIntImg
     * @param width
     * @param height
     * @param scale
     * @return passed
     */
    public boolean pass(int[][] grayIntImg, int[][] sqrIntImg, int width, int height, float scale) {
        float sum = 0;
        /*
         * Calcola la somma dei valori restituiti da ciascuno degli alberi.
         */
        for (Tree t : trees) {
            sum += t.getValue(grayIntImg, sqrIntImg, width, height, scale);
        }
        /* 
         * Se la somma supera la threshold allora fallisce.
         */
        boolean passed = sum > threshold;
        return passed;
    }
}
