package it.dei.unipd.IA.ViolaJones.Learning;

import java.awt.Rectangle;

/**
 * Un semplice rettangolo, esattamente come è definito in java.awt.Rectangle,
 * con la sola differenza che ha meno metodi e possiede un peso specifico,
 * necessario per il nostro algoritmo.
 */
public class MyRectangle {

    /*
     * Coordinate e caratteritiche del rettangolo.
     */
    protected int x, y, width, height;
    /*
     * Peso (e tipo) del rettangolo.
     */
    protected float weight;

    /**
     * Semplice costruttore che inizializza tutte le variabili necessarie a
     * descrivere un oggetto di tipo MyRectangle.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param weight
     */
    public MyRectangle(int x, int y, int width, int height, float weight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    /**
     * Crea un rettangolo a partire da una stringa.
     *
     * @param rectangleStringDefinition
     * @return
     */
    public static MyRectangle convert(String rectangleStringDefinition) {
        String[] parameters = rectangleStringDefinition.split(" ");
        int x = Integer.parseInt(parameters[0]);
        int y = Integer.parseInt(parameters[1]);
        int width = Integer.parseInt(parameters[2]);
        int height = Integer.parseInt(parameters[3]);
        Float weight = Float.parseFloat(parameters[4]);
        return new MyRectangle(x, y, width, height, weight);
    }

    /**
     * Questo metodo converte un oggetto MyRectangle in un classico rettangolo
     * fornito dalla classe Rectangle dell libreria awt di java.
     *
     * @return Rectangle(x,y,width,height)
     */
    public Rectangle convert() {
        return new Rectangle(x, y, width, height);
    }
}
