package id.dei.unipd.IA.ViolaJones.ImageUtil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class GrayScaleImageEqualization {
    
    private int[][] matrixGrayImageWithNoise;
    private int[][] equalizedMatrixGrayImageWithNoise;
    private int width, height;
    
    public GrayScaleImageEqualization(int[][] img) {
        matrixGrayImageWithNoise = img;
        width = img.length;
        height = img[0].length;
        equalizedMatrixGrayImageWithNoise = new int[width][height];
    }
    
    /**
     *  Fonti:
     *  -http://it.wikipedia.org/wiki/Equalizzazione_dell'istogramma
    **/
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
        long min = width*height;
        for (int i = 0; i < 255; i++) {
            if(pixel[i] < min)
                min = pixel[i];
        }
        for (int i = 0; i < 256; i++) {
            double tmp = pixel[i]-min;
            double tmp1 = width*height-min;
            double tmp2 = tmp/tmp1;
            pixel[i] = Math.round(tmp2*(255.0));
        }
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                equalizedMatrixGrayImageWithNoise[w][h] = (int)pixel[matrixGrayImageWithNoise[w][h]];
            }
        }
        
        return equalizedMatrixGrayImageWithNoise;
    }
    
    public BufferedImage getImageEqualized() {
        BufferedImage imageEqualized = new BufferedImage(width,height,TYPE_INT_RGB);
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int sum = equalizedMatrixGrayImageWithNoise[w][h];
                int color = ((255 & 0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8)  | ((sum & 0xFF) << 0);
                imageEqualized.setRGB(w,h,color);
            }
        }
        return imageEqualized;
    }
    
}