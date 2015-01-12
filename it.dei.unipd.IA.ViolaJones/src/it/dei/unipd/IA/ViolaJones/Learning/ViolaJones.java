package it.dei.unipd.IA.ViolaJones.Learning;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Questa classe si occupa di eseguire l'algoritmo di ViolaJones, generando la
 * cascata di weak classifier selezionati iterativamente mediante AdaBoost
 */
public class ViolaJones {

    /*
     * Valore iniziale della soglia del livello della cascata.
     */
    private final static double MAX = 1000000000;
    /*
     * Le liste che contengono i set di sample (positivo e negativo) necessari 
     * durante il training.
     */
    private ArrayList<Image> positiveTest, negativeTest, P, N;
    /*
     * Lista di tutte le feature date in pasto all'algoritmo di training
     */
    private ArrayList<Feature> features;
    /*
     * Tiene conto delle immagine negative eliminate durante la gestione dello
     * overfitting.
     */
    private int clearedNegativeSize;
    /*
     * Indica il minimo numero di immagini prima che siano ricaricate durante 
     * la fase di riduzione dell'overfitting.
     */
    private int minNumber;

    /**
     * Inizializza la classe che esgue il training settando tutte le varibili.
     *
     * @param positiveTest
     * @param negativeTest
     * @param minNumber
     * @param features
     */
    public ViolaJones(ArrayList<Image> positiveTest, ArrayList<Image> negativeTest,
            int minNumber, ArrayList<Feature> features) {
        this.positiveTest = positiveTest;
        this.negativeTest = negativeTest;
        this.P = new ArrayList<Image>(this.positiveTest);
        this.N = new ArrayList<Image>(this.negativeTest);
        this.features = features;
        clearedNegativeSize = 0;
        this.minNumber = minNumber;
    }

    /**
     * Costruisce un cascata di weak classifier che rispetti i requisiti di
     * input.
     *
     * @param maximumFalsePositiveRateForLayer
     * @param minimumDetectionRateForLayer
     * @param overallFalsePositiveRate
     * @param cascade
     * @return
     */
    public Cascade buildCascade(double maximumFalsePositiveRateForLayer,
            double minimumDetectionRateForLayer, double overallFalsePositiveRate, Cascade cascade) {

        double tmpF = 1.0;
        double lastF = 1.0;
        double tmpD = 1.0;
        double lastD = 1.0;
        int i = -1;
        int n;

        double[] tmpRet = new double[2];

        while (tmpF > overallFalsePositiveRate) {
            System.out.println("Ciclo esterno start iteration j-esima "+ tmpF 
                    +" > "+ overallFalsePositiveRate);
            i++;
            n = 0;
            tmpF = lastF;
            cascade.getCascade().add(new ArrayList<Feature>());

            while (tmpF > (maximumFalsePositiveRateForLayer * lastF)) {
                System.out.println("Ciclo interno start iteration i-esima "+ tmpF 
                        +" > "+ maximumFalsePositiveRateForLayer*lastF);
                n += 15;
                System.out.println("Avvio AdaBoost");
                cascade.getCascade().set(i, new AdaBoost().train(P, N, features, n));
                cascade.getThresholdList().add(MAX);
                double tmpThreshold = decreaseThreshold(i, minimumDetectionRateForLayer * lastD, cascade);
                System.out.println("Threshold diminuita " + tmpThreshold);
                tmpRet = evaluateOnTest(cascade);

                tmpF = tmpRet[0];
                tmpD = tmpRet[1];
                System.out.println("Ciclo interno end iteration i-esima " + tmpF 
                        +" > "+ maximumFalsePositiveRateForLayer*lastF);
            }
            System.out.println("Ciclo interno conluso"+ tmpF +" > "
                    + maximumFalsePositiveRateForLayer*lastF + " è falsa");
            N.clear();
            lastD = tmpD;
            lastF = tmpF;
            if (tmpF > overallFalsePositiveRate) {
                evaluateOnTrainNegative(N, cascade);
                System.out.println("Reducing overfitting");
            }
            System.out.println("Ciclo esterno end iteration j-esima "+ tmpF +" > "
                    + overallFalsePositiveRate);
        }
        System.out.println("Ciclo esterno concluso"+ tmpF +" > "
                + overallFalsePositiveRate + " è falsa.");
        return cascade;
    }

