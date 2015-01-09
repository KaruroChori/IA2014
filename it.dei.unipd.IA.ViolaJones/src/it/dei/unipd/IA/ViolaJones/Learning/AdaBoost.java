package it.dei.unipd.IA.ViolaJones.Learning;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author enrico
 */
public class AdaBoost {

    public AdaBoost() {

    }

    public ArrayList<Feature> train(ArrayList<Image> positive, ArrayList<Image> negative,
            ArrayList<Feature> features, int T) {
        
        int brNegative = Math.min(positive.size(), negative.size());
        
        ArrayList<Feature> solution = new ArrayList<Feature>();
        
        ArrayList<Boolean> validFeature = new ArrayList<Boolean>();
        
        /*
         * Inizializzo i due array che conterrano i pesi delle singole immagini.
         */
        float[] positiveWeights = new float[positive.size()];
        float[] negativeWeights = new float[negative.size()];
        
        /*
         * Definisco i pesi iniziali delle immagini secondo quanto riportato nel paper di
         * Viola e Jones.
         */
        float initialPositiveWeight = 0.5f / positiveWeights.length;
        float initialNegativeWeight = 0.5f / negativeWeights.length;

        /*
         * Popolo i due array con i pesi iniziali.
         */
        for (int i = 0; i < positiveWeights.length; ++i) {
            positiveWeights[i] = initialPositiveWeight;
        }
        for (int i = 0; i < negativeWeights.length; ++i) {
            negativeWeights[i] = initialNegativeWeight;
        }
        
        
        /*
         * Questa è un alternativa, funziona, ma è da perfezionare, comunque l'idea 
         * non è male, anzi. In pratica faccio quanto consigliato da enoch.
         */
        
        
        /*ArrayList<ArrayList<FeatureValue>> featureValues = new ArrayList<ArrayList<FeatureValue>>();
        ArrayList<FeatureValue> thisFeaturesValuesForAllImages = new ArrayList<FeatureValue>();
        Boolean broken = false;
        try {
            System.setOut(new PrintStream(new File("output-file.txt")));
        } catch (Exception e) {
            System.exit(0);
        }
        String s = "";
        for (int j = 0; j < positive.size(); j++) {
            for (int n = 0; n < features.size(); n++) {
                int val = positive.get(j).applyFeature(features.get(n));
                /*if (val == -Short.MAX_VALUE / 2) {
                    broken = true;
                    System.out.println("Broken");
                }
                if (broken == true) {
                    for (int z = 0; z < thisFeaturesValuesForAllImages.size(); z++) {
                        thisFeaturesValuesForAllImages.get(n).setValue((int) Math.random() % 100);
                    }
                }/*
                //thisFeaturesValuesForAllImages.add(new FeatureValue(j, val, true));
                s += "" + j + "-" +  val + "-true\n";
                //System.out.println(j + "-" +  val + "-true");
                if (j%100 == 0 && n == 162335) {
                    System.out.print(s);
                    s = "";
                }
            }
        }
        System.exit(0);

        
        for (int j = 0; j < negative.size(); j++) {
            for (int n = 0; n < features.size(); n++) {
                int val = negative.get(j).applyFeature(features.get(n));
                if (val == -Short.MAX_VALUE / 2) {
                    broken = true;
                    System.out.println("Broken");
                }
                if (broken == true) {
                    for (int z = 0; z < thisFeaturesValuesForAllImages.size(); z++) {
                        thisFeaturesValuesForAllImages.get(n).setValue((int) Math.random() % 100);
                    }
                }
                thisFeaturesValuesForAllImages.add(new FeatureValue(j, val, false));
                System.out.println(j + " " + n+"\n");
            }
        }*/
        
        
        
        
        
        /*
         * Creo una matrice di FeatureValues, dove ogni cella è il valore di una 
         * feature in una determinata immagini. La matrice avrà quindi dimensioni 
         * pari a 132.336*cardianlità(dataset).
         */
        ArrayList<ArrayList<FeatureValue>>  featureValues = new ArrayList<ArrayList<FeatureValue>>();
        for (int i = 0; i < features.size(); i++) {
            ArrayList<FeatureValue> thisFeaturesValuesForAllImages = new ArrayList<FeatureValue>();
            Boolean broken = false;
            for (int j = 0; j < positive.size(); j++) {
                int val = positive.get(j).applyFeature(features.get(i));
                if (val == -Short.MAX_VALUE/2) {
                    broken = true;
                    System.out.println("Broken");
                }
                thisFeaturesValuesForAllImages.add(new FeatureValue(j, val, true));
                System.out.println(""+j+"-"+val+"-true");
            }
            for (int j = 0; j < brNegative; j++) {
                int val = negative.get(j).applyFeature(features.get(i));
                if (val == -Short.MAX_VALUE/2) {
                    broken = true;
                    System.out.println("Broken");

                }
                thisFeaturesValuesForAllImages.add(new FeatureValue(j, val, false));
                System.out.println(""+j+"-"+val+"-false");
            }
            if ( broken == true ) {
                for(int z=0; z<thisFeaturesValuesForAllImages.size(); z++) {
                     thisFeaturesValuesForAllImages.get(i).setValue((int)Math.random() % 100);        
                }                                           
            }
            validFeature.add(!broken);
            System.out.println(i+"\n");
            Collections.sort(thisFeaturesValuesForAllImages);
            System.out.println(i+"sorting done! \n");
            featureValues.add(thisFeaturesValuesForAllImages);
        }
        
        for (int i = 0; i < T; i++) {
            normalizeWeights(positiveWeights, negativeWeights);
            int bestFeature = -1, polarity = 0;
            float error = 1e9f, threshold = 0.f;
            for (int j = 0; j < featureValues.size(); j++) {
                if (validFeature.get(j) == false) {
                    continue;
                }
                float[] total = new float[2];
                total[0] = sumWeights(positiveWeights);
                total[1] = sumWeights(negativeWeights);
                float[] curr = {0.f, 0.f};
                for (int k = 0; k < featureValues.get(j).size(); k++) {
                    int index = featureValues.get(j).get(k).getIndex();
                    int value = featureValues.get(j).get(k).getValue();

                    float val1 = curr[0] + (total[1] - curr[1]);
                    float val2 = curr[1] + (total[0] - curr[0]);

                    float curErr = Math.min(val1, val2);

                    if (featureValues.get(j).get(k).isPositive() == true) {
                        curr[0] += positiveWeights[index];
                    } else {
                        curr[1] += negativeWeights[index];
                    }

                    if (k != 0 && featureValues.get(j).get(k - 1).getValue() == featureValues.get(j).get(k).getValue()) {
                        continue;
                    }
		    //System.out.println(error +  "   " + curErr);
                    if (curErr < error) {
                        if (k == 0) {
                            threshold = featureValues.get(j).get(k).getValue();
                        } else {
                            threshold = (featureValues.get(j).get(k - 1).getValue() + featureValues.get(j).get(k).getValue()) / 2.f;
                        }

                        if (val1 < val2) {
                            polarity = -1;
                        } else {
                            polarity = 1;
                        }
                        error = (float) curErr;
                        bestFeature = j;
                    }
                }
            }
            if (error != 0) {
                float beta = error / (1 - error);
                double alpha = Math.log(1.0f / beta);

                for (int k = 0; k < featureValues.get(bestFeature).size(); k++) {
                    int val = featureValues.get(bestFeature).get(k).getValue();
                    int index = featureValues.get(bestFeature).get(k).getIndex();
                    boolean correct = featureValues.get(bestFeature).get(k).isPositive();
                    if (correct == (polarity * val < polarity * threshold)) {
                        if (correct == true) {
                            positiveWeights[index] *= beta;
                        } else {
                            negativeWeights[index] *= beta;
                        }
                    }
                }

                solution.add(features.get(bestFeature));
                solution.get(solution.size() - 1).setThreshold(threshold);
                solution.get(solution.size() - 1).setWeight(alpha);
                solution.get(solution.size() - 1).setPolarity(polarity);

            } else {
                solution.add(features.get(bestFeature));
                solution.get(solution.size() - 1).setThreshold(threshold);
                solution.get(solution.size() - 1).setWeight(1e5);
                solution.get(solution.size() - 1).setPolarity(polarity);
                System.out.println(i + " = " + (T-1) + "l'errore deve essere zero alla fine delle operazioni.");
                break;
            }
            System.out.println("Feature scelta e aggiunta!");
        }
       
        return solution;
    }

    public void normalizeWeights(float[] weightsPositive, float[] weightsNegative) {
        float sum = 0.f;
        for (int i = 0; i < weightsPositive.length; i++) {
            sum += weightsPositive[i];
        }
        for (int i = 0; i < weightsNegative.length; i++) {
            sum += weightsNegative[i];
        }

        for (int i = 0; i < weightsPositive.length; i++) {
            weightsPositive[i] /= sum;
        }
        for (int i = 0; i < weightsNegative.length; i++) {
            weightsNegative[i] /= sum;
        }
    }

    public float sumWeights(float[] weights) {
        float summedWeights = 0;
        for (int i = 0; i < weights.length; i++) {
            summedWeights += weights[i];
        }
        return summedWeights;
    }

}