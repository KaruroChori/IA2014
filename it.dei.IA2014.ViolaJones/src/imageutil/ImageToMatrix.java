package imageutil;

import java.awt.image.BufferedImage;

public class ImageToMatrix {
    
    private BufferedImage image;
    private int[][] matrixImage;
    private int width, height;
            
    
    public ImageToMatrix(BufferedImage img) {
        image = img;
        width = img.getWidth();
        height = img.getHeight();
        matrixImage = new int[width][height];
    }
    
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