package it.dei.unipd.IA.ViolaJones.Learning;

import java.awt.image.BufferedImage;

/**
 * Questa classe contiene un immagine, ne fornisce i metodi di modifica e di
 * accesso.
 *
 * @author enrico
 */
class Image {

    /*
     * Immagine di input.
     */
    private final BufferedImage image;
    /*
     * Matrice che conterrà la rappresentazione matriciale dell'immagine.
     */
    private final int[][] matrixImage;
    /*
     * Informazioni relative all'altezza e alla larghezza dell'immagine.
     */
    private final int width, height;
    /*
     * Matrice che conterrà la rappresentazione integrale dell'immagine.
     */
    private final int[][] grayIntegralImage;
    /*
     * Matrice che conterrà la rappresentazione integrale quadratica dell'imagine.
     */
    private final int[][] squaredGrayIntegralImage;
    /*
     * Variabile che conterrà il valore di una fearure calcolata per questa immagine.
     */
    private int rectanglesSum;
    /*
     * Indica se l'immagine è un sample positivo (c'è un volto, 1) o negativo 
     * (non c'è un volto, -1)
     */
    private int isFace;

    /**
     * Costruttore dell'immagine, inizializza le variabili necessarie e compie
     * tutte le operazioni necessarie per preprare l'immagine all'elaborazione.
     *
     * @param img
     * @param isFace
     */
    public Image(BufferedImage img, int isFace) {
        image = img;
        width = img.getWidth();
        height = img.getHeight();
        matrixImage = new int[width][height];
        grayIntegralImage = new int[width][height];
        squaredGrayIntegralImage = new int[width][height];
        getMatrix();
        convertAlreadyGrayImageInToGrayMatrixImage();
        generateMatrixIntegralImage();
        rectanglesSum = 0;
        this.isFace = isFace;
    }

    /**
     * Metodo privato per la conversione dell'immagine in matrice.
     *
     * @return matrixImage
     */
    private int[][] getMatrix() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int color = image.getRGB(w, h);
                matrixImage[w][h] = color;
            }
        }
        return matrixImage;
    }

    /**
     * Metodo privato che applica una trasformazione biunivoca ad un immagine in
     * scala di grigi, diminuendo la complessità della rappresentazione senza
     * perdere infromazione.
     *
     * @return matrixImage
     */
    private int[][] convertAlreadyGrayImageInToGrayMatrixImage() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int color = matrixImage[w][h];
                int grey = (color >> 16) & 0xFF;
                matrixImage[w][h] = grey;
            }
        }
        return matrixImage;
    }

    /**
     * Metodo privato che applica una trasformazione ad un immagine creando la
     * sua rappresentazione integrale.
     */
    private void generateMatrixIntegralImage() {
        for (int w = 0; w < width; w++) {
            int sumOfColumn = 0;
            int sumOfColumn2 = 0;
            for (int h = 0; h < height; h++) {
                int sum = matrixImage[w][h];
                grayIntegralImage[w][h] = (w > 0 ? grayIntegralImage[w - 1][h] : 0) + sumOfColumn + sum;
                squaredGrayIntegralImage[w][h] = (w > 0 ? squaredGrayIntegralImage[w - 1][h] : 0) + sumOfColumn2 + sum * sum;
                sumOfColumn += sum;
                sumOfColumn2 += sum * sum;
            }
        }
    }

    /**
     * Metodo pubblico per l'accesso all'immagine integrale quadratica.
     *
     * @return matrixImage
     */
    public int[][] getSquaredMatrixIntegralImageGray() {
        return squaredGrayIntegralImage;
    }

    /**
     * Metodo pubblico per l'accesso all'immagine integrale.
     *
     * @return matrixImage
     */
    public int[][] getMatrixIntegralImageGray() {
        return grayIntegralImage;
    }

    /**
     * Metodo pubblico per il calcolo del valore di una feature su questa
     * immagine.
     *
     * @return matrixImage
     */
    public int evaluateFeature(Feature feature) {
        for (int n = 0; n < feature.getSize(); n++) {
            int tmp = 0;
            MyRectangle rectangle = feature.getRectangle(n);
            int rectangleX1 = rectangle.x;
            int rectangleX2 = (rectangle.x + rectangle.width - 1);
            int rectangleY1 = rectangle.y;
            int rectangleY2 = (rectangle.y + rectangle.height - 1);
            try {
                tmp = (int) ((grayIntegralImage[rectangleX1][rectangleY1]
                        + grayIntegralImage[rectangleX2][rectangleY2]
                        - grayIntegralImage[rectangleX1][rectangleY2]
                        - grayIntegralImage[rectangleX2][rectangleY1])
                        * rectangle.weight);
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                System.out.println("Errore");
            }
            rectanglesSum = rectanglesSum + tmp;
        }
        return rectanglesSum;
    }

    /**
     * Check della classificazione durante la fase di training.
     *
     * @return matrixImage
     */
    public float evaluateTrainedFeature(Feature feature) {
        int val = evaluateFeature(feature);
        float returnInt;
        if ((feature.getPolarity() * val < feature.getPolarity() * feature.getThreshold())) {
            //System.out.println("weight " + feature.getWeight());
            returnInt = 1 * feature.getWeight();
        } else {
            returnInt = 0 * feature.getWeight();
        }
        return returnInt;
    }

    /**
     * Metodo di accesso al tipo di sample, positivo o negativo.
     *
     * @return isFace
     */
    public int getType() {
        return isFace;
    }

    /**
     * Metodo di settaggio del tipo di sample, positivo o negativo.
     *
     * @param isFace
     */
    public void setType(int isFace) {
        this.isFace = isFace;
    }

}
