public class Ladestation {

    public double x;
    public double y;
    public int id;

    public int lastverteilung;

    public final int restlast;
        // Klasse der Ladestation Generierung mit Koordinaten, der übergebenen Lastverteilung
        // restlast wird genutzt um die Lastverteilung zurückzusetzen
        // genutzt wird diese Zurücksetzung bei getGesamtabstandzuWohnorte
        // Weil durch die Benutzung der Methode die Lastverteilung für die übergebenen Instanzen der
        // Ladestationen auf 0 dekrementiert wird, muss um die Lastverteilung wieder bei
        // Bei weiteren Berechnungen der gesamtabständen zu Wohnorten zurückgesetzt werden
    public Ladestation(double x, double y, int id,int lastverteilung) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.lastverteilung = lastverteilung;
        this.restlast = lastverteilung;
    }
    // Ausgabe der Ladestation samt Koordinaten
    public String toString() {
        return "Ladestation" + this.id +"(" + this.x + " , " + this.y + " )";
    }
    // Setzen Lastverteilung zurück für Algorithmus
    public void resetLastveteilung() {
        this.lastverteilung = this.restlast;
    }
}
