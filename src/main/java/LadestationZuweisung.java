public class LadestationZuweisung implements Comparable<LadestationZuweisung>{

    public double abstand;
    public Ladestation ladestation;
        // Wird genutzt in der Methode getGesamtabstandzuWohnorte
        // Eine Instanz der Ladestation wird mit dem Abstand zu einem bestimmten Wohnort
        // gespeichert. Diese Instanzen der LadestationZuweisungsklasse werden vergleichen
        // um die Ladestation zu wählen die den minimalsten Abstand zu dem bestimmten
        // Wohnort hat. Damit diese Instanzen sortiert werden können müssen wir das
        // Comparable Interface implementieren
    public LadestationZuweisung(double abstand, Ladestation ladestation) {
        this.abstand = abstand;
        this.ladestation = ladestation;
    }
    // Ausgabe der Ladestation samt Abständen
 public String toString() {
        return "ladestation"+ ladestation.id + " Abstand: " + abstand;
 }
    @Override
    public int compareTo(LadestationZuweisung o) {
        return Double.compare(this.abstand, o.abstand);    }
}
