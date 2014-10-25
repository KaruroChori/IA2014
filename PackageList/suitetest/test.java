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
            NoiseReduction nonoise = new NoiseReduction(newimg.getGrayMatrixImage());                                         
            nonoise.getNoNoiseMatrixGrayImage();
            newimg.getGrayMatrixImage(); 
            ImageIO.write(newimg.getGrayImage(),"JPG",new File("gray.jpg"));            
            ImageIO.write(nonoise.getNoNoiseGrayImage(),"JPG",new File("norumorgray.jpg"));
            //ImageIO.write(nonoise.getNoNoiseColorImage(),"JPG",new File("norumorcolor.jpg"));
        }
        catch (IOException e) {
            System.out.println("Something wrong while loading/saving file");        
        }	        
    }  
}
