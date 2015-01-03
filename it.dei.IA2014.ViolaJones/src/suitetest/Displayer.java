package suitetest;

import imageutil.GrayImage;
import imageutil.ImageToMatrix;
import imageutil.IntegralImage;
import imageutil.NormalizeImage;
import java.lang.*;
import java.io.*;
import java.lang.Math.*;
import java.awt.*;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import violajones.Rilevatore;

public class Displayer extends JFrame {

    private Rilevatore detector;
    private ArrayList<Rectangle> allPossibleRectangle;
    private ArrayList<Integer> sizeChangePosition;
    private ArrayList<Rectangle> rectangleList;
    private ArrayList<Rectangle> rectangleUnitedList;
    private BufferedImage image;
    private Rectangle rect;

    public Displayer(File img, String XMLFile) {
        image = null;
        try {
            image = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DrawPane d = new DrawPane();
        detector = Rilevatore.create(XMLFile);
        ImageToMatrix mtxImg = new ImageToMatrix(image);
        GrayImage grImg = new GrayImage(mtxImg.getMatrix());
        NormalizeImage nrm = new NormalizeImage(grImg.getGrayMatrixImage());
        nrm.ApplyFilter(3);
        try {
            ImageIO.write(nrm.getFilteredImage(), "JPG", new File("normalized.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(Displayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        IntegralImage intImg = new IntegralImage(nrm.getNormalizeMatrixIMage());
        intImg.getMatrixIntegralImage();
        detector.trovaVolti(intImg.getMatrixIntegralImageGray(),
                intImg.getSquaredMatrixIntegralImageGray(), 1.435f, 1.2f, .10f, 2);
        allPossibleRectangle = detector.getAllPossibleRect();
        sizeChangePosition = detector.getsizeChangePosition();
        rectangleList = detector.getlista_rettangoli();
        rectangleUnitedList = detector.getrettangoli_uniti();
        setContentPane(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(image.getWidth(), image.getHeight());
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Displayer dd = new Displayer(new File("img.jpg"), "haarcascade_frontalface_alt2.xml");
        dd.render();
    }

    public void render() {

        for (int i = 0; i < allPossibleRectangle.size(); i++) {
            rect = allPossibleRectangle.get(i);
            getContentPane().repaint();
            try {
                if (i == sizeChangePosition.get(0)) {
                    sizeChangePosition.remove(0);
                    Thread.sleep(5000);
                    System.out.println("Cambio delle dimensioni della finestra di ricerca");
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class DrawPane extends JPanel {

        int img_width, img_height;

        public DrawPane() {
            super();
            img_width = image.getWidth(null);
            img_height = image.getHeight(null);

        }

       /* @Override
        public void paint(Graphics g) {
            Graphics2D g1 = (Graphics2D) g;
            g1.setColor(Color.red);
            g1.setStroke(new BasicStroke(2f));
            if (image == null) {
                return;
            }
            Dimension dim = getSize();
            g1.clearRect(0, 0, dim.width, dim.height);
            double scale_x = dim.width * 1.f / img_width;
            double scale_y = dim.height * 1.f / img_height;
            double scale = Math.min(scale_x, scale_y);
            int x_img = (dim.width - (int) (img_width * scale)) / 2;
            int y_img = (dim.height - (int) (img_height * scale)) / 2;
            g1.drawImage(image, x_img, y_img, (int) (img_width * scale), (int) (img_height * scale), null);
        }*/

        
         @Override
         public void paintComponent(Graphics g) {
         //Pulisce tutto!
         g.clearRect(0, 0, getWidth(), getHeight());       
         Graphics2D g1 = (Graphics2D) g;
         g1.setColor(Color.red);
         g1.setStroke(new BasicStroke(2f));
         if (image == null) {
                return;
            }
            Dimension dim = getSize();
            g1.clearRect(0, 0, dim.width, dim.height);
            double scale_x = dim.width * 1.f / img_width;
            double scale_y = dim.height * 1.f / img_height;
            double scale = Math.min(scale_x, scale_y);
            int x_img = (dim.width - (int) (img_width * scale)) / 2;
            int y_img = (dim.height - (int) (img_height * scale)) / 2;
            g1.drawImage(image, x_img, y_img, (int) (img_width * scale), (int) (img_height * scale), null);
         int w = (int) (rect.width * scale);
         int h = (int) (rect.height * scale);
         int x = (int) (rect.x * scale) + x_img;
         int y = (int) (rect.y * scale) + y_img;
         g1.drawRect(x, y, w, h);
         }
    }

}
