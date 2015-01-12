package it.dei.unipd.IA.ViolaJones.Learning;

/**
 * Questa è una classe di supporto e temporanea, semplicemente conserva tre
 * valori necessari durante AdaBoost, successivamente questi valori saranno
 * trasferiti in un oggetto di tipo Feature.
 */
public class FeatureValue implements Comparable<FeatureValue> {

    /*
     * La variabile index non è altro che quella variabile che individua l'immagine 
     * sulla quale è stata calcolata la feature.
     */
    private int index;
    /*
     * La variabile value altro non è che il valore della feature stessa 
     */
    private int value;
    /*
     * La variabile isPositive indica se il sample su cui è calcolata la 
     * feature è un volto o meno.
     */
    private boolean isPositive;

    /**
     * Costruttore che inizializza le variabili della classe, creando un oggetto
     * di tipo FeatureValue che possiede un indice, una valore e un flag
     * riguardante la positività o negatività dell'esempio.
     *
     * @param index
     * @param value
     * @param isPositive
     */
    public FeatureValue(int index, int value, boolean isPositive) {
        this.index = index;
        this.isPositive = isPositive;
        this.value = value;
    }

    /**
     * Metodo di accesso al valore della FeatureValue.
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Metodo di accesso che permette di settare il valore della FeatureValue in
     * un momento successivo alla sua creazione.
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Metodo di accesso all'indice dell'immagine corrispondente a questa
     * featureValue.
     *
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Metodo di accesso che permette di settare l'indice dell'immagine
     * corrispondente a questa featureValue in un momento successivo alla sua
     * creazione.
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Metodo che permette di sapere se questa featureValue è associata ad un
     * sample positivo o negativo.
     *
     * @return isPositive
     */
    public boolean isPositive() {
        return isPositive;
    }

    /**
     * Metodo di accesso che permette di settare il tipo (negativo o positivo)
     * del sample corrispondente a questa featureValue in un momento successivo
     * alla sua creazione
     *
     * @param isPositive
     */
    public void setType(boolean isPositive) {
        this.isPositive = isPositive;
    }

    /**
     * Metodo necessario per rendere due oggetti di tipo FeatureValue
     * confrontabili.
     *
     * @param fv1
     * @param fv2
     * @return v1 - v2
     */
    public int compare(FeatureValue fv1, FeatureValue fv2) {
        int v1 = fv1.getValue();
        int v2 = fv2.getValue();
        return v1 - v2;
    }

    /**
     * Metodo necessario per confrontare la corrente FeatureValue con un altra.
     *
     * @param fv1
     * @return compare(this,fv1I
     */
    @Override
    public int compareTo(FeatureValue fv1) {
        return compare(this, fv1);
    }

}
