package it.dei.unipd.IA.ViolaJones.ImageUtil;

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
    
    //Sicuri dell'ordinamento h,w ? ENRICO FIXED

    public void getMatrixIntegralImage() {
        for(int w = 0; w < width; w++) {
            int sumOfColumn=0;
            int sumOfColumn2=0;
            for(int h =0; h < height; h++) {
                int sum = grayMatrixImage[w][h];
                matrixIntegralImageGray[w][h] = (w > 0 ? matrixIntegralImageGray[w-1][h] : 0) + sumOfColumn + sum;
                squaredMatrixIntegralImageGray[w][h] = (w > 0 ? squaredMatrixIntegralImageGray[w-1][h] : 0) + sumOfColumn2 + sum*sum;
                sumOfColumn += sum;
                sumOfColumn2 += sum*sum;
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
