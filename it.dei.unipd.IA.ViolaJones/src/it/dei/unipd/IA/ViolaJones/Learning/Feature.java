package it.dei.unipd.IA.ViolaJones.Learning;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Questa classe rappresenta una feature utilizzata dall'algoritmo di
 * ViolaJones, la feature viene ricostruita grazie alle informazioni fornite nel
 * file .xml.
 */
public class Feature {

    MyRectangle[] rectangles;
    MyRectangle[] scaledRectangles;
    private int numberOfRectangles;
    private float thresholdValue;
    private double weightValue;
    private int p;
    float leftValue;
    float rightValue;
    private Point size;
    int leftNode;
    int rightNode;
    boolean leftValuePresent;
    boolean rightValuePresent;

    /**
     * Una feature è posta ad ogni nodo di un albero, è caratterizzata da una
     * threshold, e ritorna il successivo (nodo sinistro o destro) in cui
     * entrare in base al valore calcolato e al valore di soglia, la feature è
     * caratterizzata da un rettangolo pesato e il valore di soglia è la somma
     * dei pixel all'interno di ogni rectangle, pesati con il peso del
     * rectangle.
     *
     * @param threshold
     * @param leftValue
     * @param rightValue
     * @param leftNode
     * @param rigthNode
     * @param rightValuePresent
     * @param leftValuePresent
     * @param size
     */
    public Feature(float threshold, float leftValue, float rightValue, int leftNode, int rigthNode, boolean leftValuePresent, boolean rightValuePresent, Point size) {
        numberOfRectangles = 0;
        rectangles = new MyRectangle[3];
        scaledRectangles = new MyRectangle[3];
        thresholdValue = threshold;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.leftNode = leftNode;
        this.rightNode = rigthNode;
        this.leftValuePresent = leftValuePresent;
        this.rightValuePresent = rightValuePresent;
        this.size = size;
    }

    public int chooseRightOrLeftNode(int[][] grayIntegralImage, int[][] squaredGrayIntegralImage, int x, int y, float scale) {
        /*
         * Scala il punto di coordinate x e y
         */
        int w = (int) (scale * size.x);
        int h = (int) (scale * size.y);
        /*
         * Calcola l'inverso dell'area della finestra di coordinate w*h
         */
        double inverseArea = 1. / (w * h);
        /*
         * Sfruttando l'immagine integrale e la sua forma quadratica calcola l'area e 
         * l'area quadratica della finestra,
         * senza compiere iterazioni.
         */
        int sum = grayIntegralImage[x + w][y + h] + grayIntegralImage[x][y] - grayIntegralImage[x][y + h] - grayIntegralImage[x + w][y];
        int powSum2 = squaredGrayIntegralImage[x + w][y + h] + squaredGrayIntegralImage[x][y] - squaredGrayIntegralImage[x][y + h] - squaredGrayIntegralImage[x + w][y];
        /* 
         * Calcolo la media e la deviazione standard della finestra
         */
        double mean = sum * inverseArea;
        double standardDeviation = powSum2 * inverseArea - mean * mean;
        standardDeviation = (standardDeviation > 1) ? Math.sqrt(standardDeviation) : 1;
        int rectanglesSum = 0;
        for (int n = 0; n < numberOfRectangles; n++) {
            MyRectangle rectangle = rectangles[n];
            /*
             * Scala il rettangolo in base alla grandezza della finestra.
             */
            int rectangleX1 = x + (int) (scale * rectangle.x);
            int rectangleX2 = x + (int) (scale * (rectangle.x + rectangle.width));
            int rectangleY1 = y + (int) (scale * rectangle.y);
            int rectangleY2 = y + (int) (scale * (rectangle.y + rectangle.height));
            MyRectangle scaledRectangle = new MyRectangle(rectangleX1, rectangleY1,
                    Math.round(scale * rectangle.width), Math.round(scale * rectangle.height), rectangle.weight);
            scaledRectangles[n] = scaledRectangle;
            /*
             * Calcolo la somma dei pixel nel rettangolo (pesata con il peso del rettangolo).
             */
            rectanglesSum += (int) ((grayIntegralImage[rectangleX1][rectangleY1]
                    + grayIntegralImage[rectangleX2][rectangleY2]
                    - grayIntegralImage[rectangleX1][rectangleY2]
                    - grayIntegralImage[rectangleX2][rectangleY1])
                    * rectangle.weight);
        }
        double rectangleSumMultipliesInverseArea = rectanglesSum * inverseArea;
        /*
         * Scelgo il nodo a sinistra o a destra comparando il valore soglia con 
         * il valore rectangleSumMultipliesInverseArea.
         */
        return (rectangleSumMultipliesInverseArea < thresholdValue * standardDeviation) ? Tree.LEFT : Tree.RIGHT;
    }

    /**
     * Aggiunge un rettangolo all'array che li contiene, incrementando il numero
     * di rettangoli presenti.
     *
     * @param rectangle
     */
    public void addRectangle(MyRectangle rectangle) {
        rectangles[numberOfRectangles++] = rectangle;
    }

    public MyRectangle[] getFeature() {
        return rectangles;
    }

    public MyRectangle[] getScaledFeature() {
        return scaledRectangles;
    }
    
    public void setRectanglesList(MyRectangle[] rectangles) {
        numberOfRectangles = rectangles.length;
        this.rectangles = rectangles;
    }
    
    public int getSize() {
        return numberOfRectangles;
    }

    public MyRectangle getRectangle(int z) throws ArrayIndexOutOfBoundsException {
        return rectangles[z];
    }
    
    public float getThreshold() {
        return thresholdValue;
    }
    
    public void setThreshold(float thresholdValue) {
        this.thresholdValue = thresholdValue;
    }
    
    public double getWeight() {
        return weightValue;
    }
    
    public void setWeight(double weightValue) {
        this.weightValue = weightValue;
    }
    
     public int getP() {
        return p;
    }
    
    public void setP(int p) {
        this.p = p;
    }
}

