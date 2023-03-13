public class BasisStation {

    public double x;
    public double y;
    // Constructor mit Koordinatenpaar
    public BasisStation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // Ausgabe der Koordinaten der Basisstation
    public String toString() {
        return "BasisStation(" + this.x + " , " + this.y + " )";
    }
    public boolean equals(WohnOrt o) {
        return this.x == o.x && this.y == o.y;
    }
}
