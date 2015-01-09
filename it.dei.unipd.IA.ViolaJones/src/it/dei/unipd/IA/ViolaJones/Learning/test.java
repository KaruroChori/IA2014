package it.dei.unipd.IA.ViolaJones.Learning;


import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Questa classe ha puro scopo dimostrativo e didattico.Essa infatti permette di
 * vedere il risultato dell'algoritmo su una data immagine.Oltre a questo mostra
 * anche tutte le feature valutate dall'algoritmo senza badare al reale ordine
 * cronologico in cui sono state valutate, ma più che altro vorrebbe
 * sottolineare come la maggior densità di feature valutate si ha su quelle zone
 * che poi effettivamente contengono un volto.Tuttavia questa classe non è
 * sufficiente a scopo didattico e per questo va accompagnata con una seconda
 * classe che permette di visualizzare il procedimento cronoligico (e quindi non
 * per densità) delle varie fasi dell'agoritmo.
 */
public class test extends JFrame {

    private Rectangle rect;
    private Rectangle rect2;
    private Rectangle rect3;
    private GenerateFeature gf;
    private ArrayList<WeakClassifier> allWeakClassifier;
    private ArrayList<Feature> features;
    public static int ld;

    public test(int lp, int ln) {
        ld = ln;
        gf = new GenerateFeature();
        System.out.println("Feature create!\n\n\n");
        features = gf.getList();
	System.out.println(features.size());
        ArrayList<Image> positiveImage = new ArrayList<Image>();
        ArrayList<Image> negativeImage = new ArrayList<Image>();
        for (int i = 0; i < lp; i++) {
            String source = String.format("face" + "%04d", i+1);            
            File img = new File("faces/"+source+".gif");
            try {
                BufferedImage image = ImageIO.read(img);
                positiveImage.add(new Image(image, 1));
            } catch (IOException e) {
                System.out.println("Error"+source);
            }
        }
        for (int i = 0; i < ln; i++) {
            String source = String.format("nonface" + "%04d", i+1);
            File img = new File("nonfaces/"+source+".gif");
            try {
                BufferedImage image = ImageIO.read(img);
                negativeImage.add(new Image(image, - 1));
            } catch (IOException e) {
                System.out.println("Error"+source);
            }
        }
        System.out.println("Immagini processate!!\n\n\n");
        Cascade cs = new Cascade();
        ViolaJones boost = new ViolaJones(positiveImage, negativeImage, 1000);
        Cascade cs1 = boost.buildCascade(0.005, 0.999, 0.00001, cs);
        ArrayList<ArrayList<Feature>> cascata = cs1.getCascade();
        for (int i = 0; i < cascata.size(); i++) {
            for (int j = 0; j < cascata.get(i).size(); j++) {
                features.add(cascata.get(i).get(j));
            }
        }        
        System.out.println("Training finito!\n\n\n");
        DrawPane d = new DrawPane();
        setContentPane(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        test dd = new test(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        dd.render();
    }

    public void render() {
        rect3 = null;
        for (int i = 0; i < features.size(); i++) {
            for (int z = 0; z < features.get(i).getSize(); z = z + 3) {
                rect = features.get(i).getRectangle(z).convert();
                rect2 = features.get(i).getRectangle(z + 1).convert();
                if (features.get(i).getSize() == 3) {
                    rect3 = features.get(i).getRectangle(z + 2).convert();
                }
                getContentPane().repaint();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    class DrawPane extends JPanel {

        public DrawPane() {
            super();

        }

        @Override
        public void paintComponent(Graphics g) {
            g.clearRect(0, 0, getWidth(), getHeight());
            Graphics2D g1 = (Graphics2D) g;
            g1.setStroke(new BasicStroke(2f));
            Dimension dimension = getSize();
            g1.clearRect(0, 0, dimension.width, dimension.height);
            if (rect != null) {
                int w = (int) (rect.width);
                int h = (int) (rect.height);
                int x = (int) (rect.x);
                int y = (int) (rect.y);
                g1.setColor(Color.yellow);
                g1.drawRect(x, y, w, h);
            }
            if (rect2 != null) {
                int w1 = (int) (rect2.width);
                int h1 = (int) (rect2.height);
                int x1 = (int) (rect2.x);
                int y1 = (int) (rect2.y);
                g1.setColor(Color.orange);
                g1.drawRect(x1, y1, w1, h1);
            }
            if (rect3 != null) {
                int w2 = (int) (rect3.width);
                int h2 = (int) (rect3.height);
                int x2 = (int) (rect3.x);
                int y2 = (int) (rect3.y);
                g1.setColor(Color.orange);
                g1.drawRect(x2, y2, w2, h2);
            }
        }

    }

}
