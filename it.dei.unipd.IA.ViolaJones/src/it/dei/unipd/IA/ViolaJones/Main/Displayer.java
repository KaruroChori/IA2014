package it.dei.unipd.IA.ViolaJones.Main;

import id.dei.unipd.IA.ViolaJones.ImageUtil.GrayImage;
import id.dei.unipd.IA.ViolaJones.ImageUtil.ImageToMatrix;
import id.dei.unipd.IA.ViolaJones.ImageUtil.IntegralImage;
import id.dei.unipd.IA.ViolaJones.ImageUtil.NormalizeImage;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import it.dei.unipd.IA.ViolaJones.Detector.Detector;
import it.dei.unipd.IA.ViolaJones.Detector.Feature;

public class Displayer extends JFrame {

    private Detector detector;
    private ArrayList<Rectangle> allPossibleRectangle;
    private ArrayList<Integer> sizeChangePosition;
    private ArrayList<Rectangle> rectangleList;
    private ArrayList<Rectangle> rectangleUnitedList;
    private ArrayList<Feature> featureList;
    private BufferedImage image;
    private Rectangle rect;
    private Feature feature;
    private Rectangle feature1;
    private Rectangle feature2;
    private Rectangle feature3;

    public Displayer(File img, String XMLFile) {
        image = null;
        try {
            image = ImageIO.read(img);
            /*BufferedImage originalImage = ImageIO.read(img);
             int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
             BufferedImage resizeImageJpg = resizeImage(originalImage, BufferedImage.TYPE_INT_RGB, 800, 600);
             ImageIO.write(resizeImageJpg, "jpg", img);
             image = ImageIO.read(img);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        DrawPane d = new DrawPane();
        detector = new Detector(XMLFile);
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
        rectangleList = detector.getRectangleList();
        rectangleUnitedList = detector.getRectangleUnitedList();
        featureList = detector.getFeature();
        System.out.println("Size" + featureList.size());
        setContentPane(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(image.getWidth(), image.getHeight());
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Displayer dd = new Displayer(new File("img.jpg"), "haarcascade_frontalface_alt2.xml");
        dd.render();
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    public void render() {

        for (int i = 0; i < allPossibleRectangle.size(); i++) {
            rect = allPossibleRectangle.get(i);
            try {
                if (i == sizeChangePosition.get(0)) {
                    sizeChangePosition.remove(0);
                    Thread.sleep(5000);
                    System.out.println("Cambio delle dimensioni della finestra di ricerca.");
                } else {
                    Thread.sleep(1);
                }
                for (int j = 0; j < featureList.size(); j++) {
                    feature = featureList.get(j);
                    feature1 = feature.getFeature()[0].convert();
                    feature2 = feature.getFeature()[1].convert();
                    if (feature.getFeature()[2] != null) {
                        feature3 = feature.getFeature()[2].convert();
                    }
                    else {
                        feature3 = null;
                    }
                    getContentPane().repaint();
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
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

        @Override
        public void paintComponent(Graphics g) {
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
            if (feature1 != null) {
                int w1 = (int) (feature1.width);
                int h1 = (int) (feature1.height);
                int x1 = (int) (feature1.x) + x;
                int y1 = (int) (feature1.y) + y;
                g1.setColor(Color.white);
                g1.fillRect(x1, y1, w1, h1);
            }
            if (feature2 != null) {
                int w2 = (int) (feature2.width);
                int h2 = (int) (feature2.height);
                int x2 = (int) (feature2.x)+ x;
                int y2 = (int) (feature2.y)+ y;
                g1.setColor(Color.black);
                g1.fillRect(x2, y2, w2, h2);
            }
            if (feature3 != null) {
                int w3 = (int) (feature3.width);
                int h3 = (int) (feature3.height);
                int x3 = (int) (feature3.x)+ x;
                int y3 = (int) (feature3.y)+ y;
                g1.setColor(Color.black);
                g1.fillRect(x3, y3, w3, h3);
            }
        }

    }

}
