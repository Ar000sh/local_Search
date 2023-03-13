public class WohnOrt {

    public double x;
    public double y;
    // Constructor mit Koordinatenpaar
    public WohnOrt(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // Ausgabe der Koordinaten der Wohnorte
    public String toString() {
        return "Wohnort(" + this.x + " , " + this.y + " )";
    }
    public boolean equals(WohnOrt o) {
        return this.x == o.x && this.y == o.y;
    }
}
