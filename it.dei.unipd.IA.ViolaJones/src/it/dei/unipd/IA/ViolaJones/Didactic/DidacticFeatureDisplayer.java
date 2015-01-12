package it.dei.unipd.IA.ViolaJones.Didactic;

import it.dei.unipd.IA.ViolaJones.ImageUtil.GrayImage;
import it.dei.unipd.IA.ViolaJones.ImageUtil.ImageToMatrix;
import it.dei.unipd.IA.ViolaJones.ImageUtil.IntegralImage;
import it.dei.unipd.IA.ViolaJones.ImageUtil.NormalizeImage;
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
import it.dei.unipd.IA.ViolaJones.Detector.Stage;
import it.dei.unipd.IA.ViolaJones.Detector.Tree;

/**
 * Questa classe ha puro scopo dimostrativo e didattico.Essa infatti permette di 
 * vedere il risultato dell'algoritmo su una data immagine.Oltre a questo mostra
 * anche tutte le feature valutate dall'algoritmo senza badare al reale ordine cronologico 
 * in cui sono state valutate, ma più che altro vorrebbe sottolineare come la maggior densità 
 * di feature valutate si ha su quelle zone che poi effettivamente contengono un 
 * volto.Tuttavia questa classe non è sufficiente a scopo didattico e per questo va accompagnata 
 * con una seconda classe che permette di visualizzare il procedimento cronoligico (e quindi non per densità)
 * delle varie fasi dell'agoritmo.
 */

public class DidacticFeatureDisplayer extends JFrame {

    private Detector detector;
    private ArrayList<Rectangle> allPossibleRectangle;
    private ArrayList<Integer> sizeChangePosition;
    private ArrayList<Rectangle> rectangleList;
    private ArrayList<Rectangle> rectangleUnitedList;
    private ArrayList<Feature> featureList;
    private ArrayList<Tree> trees;
    private ArrayList<Stage> stages;
    private BufferedImage image;
    private Rectangle rect;
    private Feature feature;
    private Rectangle featurePart1;
    private Rectangle featurePart2;
    private Rectangle featurePart3;

    public DidacticFeatureDisplayer(File img, String XMLFile) {
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
            Logger.getLogger(DidacticFeatureDisplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        IntegralImage intImg = new IntegralImage(nrm.getNormalizeMatrixIMage());
        intImg.getMatrixIntegralImage();
        detector.findFaces(intImg.getMatrixIntegralImageGray(),
                intImg.getSquaredMatrixIntegralImageGray(), 1.2f,1.1f,.05f, 2);
        allPossibleRectangle = detector.getAllPossibleRect();
        sizeChangePosition = detector.getsizeChangePosition();
        rectangleList = detector.getRectangleList();
        rectangleUnitedList = detector.getRectangleUnitedList();
        stages = detector.getStages();
        setContentPane(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(image.getWidth(), image.getHeight());
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        DidacticFeatureDisplayer dd = new DidacticFeatureDisplayer(new File("img3.jpg"), "haarcascade_frontalface_default.xml");
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
        for (int s = 0; s < stages.size(); s++) {
            Stage stage = detector.getStages().get(s);
            trees = stage.getTreesList();
            for (int t = 0; t < trees.size(); t++) {
                featureList = trees.get(t).getFeaturesList();
                for (int f = 0; f < featureList.size(); f++) {
                    feature = featureList.get(f);
                    if (feature.getScaledFeature()[0] != null) {
                        featurePart1 = feature.getScaledFeature()[0].convert();
                    } else {
                        featurePart1 = null;
                    }
                    if (feature.getScaledFeature()[1] != null) {
                        featurePart2 = feature.getScaledFeature()[1].convert();
                    } else {
                        featurePart2 = null;
                    }
                    if (feature.getScaledFeature()[2] != null) {
                        featurePart3 = feature.getScaledFeature()[2].convert();
                    } else {
                        featurePart3 = null;
                    }
                    getContentPane().repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
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
            Dimension dimension = getSize();
            g1.clearRect(0, 0, dimension.width, dimension.height);
            double scale_x = dimension.width * 1.f / img_width;
            double scale_y = dimension.height * 1.f / img_height;
            double scale = Math.min(scale_x, scale_y);
            int imageX = (dimension.width - (int) (img_width * scale)) / 2;
            int imageY = (dimension.height - (int) (img_height * scale)) / 2;
            g1.drawImage(image, imageX, imageY, (int) (img_width * scale), (int) (img_height * scale), null);
            /*int w = (int) (rect.width * scale);
             int h = (int) (rect.height * scale);
             int x = (int) (rect.x * scale) + imageX;
             int y = (int) (rect.y * scale) + imageY;
             g1.drawRect(x, y, w, h);*/
            /*for (int i = 0; i < rectangleList.size(); i++) {
                int wf = (int) (rectangleList.get(i).width * scale);
                int hf = (int) (rectangleList.get(i).height * scale);
                int xf = (int) (rectangleList.get(i).x * scale) + imageX;
                int yf = (int) (rectangleList.get(i).y * scale) + imageY;
                g1.setColor(Color.blue);
                g1.drawRect(xf, yf, wf, hf);
            }*/
            for (int i = 0; i < rectangleUnitedList.size(); i++) {
                int wf = (int) (rectangleUnitedList.get(i).width * scale);
                int hf = (int) (rectangleUnitedList.get(i).height * scale);
                int xf = (int) (rectangleUnitedList.get(i).x * scale) + imageX;
                int yf = (int) (rectangleUnitedList.get(i).y * scale) + imageY;
                g1.setColor(Color.orange);
                g1.drawRect(xf, yf, wf, hf);
            }
            if (featurePart1 != null) {
                int w1 = (int) (featurePart1.width);
                int h1 = (int) (featurePart1.height);
                int x1 = (int) (featurePart1.x);
                int y1 = (int) (featurePart1.y);
                g1.setColor(Color.white);
                g1.fillRect(x1, y1, w1, h1);
            }
            if (featurePart2 != null) {
                int w2 = (int) (featurePart2.width);
                int h2 = (int) (featurePart2.height);
                int x2 = (int) (featurePart2.x);
                int y2 = (int) (featurePart2.y);
                g1.setColor(Color.black);
                g1.fillRect(x2, y2, w2, h2);
            }
            if (featurePart3 != null) {
                int w3 = (int) (featurePart3.width);
                int h3 = (int) (featurePart3.height);
                int x3 = (int) (featurePart3.x);
                int y3 = (int) (featurePart3.y);
                g1.setColor(Color.black);
                g1.fillRect(x3, y3, w3, h3);
            }

        }

    }

}
