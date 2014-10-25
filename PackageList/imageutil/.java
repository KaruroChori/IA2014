package imageutil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class GrayImage {
    
    private int[][] matrixGrayImage;
    private int height, width;
    
    public GrayImage(int[][] matrix) {
        matrixGrayImage = matrix;
        width = matrixGrayImage.length;
        height = matrixGrayImage[0].length;
    }
    
    public int[][] getGrayMatrixImage() {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int color = matrixGrayImage[w][h];
                int red = (color >> 16) & 0xFF;
                int green = (color >>8) & 0xFF;
                int blue = (color) & 0xFF;
                int sum = (int)(red*0.299 + green*0.587 + blue*0.114);   
                matrixGrayImage[w][h] = sum;
            }
        } 
        return matrixGrayImage;
    }
    
    public BufferedImage getGrayImage() {
        BufferedImage grayImage = new BufferedImage(width,height,TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {            
            for (int w = 0; w < width; w++) {
                int sum = matrixGrayImage[w][h];
                int grayColor = ((255 & 0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8)  | ((sum & 0xFF) << 0);
                grayImage.setRGB(w,h,grayColor);
            }
        }
        return grayImage;
    }
    
}