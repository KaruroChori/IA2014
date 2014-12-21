package suitetest;

import imageutil.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class test {

    public static void main (String[] args) throws IOException {
        try {
            
            BufferedImage img = ImageIO.read(new File("img.jpg"));
            ImageToMatrix tmp = new ImageToMatrix(img);
            GrayImage newimg = new GrayImage(tmp.getMatrix());
            GrayScaleImageEqualization equalized = new GrayScaleImageEqualization(newimg.getGrayMatrixImage());
            MedianFilter filtered = new MedianFilter(equalized.getMatrixEqualized());
            NormalizeImage normalized= new NormalizeImage(newimg.getGrayMatrixImage());
            int dimension = 5;            
            NoiseReduction nonoise = new NoiseReduction(filtered.getFilteredMatrix(dimension));
            nonoise.getNoNoiseMatrixGrayImage();
            normalized.ApplyFilter(1);
            ImageIO.write(normalized.getFilteredImage(),"JPG",new File("normalized.jpg"));
            ImageIO.write(newimg.getGrayImage(),"JPG",new File("gray.jpg"));
            ImageIO.write(equalized.getImageEqualized(),"JPG",new File("equalized.jpg"));
            ImageIO.write(filtered.getFilteredImage(),"JPG",new File("filtered"+dimension+".jpg"));
            ImageIO.write(nonoise.getNoNoiseGrayImage(),"JPG",new File("nonoisegray.jpg"));
        }
        catch (IOException e) {
            System.out.println("Something wrong while loading/saving file");        
        }	        
    }  
}
