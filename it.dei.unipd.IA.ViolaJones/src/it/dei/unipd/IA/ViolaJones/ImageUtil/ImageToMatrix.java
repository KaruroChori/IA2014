package it.dei.unipd.IA.ViolaJones.ImageUtil;

import java.awt.image.BufferedImage;

/**
 * Questa classe si occupa fornisce i metodi per convertire l'immagine in
 * matrice.
 *
 * @author enrico
 */
public class ImageToMatrix {

    /*
     * Rappresentazione nel buffer (in memoria) dell'immagine di interesse.
     */
    private BufferedImage image;
    /*
     * Matrice che conterrà l'immagine in forma matriciale
     */
    private int[][] matrixImage;
    /*
     * Altezza e largehzza dell'immagine
     */
    private int width, height;

    /**
     * Costruttore che inizializza le variabili dell'oggetto.
     *
     * @param img
     */
    public ImageToMatrix(BufferedImage img) {
        image = img;
        width = img.getWidth();
        height = img.getHeight();
        matrixImage = new int[width][height];
    }

    /**
     * Questo metodo effettua la conversione da immagine a rappresentazione
     * matriciale.
     *
     * @return
     */
    public int[][] getMatrix() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int color = image.getRGB(w, h);
                matrixImage[w][h] = color;
            }
        }
        return matrixImage;
    }

}
