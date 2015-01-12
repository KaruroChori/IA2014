package it.dei.unipd.IA.ViolaJones.ImageUtil;

/*
 * Import delle classi
 */
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Questa classe effettua l'equalizzazione dell'istogramma (ottenuto dai valori
 * corrispondenti ai pixel dell'immagine e la loro frequenza in essa) che
 * permette di regolare il contrasto dell'immagine in scala di grigi.
 */
public class GrayScaleImageEqualization {

    private int[][] matrixGrayImageWithNoise;
    private int[][] equalizedMatrixGrayImageWithNoise;
    private int width, height;

    /**
     * Il costruttore prende in input l'immagine in scala di grigi sottoforma di
     * matrice a cui applicare l'equalizzazione dell'istogramma.
     *
     * @param img
     */
    public GrayScaleImageEqualization(int[][] img) {
        matrixGrayImageWithNoise = img;
        width = img.length;
        height = img[0].length;
        equalizedMatrixGrayImageWithNoise = new int[width][height];
        getMatrixEqualized();
    }
    /*
     * Fonti:
     * -http://it.wikipedia.org/wiki/Equalizzazione_dell'istogramma
     */

    /**
     * Applica l'equalizzazione dell'istogramma all'immagine in scala di grigi
     *
     * @return l'immagine matriciale in scala grigi a seguito dell'applicazione
     * dell'equalizzazione dell'istogramma
     */
    public int[][] getMatrixEqualized() {
        long[] pixel = new long[256];
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                pixel[matrixGrayImageWithNoise[w][h]]++;
            }
        }
        long sum = 0;
        for (int i = 0; i < 256; i++) {
            sum = sum + pixel[i];
            pixel[i] = sum;
        }
        long min = width * height;
        for (int i = 0; i < 255; i++) {
            if (pixel[i] < min) {
                min = pixel[i];
            }
        }
        for (int i = 0; i < 256; i++) {
            double tmp = pixel[i] - min;
            double tmp1 = width * height - min;
            double tmp2 = tmp / tmp1;
            pixel[i] = Math.round(tmp2 * (255.0));
        }
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                equalizedMatrixGrayImageWithNoise[w][h] = (int) pixel[matrixGrayImageWithNoise[w][h]];
            }
        }
        return equalizedMatrixGrayImageWithNoise;
    }

    /**
     * Converte l'immagine in scala di grigi a seguito dell'applicazione
     * dell'equalizzazione dell'istogramma in coordinate RGB
     *
     * @return BufferedImage imageEqualized - l'immagine in scala di grigi a
     * seguito dell'applicazione dell'equalizzazione dell'istogramma in RGB
     */
    public BufferedImage getImageEqualized() {
        BufferedImage imageEqualized = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int sum = equalizedMatrixGrayImageWithNoise[w][h];
                int color = ((255 & 0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8) | ((sum & 0xFF) << 0);
                imageEqualized.setRGB(w, h, color);
            }
        }
        return imageEqualized;
    }
}
