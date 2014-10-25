package imageutil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class NoiseReduction {
    
    private int[][] matrixGrayImage;
    private int width, height;
    private int[][] noNoiseMatrixGrayImage;
    private final int[][] kernel;
    
    public NoiseReduction(int[][] img) {
        this.kernel = new int[][]{{ 2, 4 , 5 , 4 , 2 }, { 4 , 9 , 12 , 9 , 4 }, 
            { 5 , 12 , 15 , 12 , 5 }, { 4 , 9 , 12 , 9 , 4 }, { 2 , 4 , 5 , 4 , 2 }};
        matrixGrayImage = img;
        width = matrixGrayImage.length;
        height = matrixGrayImage[0].length;
        noNoiseMatrixGrayImage = new int[width][height];    
    }
    
    public int[][] getNoNoiseMatrixGrayImage() {
        /** Convoluzione dell'immagine con un filtro gaussiano.
         *  Fonti:
         *  -http://en.wikipedia.org/wiki/Canny_edge_detector#Noise_reduction
         *  -http://rosettacode.org/wiki/Category:Java
         *  -http://en.wikipedia.org/wiki/Gaussian_blur
         *  -http://en.wikipedia.org/wiki/Kernel_(image_processing)
        **/
        for(int i = 2; i < noNoiseMatrixGrayImage.length-2; i++) {
            for(int j = 2; j < noNoiseMatrixGrayImage[0].length-2; j++) {
                int sum =0;
                sum+=2*matrixGrayImage[i-2][j-2];
                sum+=4*matrixGrayImage[i-2][j-1];
                sum+=5*matrixGrayImage[i-2][j+0];
                sum+=4*matrixGrayImage[i-2][j+1];
                sum+=2*matrixGrayImage[i-2][j+2];
                sum+=4*matrixGrayImage[i-1][j-2];
                sum+=9*matrixGrayImage[i-1][j-1];
                sum+=12*matrixGrayImage[i-1][j+0];
                sum+=9*matrixGrayImage[i-1][j+1];
                sum+=4*matrixGrayImage[i-1][j+2];
                sum+=5*matrixGrayImage[i+0][j-2];
                sum+=12*matrixGrayImage[i+0][j-1];
                sum+=15*matrixGrayImage[i+0][j+0];
                sum+=12*matrixGrayImage[i+0][j+1];
                sum+=5*matrixGrayImage[i+0][j+2];
                sum+=4*matrixGrayImage[i+1][j-2];
                sum+=9*matrixGrayImage[i+1][j-1];
                sum+=12*matrixGrayImage[i+1][j+0];
                sum+=9*matrixGrayImage[i+1][j+1];
                sum+=4*matrixGrayImage[i+1][j+2];
                sum+=2*matrixGrayImage[i+2][j-2];
                sum+=4*matrixGrayImage[i+2][j-1];
                sum+=5*matrixGrayImage[i+2][j+0];
                sum+=4*matrixGrayImage[i+2][j+1];
                sum+=2*matrixGrayImage[i+2][j+2];
                noNoiseMatrixGrayImage[i][j]=sum/159;
            }
        }
        return noNoiseMatrixGrayImage;
    }
            
    public BufferedImage getNoNoiseGrayImage() {
        BufferedImage noNoiseGrayImage = new BufferedImage(width,height,TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {            
            for (int w = 0; w < width; w++) {
                int sum = noNoiseMatrixGrayImage[w][h];
                int grayColor = ((255 & 0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8)  | ((sum & 0xFF) << 0);
                noNoiseGrayImage.setRGB(w,h,grayColor);
            }
        }
        return noNoiseGrayImage;
    }
        
    //SPERIMENTALE PER ORA NON FUNZIONA
    public BufferedImage getNoNoiseColorImage() {
        BufferedImage noNoiseColorImage = new BufferedImage(width,height,TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {            
            for (int w = 0; w < width; w++) {
                int sum = noNoiseMatrixGrayImage[w][h];
                int red = (int)(sum/0.299); //ERRORE
                int blue = (int)(sum/0.114); //ERRORE
                int green = (int)(sum/0.587); //ERRORE
                int grayColor = ((255 & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8)  | ((blue & 0xFF) << 0); //ERRORE
                noNoiseColorImage.setRGB(w,h,grayColor);
            }
        }
        return noNoiseColorImage;
    }
    
}
