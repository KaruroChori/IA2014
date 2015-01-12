package it.dei.unipd.IA.ViolaJones.Learning;

import java.util.ArrayList;

/**
 * Classe che rappresenta una cascata di classificatori.E' caratterizzata da più
 * livelli, ogni livello ha una serie di feature, ogni livello ha una certa
 * soglia, che se superata permette l'accesso al livello successivo
 *
 * @author enrico
 */
public class Cascade {

    /*
     * Matrice contente la cascata di weak classifier.
     */
    private ArrayList<ArrayList<Feature>> cascade;
    /*
     * Lista contenten le soglie per ogni livello.
     */
    private ArrayList<Double> thresholdList;

    /**
     * Costruttore della classe Cascade, inizializza le variabili di classe,
     * creando la struttura contenitiva della cascata.
     */
    public Cascade() {
        cascade = new ArrayList<ArrayList<Feature>>();
        thresholdList = new ArrayList<Double>();
    }

    /**
     * Metodo di accesso alla cascata di weak classifier.
     *
     * @return cascade
     */
    public ArrayList<ArrayList<Feature>> getCascade() {
        return cascade;
    }

    /**
     * Metodo di accesso che permette di settare l'intera cascata di weak
     * classifier.
     *
     * @param cascade
     */
    public void setCascade(ArrayList<ArrayList<Feature>> cascade) {
        this.cascade = cascade;
    }

    /**
     * Metodo di accesso alla lista delle soglie di ogni livello della cascata.
     *
     * @return thresholdList
     */
    public ArrayList<Double> getThresholdList() {
        return thresholdList;
    }

    /**
     * Metodo di accesso che permette di settare la lista di soglie per la
     * cascata.
     *
     * @param thresholdList
     */
    public void setThreshold(ArrayList<Double> thresholdList) {
        this.thresholdList = thresholdList;
    }

}
