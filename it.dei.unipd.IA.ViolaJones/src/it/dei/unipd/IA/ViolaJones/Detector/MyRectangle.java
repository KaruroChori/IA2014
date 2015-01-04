package it.dei.unipd.IA.ViolaJones.Detector;

import java.awt.Rectangle;

public class MyRectangle {

    /*
     * Coordinate spigoli rettangolo.
     */
    protected int x, y, width, height;
    protected float weight;

    public MyRectangle(int x, int y, int width, int height, float weight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    /**
     * Crea un rettangolo x partire da una stringa.
     *
     * @param text
     * @return
     */
    public static MyRectangle convert(String text) {
        String[] parameters = text.split(" ");
        int x = Integer.parseInt(parameters[0]);
        int y = Integer.parseInt(parameters[1]);
        int width = Integer.parseInt(parameters[2]);
        int height = Integer.parseInt(parameters[3]);
        Float weight = Float.parseFloat(parameters[4]);
        return new MyRectangle(x, y, width, height, weight);
    }
    
    public Rectangle convert() {
        return new Rectangle(x,y,width,height);
    }
}
