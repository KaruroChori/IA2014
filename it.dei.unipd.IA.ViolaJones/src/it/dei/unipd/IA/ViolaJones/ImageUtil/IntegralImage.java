package it.dei.unipd.IA.ViolaJones.ImageUtil;

public class IntegralImage {

    /*
     * Immagine matriciale originale.
     */
    private int[][] grayMatrixImage;
    /*
     * Dimensioni della matrice.
     */
    private int width, height;
    /*
     * Immagine matriciale integrale.
     */
    private int[][] matrixIntegralImageGray;
    /*
     * Immagine matriciale integrale quadratica.
     */
    private int[][] squaredMatrixIntegralImageGray;

    /**
     * Il costruttore prende in input l'immagine sottoforma di matrice e
     * inizializza le variabili.
     *
     * @param img
     */
    public IntegralImage(int[][] img) {
        grayMatrixImage = img;
        width = img.length;
        height = img[0].length;
        matrixIntegralImageGray = new int[width][height];
        squaredMatrixIntegralImageGray = new int[width][height];
    }

    /**
     * Effettua le operazioni necessarie al calcolo dell'immagine integrale e
     * immagine integrale quadratica secondo le formule.
     */
    public void getMatrixIntegralImage() {
        for (int w = 0; w < width; w++) {
            int sumOfColumn = 0;
            int sumOfColumn2 = 0;
            for (int h = 0; h < height; h++) {
                int sum = grayMatrixImage[w][h];
                matrixIntegralImageGray[w][h] = (w > 0 ? matrixIntegralImageGray[w - 1][h] : 0) + sumOfColumn + sum;
                squaredMatrixIntegralImageGray[w][h] = (w > 0 ? squaredMatrixIntegralImageGray[w - 1][h] : 0) + sumOfColumn2 + sum * sum;
                sumOfColumn += sum;
                sumOfColumn2 += sum * sum;
            }
        }
    }

    /**
     * Restituisce l'immagine integrale sottoforma di matrice
     *
     * @return int[][] squaredMatrixIntegralImageGray - l'immagine integrale
     * quadratica
     */
    public int[][] getSquaredMatrixIntegralImageGray() {
        return squaredMatrixIntegralImageGray;
    }

    /**
     * Restituisce l'immagine integrale quadratica sottoforma di matrice
     *
     * @return int[][] matrixIntegralImageGray - l'immagine integrale
     */
    public int[][] getMatrixIntegralImageGray() {
        return matrixIntegralImageGray;
    }

}
