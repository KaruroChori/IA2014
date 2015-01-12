package it.dei.unipd.IA.ViolaJones.ImageUtil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Questa classe effettua la riduzione del rumore tramite una funzione di filtro
 * il cui scopo è evidenziare i pattern significativi, attenuando il rumore
 * generato da artefatti ambientali, elettrici, elettronici, informatici o
 * fisiologici oppure altri fenomeni di disturbo legati a fattori di scala molto
 * piccoli.
 */
public class NoiseReduction {

    private int[][] matrixGrayImage;
    private int width, height;
    private int[][] noNoiseMatrixGrayImage;
    private final int[][] kernel;

    /**
     * Il costruttore prende in input l'immagine sottoforma di matrice a cui si
     * vuole applicare il filtro.
     *
     */
    public NoiseReduction(int[][] img) {
        this.kernel = new int[][]{{2, 4, 5, 4, 2}, {4, 9, 12, 9, 4},
        {5, 12, 15, 12, 5}, {4, 9, 12, 9, 4}, {2, 4, 5, 4, 2}};
        matrixGrayImage = img;
        width = matrixGrayImage.length;
        height = matrixGrayImage[0].length;
        noNoiseMatrixGrayImage = new int[width][height];
        getNoNoiseMatrixGrayImage();
    }

    /**
     * Effettua la convoluzione dell'immagine con un filtro gaussiano con lo
     * scopo di ottenerne una con una leggera sfocatura gaussiana, in cui nessun
     * pixel è affetto da disturbi di livello significativo.Fonti:
     * -http://en.wikipedia.org/wiki/Canny_edge_detector#Noise_reduction
     * -http://rosettacode.org/wiki/Category:Java
     * -http://en.wikipedia.org/wiki/Gaussian_blur
     * -http://en.wikipedia.org/wiki/Kernel_(image_processing) .
     */
    public int[][] getNoNoiseMatrixGrayImage() {
        for (int i = 2; i < noNoiseMatrixGrayImage.length - 2; i++) {
            for (int j = 2; j < noNoiseMatrixGrayImage[0].length - 2; j++) {
                int sum = 0;
                sum += 2 * matrixGrayImage[i - 2][j - 2];
                sum += 4 * matrixGrayImage[i - 2][j - 1];
                sum += 5 * matrixGrayImage[i - 2][j + 0];
                sum += 4 * matrixGrayImage[i - 2][j + 1];
                sum += 2 * matrixGrayImage[i - 2][j + 2];
                sum += 4 * matrixGrayImage[i - 1][j - 2];
                sum += 9 * matrixGrayImage[i - 1][j - 1];
                sum += 12 * matrixGrayImage[i - 1][j + 0];
                sum += 9 * matrixGrayImage[i - 1][j + 1];
                sum += 4 * matrixGrayImage[i - 1][j + 2];
                sum += 5 * matrixGrayImage[i + 0][j - 2];
                sum += 12 * matrixGrayImage[i + 0][j - 1];
                sum += 15 * matrixGrayImage[i + 0][j + 0];
                sum += 12 * matrixGrayImage[i + 0][j + 1];
                sum += 5 * matrixGrayImage[i + 0][j + 2];
                sum += 4 * matrixGrayImage[i + 1][j - 2];
                sum += 9 * matrixGrayImage[i + 1][j - 1];
                sum += 12 * matrixGrayImage[i + 1][j + 0];
                sum += 9 * matrixGrayImage[i + 1][j + 1];
                sum += 4 * matrixGrayImage[i + 1][j + 2];
                sum += 2 * matrixGrayImage[i + 2][j - 2];
                sum += 4 * matrixGrayImage[i + 2][j - 1];
                sum += 5 * matrixGrayImage[i + 2][j + 0];
                sum += 4 * matrixGrayImage[i + 2][j + 1];
                sum += 2 * matrixGrayImage[i + 2][j + 2];
                noNoiseMatrixGrayImage[i][j] = sum / 159;
            }
        }
        return noNoiseMatrixGrayImage;
    }

    /**
     * Restituisce l'immagine in scala di grigi a cui è stato applicato il
     * filtro per la riduzione del rumore
     *
     * @return l'immagine in scala di grigi in RGB in seguito all'applicazione
     * del filtro per la riduzione del rumore
     */
    public BufferedImage getNoNoiseGrayImage() {
        BufferedImage noNoiseGrayImage = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int sum = noNoiseMatrixGrayImage[w][h];
                int grayColor = ((255 & 0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8) | ((sum & 0xFF) << 0);
                noNoiseGrayImage.setRGB(w, h, grayColor);
            }
        }
        return noNoiseGrayImage;
    }

}
