package it.dei.unipd.IA.ViolaJones.Learning;

import java.util.Comparator;

/**
 *
 * @author enrico
 */
public class FeatureValue implements Comparable<FeatureValue>{
    
    private int index;
    private int value; 
    private boolean isPositive;
    
    public FeatureValue(int index, int value, boolean isPositive) {
        this.index = index;
        this.isPositive = isPositive;
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public int compare(FeatureValue fv1, FeatureValue fv2) {
        int v1 = fv1.getValue();
        int v2 = fv2.getValue();
        return v1 - v2;
    }

    @Override
    public int compareTo(FeatureValue fv1) {
        return compare(this, fv1);
    }
    
    public int getIndex() {
        return index;
    }
    
    public boolean isPositive() {
        return isPositive;
    }
    

    
}
