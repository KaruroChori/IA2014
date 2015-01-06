package it.dei.unipd.IA.ViolaJones.Learning;

import java.io.*;
import java.awt.*;
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

    public test() {
        gf = new GenerateFeature();
        DrawPane d = new DrawPane();
        setContentPane(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        test dd = new test();
        dd.render();
    }

    public void render() {
        rect3 = null;
        for (int i = 0; i < gf.getList().size(); i++) {
            for (int z = 0; z < gf.getList().get(i).getSize(); z = z + 3) {
                rect = gf.getList().get(i).getRectangle(z).convert();
                rect2 = gf.getList().get(i).getRectangle(z + 1).convert();
                if (gf.getList().get(i).getSize() == 3) {
                    rect3 = gf.getList().get(i).getRectangle(z + 2).convert();
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
