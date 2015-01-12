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
     * Costruisce una feature che ha come parametro una soglia, inizializza
     * inoltre le variabili di necessarie a descrivere la classe.
     *
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

    /**
     * Metodo d'accesso alla lista di rettangoli che descrive la feature.
     *
     * @return MyRectangle[] rectangles
     */
    public MyRectangle[] getRectanglesDescribingFeature() {
        return rectangles;
    }

    /**
     * Metodo d'accesso che setta la lista di rettangoli che descrive la
     * feature.
     *
     * @param rectangles
     */
    public void setRectanglesList(MyRectangle[] rectangles) {
        numberOfRectangles = rectangles.length;
        this.rectangles = rectangles;
    }

    /**
     * Metodo cheritorna il numero di rettangoli che descrivono la feature e
     * quindi permette di sapere a quale dei tre tipi principali appartiene la
     * feature.
     *
     * @return int numberOfRectangles
     */
    public int getSize() {
        return numberOfRectangles;
    }

    /**
     * Metodo che resituisce il rettangolo in posizione index nella lista di
     * rettangoli che descrive la feature.
     *
     * @param index
     *
     * @return MyRectangle rectangles[index]
     */
    public MyRectangle getRectangle(int index) throws ArrayIndexOutOfBoundsException {
        return rectangles[index];
    }

    /**
     * Metodo di accesso alla soglia della feature.
     *
     * @return float threshold
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * Metodo di accesso che permette di settare la soglia della feature.
     *
     * @param threshold
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    /**
     * Metodo di accesso al peso della feature.
     *
     * @return float weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Metodo di accesso che permette di settare il peso della feature.
     *
     * @param weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Metodo di accesso alla polarità, relativa al senso della disequazione per
     * la sua valutazione della feature.
     *
     * @return float weight
     */
    public int getPolarity() {
        return polarity;
    }

    /**
     * Metodo di accesso che permette di settare la polarità della feature,
     * relativa al senso della disequazione per la sua valutazione della
     * feature.
     *
     * @param polarity
     */
    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

}
