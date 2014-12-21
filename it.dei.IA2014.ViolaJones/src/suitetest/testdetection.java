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

public class testdetection extends JFrame {
    

    public testdetection(File img, String XMLFile) {
        /* Load image */
        BufferedImage image = null;
        try {
            image = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Dessin d = new Dessin(image);
        Rilevatore detector = Rilevatore.create(XMLFile);
        ImageToMatrix mtxImg = new ImageToMatrix(image);
        GrayImage  grImg = new GrayImage(mtxImg.getMatrix());
        NormalizeImage nrm = new NormalizeImage(grImg.getGrayMatrixImage());
        nrm.ApplyFilter(3);
        try {
            ImageIO.write(nrm.getFilteredImage(),"JPG",new File("normalized.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(testdetection.class.getName()).log(Level.SEVERE, null, ex);
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
        new testdetection(new File("img.jpg"), "haarcascade_frontalface_alt2.xml").setVisible(true);
    }

}

class Dessin extends JPanel {

    protected Image img;
    int img_width, img_height;
    List<Rectangle> res;
    List<Rectangle> nonunificati;

    public Dessin(Image img) {
        super();
        this.img = img;
        img_width = img.getWidth(null);
        img_height = img.getHeight(null);
        res = null;
        nonunificati = null;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g1 = (Graphics2D) g;
        g1.setColor(Color.red);
        g1.setStroke(new BasicStroke(2f));
        if (img == null) {
            return;
        }
        Dimension dim = getSize();
        g1.clearRect(0, 0, dim.width, dim.height);
        double scale_x = dim.width * 1.f / img_width;
        double scale_y = dim.height * 1.f / img_height;
        double scale = Math.min(scale_x, scale_y);
        int x_img = (dim.width - (int) (img_width * scale)) / 2;
        int y_img = (dim.height - (int) (img_height * scale)) / 2;
        g1.drawImage(img, x_img, y_img, (int) (img_width * scale), (int) (img_height * scale), null);
        if (res == null || nonunificati == null) {
            return;
        }
        for (int i = 0; i < nonunificati.size(); i++) {
            Rectangle rect = nonunificati.get(i);
            int w = (int) (rect.width * scale);
            int h = (int) (rect.height * scale);
            int x = (int) (rect.x * scale) + x_img;
            int y = (int) (rect.y * scale) + y_img;
            g1.setColor(Color.WHITE);
            g1.drawRect(x, y, w, h);
            if (i < res.size()) {
                Rectangle rectuni = res.get(i);
                w = (int) (rectuni.width * scale);
                h = (int) (rectuni.height * scale);
                x = (int) (rectuni.x * scale) + x_img;
                y = (int) (rectuni.y * scale) + y_img;
                g1.setColor(Color.MAGENTA);
                g1.drawRect(x, y, w, h);
            }
        }

    }

    public void setRects(List<Rectangle> list, List<Rectangle> list2) {
        this.nonunificati = list;
        this.res = list2;
        repaint();
    }
}
