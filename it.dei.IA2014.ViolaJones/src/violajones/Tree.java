package violajones;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    public final static int SINISTRA = 0;
    public final static int DESTRA = 1;
    private List<Feature> features;

    public Tree() {
        features = new ArrayList<Feature>();
    }

    public void addFeature(Feature f) {
        features.add(f);
    }

    public float getValue(int[][] grayIntImg, int[][] sqrIntImg, int i, int j, float scala) {
        /*
         * Parto dalla radice
         */
        Feature nodo_corrente = features.get(0);
        while (true) {
            int position = nodo_corrente.scegliNodoSXDX(grayIntImg, sqrIntImg, i, j, scala);
            if (position == SINISTRA) {
                if (nodo_corrente.valoreSXCÈ) {
                    return nodo_corrente.valoreSX;
                } else {
                    /*
                     * Mi devo spostare nel figlio sinistro
                     */
                    nodo_corrente = features.get(nodo_corrente.nodoSX);
                }
            } else {
                if (nodo_corrente.valoreDXCÈ) {
                    return nodo_corrente.valoreDX;
                } else {
                    /*
                     * Mi devo spostare sul filgio destro
                     */
                    nodo_corrente = features.get(nodo_corrente.nodoDX);
                }
            }
        }
    }
}
