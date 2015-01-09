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

    ArrayList<Image> positive, negative, validationSet;
    ArrayList<Feature> features;
    int clearedNegativeSize;
    int minNumber;

    public ViolaJones(ArrayList<Image> positive, ArrayList<Image> negative, int minNumber/*, ArrayList<Image> validationSet, ArrayList<Feature> features*/) {
        this.positive = positive;
        this.negative = negative;
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

        while (tmpF < targetF) {
            i++;
            n = 0;
            tmpF = lastF;
            cascade.getCascade().add(new ArrayList<Feature>());

            while (tmpF > f * lastF) {
                n += 15;
                cascade.getCascade().set(i, new AdaBoost().train(positive, negative, features, n));
                cascade.getThreshold().add(MAX);
                decreaseThreshold(i, d * lastD, cascade);

                tmpRet = evaluateOnTest(cascade);

                tmpF = tmpRet[0];
                tmpD = tmpRet[1];
            }
            negative.clear();
            lastD = tmpD;
            lastF = tmpF;
            if (tmpF > targetF) {
                evaluateOnTrainNegative(negative, cascade);
            }
        }
        return cascade;
    }

    public double[] evaluateOnTest(Cascade cascade) {
        int corrP = 0, errN = 0;
        double[] pair = new double[2];

        for (int i = 0; i < positive.size(); i++) {
            if (evaluate(positive.get(i), cascade)) {
                corrP++;
            }
        }

        for (int i = 0; i < negative.size(); i++) {
            if (evaluate(negative.get(i), cascade)) {
                errN++;
            }
        }
        pair[0] = errN / (double) (negative.size() + clearedNegativeSize);
        pair[1] = corrP / (double) (positive.size());
        return pair;

    }

    boolean evaluate(Image img, Cascade cascade) {
        double sum = 0;
        for (int k, j, i = 0; i < cascade.getCascade().size(); i++) {
            sum = 0;
            for (j = 0; j < cascade.getCascade().get(i).size(); j++) {
                sum += img.evaluateTrainedFeature(cascade.getCascade().get(i).get(j));
            }

            if (sum < cascade.getThreshold().get(i)) {
                return false;
            }
        }
        return true;
    }

    public void evaluateOnTrainNegative(ArrayList<Image> N, Cascade cascade) {
        for (int i = 0; i < negative.size(); i++) {
            if (evaluate(negative.get(i), cascade)) {
                N.add(negative.get(i));
            } else {
                //negative.remove(i);
                clearedNegativeSize++;
            }
        }

        negative = N;
        if (negative.size() < minNumber) {
            ArrayList<Image> tmp = new ArrayList<Image>();
            for (int i = 0; i < test.ld; i++) {
                String source = String.format("face" + "%04d", i + 1);
                File img = new File("faces/" + source + ".gif");
                try {
                    BufferedImage image = ImageIO.read(img);
                    this.negative.add(new Image(image, 1));
                } catch (IOException e) {
                    System.out.println("Error" + source);
                }
            }
            if (tmp.size() == 0) {
                return;
            }

            for (int i = 0; i < tmp.size(); i++) {
                this.negative.add(tmp.get(i));
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

        double mid, up = cascade.getCascade().get(ith).size() * 10;
        double down = 0;
        while (up - down > 1e-3) {
            cascade.getThreshold().set(ith, (down + up) / 2);
            mid = cascade.getThreshold().get(ith);
            corrP = 0;
            for (int i = 0; i < positive.size(); i++) {
                if (evaluate(positive.get(i), cascade)) {
                    corrP++;
                }
            }

            tmpD = corrP / (double) positive.size();

            if (tmpD < minD + 1e-9) {
                up = mid;
            } else {
                down = mid;
            }

        }
        cascade.getThreshold().set(ith, down);
        mid = cascade.getThreshold().get(ith);
    }

}
