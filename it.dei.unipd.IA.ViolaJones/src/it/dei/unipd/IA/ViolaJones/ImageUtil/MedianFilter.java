package it.dei.unipd.IA.ViolaJones.ImageUtil;

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
        getFilteredMatrix(3);
    }


    /*
     *  This Quickselect routine is based on the algorithm described in
     *  "Numerical recipes in C", Second Edition,
     *  Cambridge University Press, 1992, Section 8.5, ISBN 0-521-43108-5
     *  This code by Nicolas Devillard - 1998. Public domain.
     *  Java version by karurochari
     */

    public int quick_select(int arr[], int n){
        int low, high ;
        int median;
        int middle, ll, hh;

        low = 0 ; high = n-1 ; median = (low + high) / 2;
        for (;;) {
            if (high <= low) /* One element only */
                return arr[median] ;

            if (high == low + 1) {  /* Two elements only */
                if (arr[low] > arr[high]){int t=arr[low];arr[low]=arr[high];arr[high]=t;}
                return arr[median] ;
            }

        /* Find median of low, middle and high items; swap into position low */
        middle = (low + high) / 2;
        if (arr[middle] > arr[high])    {int t=arr[middle];arr[middle]=arr[high];arr[high]=t;}
        if (arr[low] > arr[high])       {int t=arr[low];arr[low]=arr[high];arr[high]=t;}
        if (arr[middle] > arr[low])     {int t=arr[middle];arr[middle]=arr[low];arr[low]=t;}

        /* Swap low item (now in position middle) into position (low+1) */
        {int t=arr[middle];arr[middle]=arr[low+1];arr[low+1]=t;}

        /* Nibble from each end towards middle, swapping items when stuck */
        ll = low + 1;
        hh = high;
        for (;;) {
            do ll++; while (arr[low] > arr[ll]) ;
            do hh--; while (arr[hh]  > arr[low]) ;

            if (hh < ll)
            break;

	    {int t=arr[ll];arr[ll]=arr[hh];arr[hh]=t;}
        }

        /* Swap middle item (in position low) back into correct position */
        {int t=arr[low];arr[low]=arr[hh];arr[hh]=t;}

        /* Re-set active partition */
        if (hh <= median)
            low = ll;
            if (hh >= median)
            high = hh - 1;
        }
    }
    
    public int[][] getFilteredMatrix(int dimension) {
        int[] window = new int[dimension*dimension];
        int[] window2 = new int[dimension*dimension];

        int edgex = (int)Math.floor(dimension/2);
        int edgey = (int)Math.floor(dimension/2);
        long start = System.nanoTime();
	int r=0;

        for (int x = edgex; x < width - edgex; x++) {
            for (int y = edgey; y < height - edgey; y++) {
		        if(y==edgey){
			        int i=0;
			        for (int fx = 0; fx < dimension; fx++){
		                	for (int fy = 0; fy < dimension; fy++){
					        window2[i] = window[i] = matrixGrayImage[x+fx-edgex][y+fy-edgey];
					        i++;
				        }        
			        }
			        r=0;
		        }

		        else{
			        int i=0;
			        for (int fx = 0; fx < dimension; fx++){
				        window2[i+r%dimension]= matrixGrayImage[x+fx-edgex][y+dimension-1-edgey];
				        i+=dimension;      
			        } 
                		System.arraycopy(window2,0,window,0,dimension*dimension);  
			        r++;   
		        }
		        filteredMatrixGrayImage[x][y] = quick_select(window,dimension*dimension);
            }         
        }
        long end = System.nanoTime();
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
