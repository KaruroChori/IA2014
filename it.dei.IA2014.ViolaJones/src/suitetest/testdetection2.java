package suitetest;

import imageutil.GrayImage;
import imageutil.ImageToMatrix;
import imageutil.IntegralImage;
import imageutil.NormalizeImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import violajones.Rilevatore;
import violajones.Disegno;

public class testdetection2 extends JFrame {
    

    public testdetection2(File img, String XMLFile) {
        /* Load image */
        BufferedImage image = null;
        try {
            image = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Disegno d = new Disegno(image);
        Rilevatore detector = Rilevatore.create(XMLFile);
        ImageToMatrix mtxImg = new ImageToMatrix(image);
        GrayImage  grImg = new GrayImage(mtxImg.getMatrix());
        NormalizeImage nrm = new NormalizeImage(grImg.getGrayMatrixImage());
        nrm.ApplyFilter(3);
        try {
            ImageIO.write(nrm.getFilteredImage(),"JPG",new File("normalized.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(testdetection2.class.getName()).log(Level.SEVERE, null, ex);
        }
        IntegralImage intImg = new IntegralImage(nrm.getNormalizeMatrixIMage());
        intImg.getMatrixIntegralImage();
        List<Rectangle> nonunificato = detector.trovaVolti(intImg.getMatrixIntegralImageGray(), intImg.getSquaredMatrixIntegralImageGray(), 1.435f,1.2f,.10f, 2);
        List<Rectangle> res = detector.unifico(nonunificato, 2);
        System.out.println(res.size() + " faces found!");
        d.setRects(nonunificato, res);
        setContentPane(d);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                // Exit the application
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        new testdetection2(new File("img.jpg"), "haarcascade_frontalface_alt2.xml").setVisible(true);
    }

}

