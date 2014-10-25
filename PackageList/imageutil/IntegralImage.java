package imageutil;

public class IntegralImage {
    
    private int[][] grayMatrixImage;
    private int width, height;
    private int[][] matrixIntegralImageGray;
    private int[][] squaredMatrixIntegralImageGray;
    
    public IntegralImage(int[][] img) {
        grayMatrixImage = img; 
        width = img.length;
        height = img[0].length;
        matrixIntegralImageGray = new int[width][height];
        squaredMatrixIntegralImageGray = new int[width][height];
    }
    
    public void getMatrixIntegralImage() {
        for(int h =0; h < height; h++) {
            int sumOfColumn=0;
            int sumOfColumn2=0;
            for(int w = 0; w < width; w++) {
                int sum = grayMatrixImage[w][h];
                matrixIntegralImageGray[h][w] = (h > 0 ? matrixIntegralImageGray[h-1][w] : 0) + sumOfColumn + sum;
                squaredMatrixIntegralImageGray[h][w] = (h>0?squaredMatrixIntegralImageGray[h-1][w]:0) + sumOfColumn2 + sum*sum;
                sumOfColumn = sumOfColumn + sum;
                sumOfColumn2 = sumOfColumn2 + sum*sum;
            }
        }
    }
    
    public int[][] getSquaredMatrixIntegralImageGray() {
        return squaredMatrixIntegralImageGray;
    }
    
    public int[][] getMatrixIntegralImageGray() {
        return matrixIntegralImageGray;
    } 
        
}
