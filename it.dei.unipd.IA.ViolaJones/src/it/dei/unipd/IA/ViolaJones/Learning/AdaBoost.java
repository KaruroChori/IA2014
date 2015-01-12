package it.dei.unipd.IA.ViolaJones.Learning;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Questa classe implementa la variante di AdaBoost citata nel paper di
 * ViolaJones.
 *
 * @author enrico
 */
public class AdaBoost {

    /**
     * Questo metdo esegue la selezione delle feature secondo la tecinca di
     * machine learning nota sotto il nome di AdaBoost.
     *
     * @param positive
     * @param negative
     * @param features
     * @param T
     * @return
     */
    public ArrayList<Feature> train(ArrayList<Image> positive, ArrayList<Image> negative,
            ArrayList<Feature> features, int T) {

        int brNegative = Math.min(positive.size(), negative.size());

        ArrayList<Feature> solution = new ArrayList<Feature>();

        //ArrayList<Boolean> validFeature = new ArrayList<Boolean>();

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
         * Creo una matrice di FeatureValues, dove ogni cella è il valore di una 
         * feature in una determinata immagini. La matrice avrà quindi dimensioni 
         * pari a 132.336*cardianlità(dataset).
         */
        ArrayList<ArrayList<FeatureValue>> featureValues = new ArrayList<ArrayList<FeatureValue>>();
        for (int i = 0; i < features.size(); i++) {
            ArrayList<FeatureValue> thisFeaturesValuesForAllImages = new ArrayList<FeatureValue>();
            Boolean broken = false;
            for (int j = 0; j < positive.size(); j++) {
                int val = positive.get(j).evaluateFeature(features.get(i));
                /*if (val == -Integer.MAX_VALUE / 2) {
                    broken = true;
                    System.out.println("Broken");
                }*/
                thisFeaturesValuesForAllImages.add(new FeatureValue(j, val, true));
            }
            for (int j = 0; j < brNegative; j++) {
                int val = negative.get(j).evaluateFeature(features.get(i));
                /*if (val == -Integer.MAX_VALUE / 2) {
                    broken = true;
                    System.out.println("Broken");

                }*/
                thisFeaturesValuesForAllImages.add(new FeatureValue(j, val, false));
            }
            /*if (broken == true) {
                for (int z = 0; z < thisFeaturesValuesForAllImages.size(); z++) {
                    thisFeaturesValuesForAllImages.get(i).setValue((int) Math.random() % 100);
                }
            }*/
            //validFeature.add(!broken);
            Collections.sort(thisFeaturesValuesForAllImages);
            //System.out.println(i+"     sorting done! \n");
            featureValues.add(thisFeaturesValuesForAllImages);
        }

        /*
         * Avvio un ciclo con T iterazioni, dove T è il numero di feature che voglio
         * selezionare.
         */
        for (int i = 0; i < T; i++) {
            normalizeWeights(positiveWeights, negativeWeights);
            int bestFeature = -1, polarity = 0;
            float error = 1e9f, threshold = 0.f;
            for (int j = 0; j < featureValues.size(); j++) {
                /*if (validFeature.get(j) == false) {
                    continue;
                }*/
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
                    if (curErr < error) {
                        //System.out.println(curErr +  "  < " + error + "?");
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
                        //System.out.println("Selezionata la featureValue " +j);
                    }
                }
            }
            if (error != 0) {
                float beta = error / (1 - error);
                float alpha = (float) Math.log(1.0f / beta);

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
                solution.get(solution.size() - 1).setWeight(1e5f);
                solution.get(solution.size() - 1).setPolarity(polarity);
                //System.out.println(i + " = " + (T-1) + "l'errore deve essere zero alla fine delle operazioni.");
                break;
            }
            //System.out.println("Feature " + bestFeature + " scelta e aggiunta!");
        }
        return solution;
    }

    /**
     * Calcola i valori parziali e totali delle somme dei pesi, al fine di poter
     * eseguire la normalizzazione dei pesi.
     *
     * @param weightsPositive
     * @param weightsNegative
     */
    private void normalizeWeights(float[] weightsPositive, float[] weightsNegative) {
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

    /**
     * Compie la somma di tutti i pesi all'interno dell'array di input.
     *
     * @param weights
     * @return float summedWeights
     */
    private float sumWeights(float[] weights) {
        float summedWeights = 0;
        for (int i = 0; i < weights.length; i++) {
            summedWeights += weights[i];
        }
        return summedWeights;
    }

}
