package it.dei.unipd.IA.ViolaJones.ImageUtil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Questa classe effettua la conversione dell'immagine in scala di grigi.
 *
 * @author enrico
 */
public class GrayImage {

    /*
     * La matrice che conterrà l'immagine matriciale di input.
     */
    private int[][] matrix;
    /*
     * Dimensioni della matrice
     */
    private int height, width;
    /*
     * La matrice che conterrà l'immagine matriciale in scala di grigi.
     */
    int[][] mtrxGrImg;

    /**
     * Costruisce l'oggetto dato un immagine matriciale di input, assegnando
     * tutte le variabili necessarie.
     *
     * @param matrix
     */
    public GrayImage(int[][] matrix) {
        this.matrix = matrix;
        width = this.matrix.length;
        height = this.matrix[0].length;
        mtrxGrImg = new int[width][height];
        getGrayMatrixImage();
    }

    /**
     * Converte l'immagine in scala di grigi sottoforma di matrice
     *
     * @return int[][] mtrxGrImg - l'immagine matriciale in scala grigi
     */
    public int[][] getGrayMatrixImage() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int color = matrix[w][h];
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = (color) & 0xFF;
                int sum = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
                mtrxGrImg[w][h] = sum;
            }
        }
        return mtrxGrImg;
    }

    /**
     * Converte l'immagine in scala di grigi in coordinate RGB
     *
     * @return BufferedImage grayImage - l'immagine in scala di grigi in RGB
     */
    public BufferedImage getGrayImage() {
        BufferedImage grayImage = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int sum = matrix[w][h];
                int grayColor = ((255 & 0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8) | ((sum & 0xFF) << 0);
                grayImage.setRGB(w, h, grayColor);
            }
        }
        return grayImage;
    }

}
