package it.dei.unipd.IA.ViolaJones.Learning;

/**
 * @author enrico
 */
public class WeakClassifier {
   
    
    private int polarity;
    private float threshold;
    private Feature feature;
    
    public WeakClassifier(Feature feature, int polarity, float threshold) {
        this.polarity = polarity;
        this.feature = feature;
        this.threshold = threshold;
    }
    
    public float getThreshold() {
        return threshold;
    }
    
    public void setThreshold(float thresholdValue) {
        this.threshold = threshold;
    }
    
    public int getPolarity() {
        return polarity;
    }
    
    public void setPolarity(int polarity) {
       this.polarity = polarity;
    }
    
    public void setFeature(Feature feature) {
        this.feature = feature;
    }
    
    public Feature getFeature() {
        return feature;
    }
}
