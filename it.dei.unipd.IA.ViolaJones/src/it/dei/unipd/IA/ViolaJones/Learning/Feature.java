package it.dei.unipd.IA.ViolaJones.Learning;

/**
 * Questa classe rappresenta una feature utilizzata dall'algoritmo di
 * ViolaJones.
 *
 * @author enrico
 */
public class Feature {

    MyRectangle[] rectangles;
    private int numberOfRectangles;
    private float threshold;
    private float weight;
    private int polarity;

    /**
     * @param threshold
     */
    public Feature(float threshold) {
        numberOfRectangles = 0;
        rectangles = new MyRectangle[3];
        this.threshold = threshold;
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

    public MyRectangle[] getRectanglesDescribingFeature() {
        return rectangles;
    }

    public void setRectanglesList(MyRectangle[] rectangles) {
        numberOfRectangles = rectangles.length;
        this.rectangles = rectangles;
    }

    public int getSize() {
        return numberOfRectangles;
    }

    public MyRectangle getRectangle(int index) throws ArrayIndexOutOfBoundsException {
        return rectangles[index];
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

}
