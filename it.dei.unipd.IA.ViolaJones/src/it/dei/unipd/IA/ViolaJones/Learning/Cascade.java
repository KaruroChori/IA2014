package it.dei.unipd.IA.ViolaJones.Learning;

import java.util.ArrayList;

/**
 *
 * @author enrico
 */
public class Cascade {
    
    private ArrayList<ArrayList<Feature>> cascade;
    private ArrayList<Double> threshold;
    
    public Cascade() {
        cascade = new ArrayList<ArrayList<Feature>>();
        threshold = new ArrayList<Double>();
    }
    
    public ArrayList<ArrayList<Feature>> getCascade() {
        return cascade;
    }
     
    public void setCascade(ArrayList<ArrayList<Feature>> cascade) {
        this.cascade = cascade;
    }
    
    public ArrayList<Double> getThreshold() {
        return threshold;
    }
    
    public void setThreshold(ArrayList<Double> threshold) {
        this.threshold = threshold;
    }
    
    public void saveCascade(String nameFile) {
        //TODO
    }
    
    public void loadCascade(String nameFile) {
        //TODO
    }
    
}
