package violajones;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* 
 * Ogni stage è fatto di diversi alberi e una soglia.
 * Ogni albero restituisce un valore. Se la somma dei valori restituiti supera la soglia, 
 * l'oggetto cercato non é nella finestra.
 */
public class Fasi {

    private List<Tree> trees;
    private float soglia;

    public Fasi(float limite) {
        soglia = limite;
        trees = new LinkedList< Tree>();
    }

    public void addTree(Tree t) {
        trees.add(t);
    }

    public boolean pass(int[][] grayIntImg, int[][] sqrIntImg, int i, int j, float scala) {
        float sum = 0;
        //System.out.println(soglia); ////////////////////////////////////////////////////////////////////////
        /*
         * Calcola la somma dei valori restituiti da ciascuno degli alberi.
         */
        for (Tree t : trees) {
            sum += t.getValue(grayIntImg, sqrIntImg, i, j, scala);
        }
        //System.out.println(sum); /////////////////////////////////////////////////////////////////////////////
        /* 
         * Se la somma supera la soglia allora fallisce.
         */
        return sum > soglia;
    }
}
