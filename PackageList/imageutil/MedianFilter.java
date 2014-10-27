package imageutil;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.floor;
import java.util.Arrays;

public class MedianFilter {
    
    private int[][] matrixGrayImage;
    private int width, height;
    private int[][] filteredMatrixGrayImage;
    
    public MedianFilter(int[][] img) {
        matrixGrayImage = img;
        width = img.length;
        height = img[0].length;
        filteredMatrixGrayImage = new int[width][height];
    }
    
    //FA UN BUON LAVORO CON UNA FILTRO DI DIMENSIONI 9x9 MA VA SOPRA IL SECONDO E MEZZO,
    //PERCHÈ BISOGNA MANTENERE ORDINATO UN ARRAY
    public int[][] getFilteredMatrix(int dimension) {
        int[] window = new int[dimension*dimension];
        int edgex = (int)Math.floor(dimension/2);
        int edgey = (int)Math.floor(dimension/2);
        //long start = System.nanoTime();
        for (int x = edgex; x < width - edgex; x++) {
            for (int y = edgey; y < height - edgey; y++) {
                int i = 0;
                for (int fx = 0; fx < dimension; fx++) {
                    for (int fy = 0; fy < dimension; fy++) {
                        window[i] = matrixGrayImage[x+fx-edgex][y+fy-edgey];
                        i = i+1;
                    }                
                }                
                Arrays.sort(window);
                filteredMatrixGrayImage[x][y] = window[dimension*dimension/2];
            }         
        }
        //long end = System.nanoTime();
        //System.out.println((end-start)/1000000000.0);
        return filteredMatrixGrayImage;
    }
    
    public BufferedImage getFilteredImage() {
        BufferedImage filteredImage = new BufferedImage(width,height,TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {            
            for (int w = 0; w < width; w++) {
                int sum = filteredMatrixGrayImage[w][h];
                int grayColor = ((0xFF) << 24) | ((sum & 0xFF) << 16) | ((sum & 0xFF) << 8)  | ((sum & 0xFF) << 0);
                filteredImage.setRGB(w,h,grayColor);
            }
        }
        return filteredImage;
    }
    
}