    /**
     * Valuta la cascata sul set di immagini positive e neagative.
     *
     * @param cascade
     * @return double[]
     */
    private double[] evaluateOnTest(Cascade cascade) {
        int corrP = 0, errN = 0;
        double[] pair = new double[2];

        for (int i = 0; i < positiveTest.size(); i++) {
            if (evaluate(positiveTest.get(i), cascade)) {
                corrP++;
                //System.out.println("cp " + corrP);
            }
            //System.out.println("positive " + i);

        }

        for (int i = 0; i < negativeTest.size(); i++) {
            if (evaluate(negativeTest.get(i), cascade)) {
                errN++;
                //System.out.println("en " + errN);
            }
            //System.out.println("negative " + i);
        }
        //System.out.println(clearedNegativeSize);
        //System.out.println(negativeTest.size() + "<--n & p-->" + positiveTest.size());
        //System.out.println("cp " + corrP + "  en " + errN);
        pair[0] = errN / (double) (negativeTest.size() + clearedNegativeSize);
        //System.out.println(pair[0]);
        pair[1] = corrP / (double) (positiveTest.size());
        //System.out.println(pair[1]);
        //System.out.println("Pair calcolato!");
        return pair;

    }

    /**
     * Valuta la cascata di weak classfier su un immagine precisa.
     *
     * @param img
     * @param cascade
     */
    private boolean evaluate(Image img, Cascade cascade) {
        double sum = 0;
        //System.out.println(cascade.getCascade().size());
        for (int k, j, i = 0; i < cascade.getCascade().size(); i++) {
            sum = 0;
            for (j = 0; j < cascade.getCascade().get(i).size(); j++) {
                sum += img.evaluateTrainedFeature(cascade.getCascade().get(i).get(j));
            }
            //System.out.println("Somma: " + sum + " Threshold: " + cascade.getThresholdList().get(i) + " isPositive " + img.getType());
            //System.out.println();

            if (sum < cascade.getThresholdList().get(i)) {
                //System.out.println("return false");
                return false;
            }
        }
        //System.out.println("return true");
        return true;
    }

    /**
     * Si occupa della riduzione dell'overfitting, valutando le immagine
     * negative e se necessario ricaricandole.
     *
     * @param N
     * @param cascade
     */
    private void evaluateOnTrainNegative(ArrayList<Image> N, Cascade cascade) {
        for (int i = 0; i < negativeTest.size(); i++) {
            if (evaluate(negativeTest.get(i), cascade)) {
                N.add(negativeTest.get(i));
            } else {
                negativeTest.set(i, null);
                clearedNegativeSize++;
            }
        }
        negativeTest = N;
        //System.out.println(N.size() + "<--N & negativeTest-->" + negativeTest.size());
        if (negativeTest.size() < minNumber) {
            //System.out.println("reloading image");
            ArrayList<Image> tmp = new ArrayList<Image>();
            for (int i = 0; i < test.ld; i++) {
                String source = String.format("nonface" + "%04d", i + 1);
                File img = new File("nonfaces/" + source + ".gif");
                try {
                    BufferedImage image = ImageIO.read(img);
                    this.negativeTest.add(new Image(image, -1));
                } catch (IOException e) {
                    //System.out.println("Error" + source);
                }
            }
            if (tmp.size() == 0) {
                return;
            }

            for (int i = 0; i < tmp.size(); i++) {
                this.negativeTest.add(tmp.get(i));
            }
            N.clear();
            evaluateOnTrainNegative(N, cascade);
        }
    }

    /**
     * Diminuisce la soglia per l'i-esimo classificatore finchè la cascata
     * corrente ha un detection rate di almeno minD.
     *
     * @param ith
     * @param minD
     * @param cascade
     */
    
    private double decreaseThreshold(int ith, double minD, Cascade cascade) {
        int corrP;
        double tmpD;
        double mid;
        double up = cascade.getCascade().get(ith).size() * 10;
        double down = 0;
        while ((up - down) > 1e-3) {
            cascade.getThresholdList().set(ith, (down + up) / 2);
            mid = (down + up) / 2;
            //System.out.println("mid " + mid);
            corrP = 0;
            for (int i = 0; i < positiveTest.size(); i++) {
                if (evaluate(positiveTest.get(i), cascade)) {
                    corrP++;
                }
            }
            //System.out.println("coorP " + corrP);
            //System.out.println("Threshold: " + cascade.getThresholdList().get(ith));

            tmpD = corrP / ((double) (positiveTest.size()));

            if (tmpD < (minD + 1e-9)) {
                up = mid;
            } else {
                down = mid;
            }
            //System.out.println("tmp " + tmpD + " up " + up + " down " + down);
            //System.out.println();

        }
        cascade.getThresholdList().set(ith, down);
        mid = down;
        return down;
    }

}
