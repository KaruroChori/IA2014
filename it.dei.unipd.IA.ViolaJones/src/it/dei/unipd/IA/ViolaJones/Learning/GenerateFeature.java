package it.dei.unipd.IA.ViolaJones.Learning;

import java.util.ArrayList;

/**
 * Questa classe si occupa di generare tutte le possibili feature che possono
 * esistere in un quadrato 24x24, il loro numero è, in accordo con il paper
 * originale dell'algoritmo 162.336.
 */
public class GenerateFeature {

    /*
     * Lista che conterrà tutte le feature possibili in un quadrato di dimensioni 24x24.
     */
    final ArrayList<Feature> allPossibleFeatures;
    /*
     * Dimensioni del quadrato sul quale saranno generate le feature.
     */
    final static int FRAMESIZE = 24;

    /**
     * Costruttore che inzializza le variabili e si occupa della generazione di
     * tutte le possibili feature.
     */
    public GenerateFeature() {
        this.allPossibleFeatures = new ArrayList<Feature>();
        generateFeatureType1();
        generateFeatureType2();
        generateFeatureType3();
        generateFeatureType4();
        generateFeatureType5();
    }

    /**
     * Metodo che costruisce tutte le feature di tipo uno, cioè quelle formate
     * da due rettangoli verticali.
     */
    private void generateFeatureType1() {
        for (int h = 0; h < FRAMESIZE; h++) {
            for (int w = 0; w < FRAMESIZE; w++) {
                for (int a = 1; a < FRAMESIZE - h + 1; a = a + 1) {
                    for (int l = 2; l < FRAMESIZE - w + 1; l = l + 2) {
                        MyRectangle[] rect = new MyRectangle[2];
                        MyRectangle main = new MyRectangle(w, h, l, a, -1.f);
                        MyRectangle black = new MyRectangle(w + l / 2, h, l / 2, a, 2.f);
                        rect[0] = main;
                        rect[1] = black;
                        Feature feature = new Feature(0);
                        feature.setRectanglesList(rect);
                        //System.out.println(main.x + " " + main.y + " " + main.width + " " + main.height);
                        //System.out.println(black.x + " " + black.y + " " + black.width + " " + black.height);
                        allPossibleFeatures.add(feature);
                    }
                }
            }
        }
        System.out.println(allPossibleFeatures.size() + "\n\n");
    }

    /**
     * Metodo che costruisce tutte le feature di tipo due, cioè quelle formate
     * da due rettangoli orizzontali.
     */
    private void generateFeatureType2() {
        for (int h = 0; h < FRAMESIZE; h++) {
            for (int w = 0; w < FRAMESIZE; w++) {
                for (int a = 2; a < FRAMESIZE - h + 1; a = a + 2) {
                    for (int l = 1; l < FRAMESIZE - w + 1; l = l + 1) {
                        MyRectangle[] rect = new MyRectangle[2];
                        MyRectangle main = new MyRectangle(w, h, l, a, -1.f);
                        MyRectangle black = new MyRectangle(w, h + a / 2, l, a / 2, 2.f);
                        rect[0] = main;
                        rect[1] = black;
                        Feature feature = new Feature(0);
                        feature.setRectanglesList(rect);
                        //System.out.println(main.x + " " + main.y + " " + main.width + " " + main.height);
                        //System.out.println(black.x + " " + black.y + " " + black.width + " " + black.height);
                        allPossibleFeatures.add(feature);
                    }
                }
            }
        }
        System.out.println(allPossibleFeatures.size() + "\n\n");
    }

