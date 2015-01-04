package it.dei.unipd.IA.ViolaJones.Detector;

import java.awt.Point;

public class Feature {
    
    MyRectangle[] rectangles;
    int numberOfRectangles;
    float thresholdValue;
    float leftValue;
    float rightValue;
    /*
     * Point è un oggetto che contine due coordinate x, y
    */
    Point size; 
    int leftNode;
    int rightNode;
    boolean leftValuePresent;
    boolean rightValuePresent;
    
    /*
     * Aggiunge un rectangle all'array che li contiene, incrementando il numero 
     * di rectangles presenti.
    */
    public void addRectangle(MyRectangle rectangle) {
        rectangles[numberOfRectangles++] = rectangle;
    }
    
    /*
     * Una feature è posta ad ogni nodo di un albero, è caratterizzata da una threshold,
     * e ritorna il successivo (nodo sinistro o destro) in cui entrare in base al valore 
     * calcolato e al valore threshold. La feature è caratterizzata da un rectangle pesato 
     * e il valore è la somma dei pixel all'interno di ogni rectangle, 
     * pesati con il peso del rectangle.
     * 
    */
    public Feature(float threshold, float leftValue, float rightValue, int leftNode, int rigthNode, boolean leftValuePresent, boolean rightValuePresent, Point size) {
        numberOfRectangles = 0;
        rectangles = new MyRectangle[3];
        thresholdValue = threshold;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.leftNode = leftNode;
        this.rightNode = rigthNode;
        this.leftValuePresent = leftValuePresent;
        this.rightValuePresent = rightValuePresent;
        this.size = size;       
    }
    
    public int chooseRightOrLeftNode(int[][] grayIntegralImage, int[][] squaredGrayIntegralImage, int width, int height, float scale) {
        /*
         * Scala il punto di coordinate x e y
        */
        int w = (int)(scale*size.x);
        int h = (int)(scale*size.y);
        /*
         * Calcola l'inverso dell'area della finestra di coordinate w*h
        */
        double inverseArea = 1./(w*h);
        /*
         * Sfruttando l'immagine integrale e la sua forma quadratica calcola l'area e l'area quadratica della finestra,
         * senza compiere iterazioni.
        */
        int sum = grayIntegralImage[width+w][height+h] + grayIntegralImage[width][height] - grayIntegralImage[width][height+h] - grayIntegralImage[width+w][height];
        int powSum2 = squaredGrayIntegralImage[width+w][height+h] + squaredGrayIntegralImage[width][height] - squaredGrayIntegralImage[width][height+h] - squaredGrayIntegralImage[width+w][height];
        /* 
         * Calcolo la mean e la varianza della finestra
        */
        double mean = sum*inverseArea;
        double standardDeviation = powSum2*inverseArea-mean*mean;
        standardDeviation = (standardDeviation>1)? Math.sqrt(standardDeviation):1;
        int rectanglesSum = 0;
        for (int n = 0; n < numberOfRectangles; n++) {
            MyRectangle rectangle = rectangles[n];
            /*
             * Scala il rectangle in base alla grandezza della finestra.
            */
            int rectangleX = width + (int)(scale*rectangle.x);
            int rectangleY = width + (int)(scale*(rectangle.x+rectangle.width));
            int rectangleWidth = height + (int)(scale*rectangle.y);
            int rectangleHeight = height + (int)(scale*(rectangle.y+rectangle.height));
            /*
             * Calcolo la somma dei pixel nel rectangles (pesata con il peso del rectangle).
            */
            rectanglesSum += (int)((grayIntegralImage[rectangleX][rectangleWidth] + grayIntegralImage[rectangleY][rectangleHeight] 
                    - grayIntegralImage[rectangleX][rectangleHeight] - grayIntegralImage[rectangleY][rectangleWidth])*rectangle.weight);
        }
        double rectangleSumMultipliesInverseArea = rectanglesSum*inverseArea;
        /*
         * Scelgo il nodo a sinistra o a destra comparando il valore soglia con il valore rectangleSumMultipliesInverseArea.
        */
        return (rectangleSumMultipliesInverseArea < thresholdValue*standardDeviation)? Tree.LEFT : Tree.RIGHT;
    }       
    
    
    public MyRectangle[] getFeature() {
        return rectangles;
    }
}
