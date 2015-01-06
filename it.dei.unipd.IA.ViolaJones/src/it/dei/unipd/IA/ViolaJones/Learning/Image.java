package it.dei.unipd.IA.ViolaJones.Learning;

import java.awt.image.BufferedImage;

/**
 *
 * @author enrico
 */
class Image {
    
    private BufferedImage image;
    private int[][] matrixImage;
    private int width, height;
    private int[][] grayIntegralImage;
    private int[][] squaredGrayIntegralImage;
    final static int WIDTH = 24;
    final static int HEIGHT = 24;
    private int rectanglesSum;
    private int isPositive;
            
    
    public Image(BufferedImage img, int isPositive) {
        image = img;
        width = img.getWidth();
        height = img.getHeight();
        matrixImage = new int[width][height];
        getMatrix();
        getGrayMatrixImage();
        rectanglesSum = 0;
        this.isPositive = isPositive;
    }
    
    private int[][] getMatrix() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int color = image.getRGB(w, h);
                matrixImage[w][h] = color;
            }
        } 
        return matrixImage;
    }
    
    private int[][] getGrayMatrixImage() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int color = matrixImage[w][h];
                int red = (color >> 16) & 0xFF;
                int green = (color >>8) & 0xFF;
                int blue = (color) & 0xFF;
                int sum = (int)(red*0.299 + green*0.587 + blue*0.114);   
                matrixImage[w][h] = sum;
            }
        } 
        return matrixImage;
    }
     
    public void getMatrixIntegralImage() {
        for(int w = 0; w < width; w++) {
            int sumOfColumn=0;
            int sumOfColumn2=0;
            for(int h =0; h < height; h++) {
                int sum = matrixImage[w][h];
                grayIntegralImage[w][h] = (w > 0 ? grayIntegralImage[w-1][h] : 0) + sumOfColumn + sum;
                squaredGrayIntegralImage[w][h] = (w > 0 ? squaredGrayIntegralImage[w-1][h] : 0) + sumOfColumn2 + sum*sum;
                sumOfColumn += sum;
                sumOfColumn2 += sum*sum;
            }
        }
    }
    
    public int[][] getSquaredMatrixIntegralImageGray() {
        return squaredGrayIntegralImage;
    }
    
    public int[][] getMatrixIntegralImageGray() {
        return grayIntegralImage;
    }
    
    public int applyFeature(Feature feature) {
        for (int n = 0; n < feature.getSize(); n++) {
            MyRectangle rectangle = feature.getRectangle(n);
            int rectangleX1 =  rectangle.x;
            int rectangleX2 = (rectangle.x + rectangle.width);
            int rectangleY1 = rectangle.y;
            int rectangleY2 = (rectangle.y + rectangle.height);
            rectanglesSum += (int) ((grayIntegralImage[rectangleX1][rectangleY1]
                    + grayIntegralImage[rectangleX2][rectangleY2]
                    - grayIntegralImage[rectangleX1][rectangleY2]
                    - grayIntegralImage[rectangleX2][rectangleY1])
                    * rectangle.weight);
        }
        return rectanglesSum;
    }
    
    public int getType() {
        return isPositive;
    }
    
    
}