    /**
     * Metodo che costruisce tutte le feature di tipo tre, cioè quelle formate
     * da tre rettangoli verticali.
     */
    private void generateFeatureType3() {
        for (int h = 0; h < FRAMESIZE; h++) {
            for (int w = 0; w < FRAMESIZE; w++) {
                for (int a = 1; a < FRAMESIZE - h + 1; a = a + 1) {
                    for (int l = 3; l < FRAMESIZE - w + 1; l = l + 3) {
                        MyRectangle[] rect = new MyRectangle[2];
                        MyRectangle main = new MyRectangle(w, h, l, a, -1.f);
                        MyRectangle black = new MyRectangle(w + l / 3, h, l / 3, a, 3.f);
                        rect[0] = main;
                        rect[1] = black;
                        Feature feature = new Feature(0);
                        feature.setRectanglesList(rect);
                        //System.out.println(main.x + " " + main.y + " " + main.width + " " + main.height);
                        //System.out.println(black.x + " " + black.y + " " + black.width + " " + black.height);
                        allPossibleFeatures.add(feature);
                    }
                }
            }
        }
        System.out.println(allPossibleFeatures.size() + "\n\n");
    }

    /**
     * Metodo che costruisce tutte le feature di tipo quattro, cioè quelle
     * formate da tre rettangoli orizzontali.
     */
    private void generateFeatureType4() {
        for (int h = 0; h < FRAMESIZE; h++) {
            for (int w = 0; w < FRAMESIZE; w++) {
                for (int a = 3; a < FRAMESIZE - h + 1; a = a + 3) {
                    for (int l = 1; l < FRAMESIZE - w + 1; l = l + 1) {
                        MyRectangle[] rect = new MyRectangle[2];
                        MyRectangle main = new MyRectangle(w, h, l, a, -1.f);
                        MyRectangle black = new MyRectangle(w, h + a / 3, l, a / 3, 3.f);
                        rect[0] = main;
                        rect[1] = black;
                        Feature feature = new Feature(0);
                        feature.setRectanglesList(rect);
                        //System.out.println(main.x + " " + main.y + " " + main.width + " " + main.height);
                        //System.out.println(black.x + " " + black.y + " " + black.width + " " + black.height);
                        allPossibleFeatures.add(feature);
                    }
                }
            }
        }
        System.out.println(allPossibleFeatures.size() + "\n\n");
    }

    /**
     * Metodo che costruisce tutte le feature di tipo cinque, cioè quelle
     * formate da quattro rettangoli verticali e orizzonatali.
     */
    private void generateFeatureType5() {
        for (int h = 0; h < FRAMESIZE; h++) {
            for (int w = 0; w < FRAMESIZE; w++) {
                for (int a = 2; a < FRAMESIZE - h + 1; a = a + 2) {
                    for (int l = 2; l < FRAMESIZE - w + 1; l = l + 2) {
                        MyRectangle[] rect = new MyRectangle[3];
                        MyRectangle main = new MyRectangle(w, h, l, a, -1.f);
                        MyRectangle black = new MyRectangle(w + l / 2, h, l / 2, a / 2, 2.f);
                        MyRectangle black2 = new MyRectangle(w, h + a / 2, l / 2, a / 2, 2.f);
                        rect[0] = main;
                        rect[1] = black;
                        rect[2] = black2;
                        Feature feature = new Feature(0);
                        feature.setRectanglesList(rect);
                        //System.out.println(main.x + " " + main.y + " " + main.width + " " + main.height);
                        //System.out.println(black.x + " " + black.y + " " + black.width + " " + black.height);
                        //System.out.println(black2.x + " " + black2.y + " " + black2.width + " " + black2.height);
                        allPossibleFeatures.add(feature);
                    }
                }
            }
        }
        System.out.println(allPossibleFeatures.size() + "\n\n");
    }

    /**
     * Metodo di accesso alla lista di tutte le feature.
     *
     * @return allaPossibleFeatures
     */
    public ArrayList<Feature> getList() {
        return allPossibleFeatures;
    }

    /**
     * Metodo di accesso al numero di feature create.
     *
     * @return numberOfPossibleFeatures
     */
    public int getNumberOfFeatures() {
        return allPossibleFeatures.size();
    }

}
