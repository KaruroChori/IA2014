package violajones;

import java.awt.Point;

public class Feature {
    
    Rettangolo[] rettangoli;
    int numeroDiRettangoli;
    float valoreSoglia;
    float valoreSX;
    float valoreDX;
    /*
     * Point è un oggetto che contine due coordinate x, y
    */
    Point size; 
    int nodoSX;
    int nodoDX;
    boolean valoreSXCÈ;
    boolean valoreDXCÈ;
    
    /*
     * Aggiunge un rettangolo all'array che li contiene, incrementando il numero 
     * di rettangoli presenti.
    */
    public void aggiungi(Rettangolo r) {
        rettangoli[numeroDiRettangoli++] = r;
    }
    
    /*
     * Una feature è posta ad ogni nodo di un albero, è caratterizzata da una soglia,
     * e ritorna il successivo (nodo sinistro o destro) in cui entrare in base al valore 
     * calcolato e al valore soglia. La feature è caratterizzata da un rettangolo pesato 
     * e il valore è la somma dei pixel all'interno di ogni rettangolo, 
     * pesati con il peso del rettangolo.
     * 
    */
    public Feature(float soglia, float valsx, float valdx, int sx, int dx, boolean sxcè, boolean dxcè, Point size) {
        numeroDiRettangoli = 0;
        rettangoli = new Rettangolo[3];
        valoreSoglia = soglia;
        valoreSX = valsx;
        valoreDX = valdx;
        nodoSX = sx;
        nodoDX = dx;
        valoreSXCÈ = sxcè;
        valoreDXCÈ = dxcè;
        this.size = size;       
    }
    
    public int scegliNodoSXDX(int[][] intImg, int[][] sqrIntImg, int i, int j, float scala) {
        /*
         * Scala il punto di coordinate x e y
        */
        int w = (int)(scala*size.x);
        int h = (int)(scala*size.y);
        /*
         * Calcola l'inverso dell'area della finestra di coordinate w*h
        */
        double areaInversa = 1./(w*h);
        /*
         * Sfruttando l'immagine integrale e la sua forma quadratica calcola l'area e l'area quadratica della finestra,
         * senza compiere iterazioni.
        */
        int sum = intImg[i+w][j+h] + intImg[i][j] - intImg[i][j+h] - intImg[i+w][j];
        int pow_sum_2 = sqrIntImg[i+w][j+h] + sqrIntImg[i][j] - sqrIntImg[i][j+h] - sqrIntImg[i+w][j];
        /* 
         * Calcolo la media e la varianza della finestra
        */
        double media = sum*areaInversa;
        double stdDev = pow_sum_2*areaInversa-media*media;
        stdDev = (stdDev>1)? Math.sqrt(stdDev):1;
        int sum_rettangoli = 0;
        for (int n = 0; n < numeroDiRettangoli; n++) {
            Rettangolo rettangolo = rettangoli[n];
            /*
             * Scala il rettangolo in base alla grandezza della finestra.
            */
            int rettangolox1 = i + (int)(scala*rettangolo.x1);
            int rettangolox2 = i + (int)(scala*(rettangolo.x1+rettangolo.y1));
            int rettangoloy1 = j + (int)(scala*rettangolo.x2);
            int rettangoloy2 = j + (int)(scala*(rettangolo.x2+rettangolo.y2));
            /*
             * Calcolo la somma dei pixel nel rettangoli (pesata con il peso del rettangolo).
            */
            sum_rettangoli += (int)((intImg[rettangolox1][rettangoloy1] + intImg[rettangolox2][rettangoloy2] 
                    - intImg[rettangolox1][rettangoloy2] - intImg[rettangolox2][rettangoloy1])*rettangolo.weight);
        }
        double sum_rettangoli_2 = sum_rettangoli*areaInversa;
        /*
         * Scelgo il nodo a sinistra o a destra comparando il valore soglia con il valore sum_rettangoli_2.
        */
        return (sum_rettangoli_2 < valoreSoglia*stdDev)? Tree.SINISTRA : Tree.DESTRA;
    }        
}
