package violajones;

public class Rettangolo {

    /*
     * Coordinate spigoli rettangolo.
     */
    protected int x1, x2, y1, y2;
    protected float weight;

    public Rettangolo(int x1, int x2, int y1, int y2, float weight) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.weight = weight;
    }

    /*
     * Crea un rettangolo a partire da una stringa.
     */
    public static Rettangolo convert(String text) {
        String[] parameters = text.split(" ");
        int a = Integer.parseInt(parameters[0]);
        int b = Integer.parseInt(parameters[1]);
        int c = Integer.parseInt(parameters[2]);
        int d = Integer.parseInt(parameters[3]);
        Float weight = Float.parseFloat(parameters[4]);
        return new Rettangolo(a, b, c, d, weight);
    }
}
