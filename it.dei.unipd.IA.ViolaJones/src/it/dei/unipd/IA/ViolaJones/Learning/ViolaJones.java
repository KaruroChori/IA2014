package it.dei.unipd.IA.ViolaJones.Learning;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *  Molte cose da sistemare... 
 * @author enrico
 */
public class ViolaJones {

    private final static double MAX = 1000000000;

    ArrayList<Image> positiveTest, negativeTest, validationSet;
    ArrayList<Image> P, N;
    ArrayList<Feature> features;
    int clearedNegativeSize;
    int minNumber;

    public ViolaJones(ArrayList<Image> positiveTest, ArrayList<Image> negativeTest, int minNumber/*, ArrayList<Image> validationSet*/, ArrayList<Feature> features) {
        this.positiveTest = positiveTest;
        this.negativeTest = negativeTest;
        this.P = new ArrayList<Image>(this.positiveTest);
        this.N = new ArrayList<Image>(this.negativeTest);
        this.features = features;
        this.validationSet = validationSet;
        clearedNegativeSize = 0;
        this.minNumber = minNumber;
    }

    public Cascade buildCascade(double f, double d, double targetF, Cascade cascade) {

        double tmpF = 1.0;
        double lastF = 1.0;
        double tmpD = 1.0;
        double lastD = 1.0;
        int i = -1;
        int n;

        double[] tmpRet = new double[2];

        while (tmpF > targetF) {
            System.out.println("Ciclo esterno start iteration j-esima "+ tmpF +" > "+ targetF);
            i++;
            n = 0;
            tmpF = lastF;
            cascade.getCascade().add(new ArrayList<Feature>());

            while (tmpF > (f * lastF)) {
                System.out.println("Ciclo interno start iteration i-esima "+ tmpF +" > "+ f*lastF);
                n += 15;
                System.out.println("Avvio AdaBoost");
                cascade.getCascade().set(i, new AdaBoost().train(P, N, features, n));
                cascade.getThresholdList().add(MAX);
                decreaseThreshold(i, d * lastD, cascade);

                tmpRet = evaluateOnTest(cascade);

                tmpF = tmpRet[0];
                tmpD = tmpRet[1];
                System.out.println("Ciclo interno end iteration i-esima " + tmpF +" > "+ f*lastF);
            }
            System.out.println("Ciclo interno conluso"+ tmpF +" > "+ f*lastF + " è falsa");
            N.clear();
            lastD = tmpD;
            lastF = tmpF;
            if (tmpF > targetF) {
                evaluateOnTrainNegative(N, cascade);
                System.out.println("Reducing overfitting");
            }
            System.out.println("Ciclo esterno end iteration j-esima "+ tmpF +" > "+ targetF);
        }
        System.out.println("Ciclo esterno concluso"+ tmpF +" > "+ targetF + " è falsa.");
        return cascade;
    }

    public double[] evaluateOnTest(Cascade cascade) {
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

    boolean evaluate(Image img, Cascade cascade) {
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

    public void evaluateOnTrainNegative(ArrayList<Image> N, Cascade cascade) {
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
     * Diminuisce la soglia per l'i-esimo classificatore finchè la cascata corrente
     * ha un detection rate di almeno minD.
     * @param ith
     * @param minD
     * @param cascade
     */
    public void decreaseThreshold(int ith, double minD, Cascade cascade) {
        int corrP; 
        double tmpD; 
        double mid = 0;
        double up = cascade.getCascade().get(ith).size() * 10;
        double down = 0;
        while ((up - down) > 1e-3) {
            cascade.getThresholdList().set(ith, (down + up) / 2);
            mid = (down + up) / 2;
            //System.out.print("mid " + mid);
            corrP = 0;
            for (int i = 0; i < positiveTest.size(); i++) {
                if (evaluate(positiveTest.get(i), cascade)) {
                    corrP++;
                }
            }
            //System.out.print(" Threshold: " + cascade.getThresholdList().get(ith));
            
            tmpD = corrP / ((double)(positiveTest.size()));
            
            if (tmpD < (minD + 1e-9)) {
                up = mid;
            } else {
                down = mid;
            }
            //System.out.println(" tmp " + tmpD + " up " + up + " down " + down);
            //System.out.println();

        }
        cascade.getThresholdList().set(ith, down);
        mid = down;
    }

}
