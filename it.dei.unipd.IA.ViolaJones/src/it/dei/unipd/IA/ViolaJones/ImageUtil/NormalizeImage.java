package it.dei.unipd.IA.ViolaJones.ImageUtil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.floor;
import java.util.Arrays;

/**
 * Questa classe esegue la normalizzazione dell'immagine di ingresso,
 * restituendo una versione a media zero e varianza unitaria.Lo scopo principale
 * è quello di ridurre la dipendenza dei colori dalle condizioni di luce in cui
 * l'immagine è stata catturata.
 */
public class NormalizeImage {

    private int[][] image;
    private int width, height;
    private int[][] temp;

    /**
     * Il costruttore prende in input l'immagine sottoforma di matrice a cui si
     * vuole applicare la normalizzazione.
     *
     */
    public NormalizeImage(int[][] img) {
        image = img;
        width = img.length;
        height = img[0].length;
        temp = new int[width][height];
    }

    public void ApplyFilter(int sigma) {
        int times[] = new int[256];
        long mean = 0;
        long variance = 0;

        // Valuto la distribuzione del colore e la media
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                times[image[w][h]]++;
                mean += image[w][h];
            }
        }

        mean /= (width * height);

        // Calcolo la varianza
        for (int i = 0; i != 256; i++) {
            variance += Math.pow(i - mean, 2) * times[i];
        }

        variance /= (width * height);

        double stddev = Math.sqrt((double) variance);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {

                //Queste due righe operano la normalizzazione. Teoricamente noi dovremo usare l'immagine così
                //anche per il calcolo dell'immagine integrale. Riconvertirla in immagine è possibile, perdendo 
                //delle componenti troppo esterne  ma non lo trovo l'approccio giusto da seguire se non come 
                //semplice debug visivo... ASSUMENDO temp di tipo double ovviamente e non intero come invece ho messo!
                //OPTION:1[
                //temp[w][h]=(double)image[w][h]-mean;
                //temp[w][h]/=stddev;
                //]
                //Per una normalizzazione visibile tenete questa parte invece. Ma ripeto non è molto bello usarla 
                //per fare calcoli sull'immagine in seguito...
                //OPTION:2[
                temp[w][h] = image[w][h] - (int) mean;											//Allinea la media allo 0
                if (temp[w][h] < -((double) sigma * stddev)) {
                    temp[w][h] = -128;					//Satura i pezzi fuori dominio
                } else if (temp[w][h] > ((double) sigma * stddev)) {
                    temp[w][h] = 127;				//Satura i pezzi fuori dominio
                } else {
                    temp[w][h] *= ((double) sigma * (double) stddev) / (double) 256;			//Riscalo il dominio dei colori...
                }
                temp[w][h] += 128;
                //]
            }
        }

        return;
    }

    /**
     * Restituisce l'immagine in scala di grigi sottoforma di matrice a cui è
     * stata applicata la normalizzazione
     *
     * @return l'immagine matriciale in scala grigi a cui è stata applicata la
     * normalizzazione
     */
    public int[][] getNormalizeMatrixIMage() {
        return temp;
    }

    /**
     * Restituisce l'immagine in scala di grigi a cui è stata applicata la
     * normalizzazione
     *
     * @return l'immagine in scala di grigi in RGB in seguito all'applicazione
     * della normalizzazione
     */
    public BufferedImage getFilteredImage() {
        BufferedImage filteredImage = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int sum = temp[w][h];
                int grayColor = ((0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8) | ((sum & 0xFF) << 0);
                filteredImage.setRGB(w, h, grayColor);
            }
        }
        return filteredImage;
    }

}
