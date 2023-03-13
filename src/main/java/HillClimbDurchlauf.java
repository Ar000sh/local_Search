import java.util.List;

public class HillClimbDurchlauf {
    // Klasse wird genutzt um die Ausgaben der Hill Climbing Methode zu speichern
    public double abstandZuWohnorte;
    public double abstandZuBasisStation;
    public List<Ladestation> ladestationList;
    public HillClimbDurchlauf(double abstandZuWohnorte, double abstandZuBasisStation, List<Ladestation> ladestationList) {
        this.abstandZuWohnorte = abstandZuWohnorte;
        this.abstandZuBasisStation = abstandZuBasisStation;
        this.ladestationList = ladestationList;
    }
    // Ausgabe des Hill Climbing Durchlaufs mit allen Endwerten
    public String toString() {
        return "Abstand zu Wohnorten:\t" + this.abstandZuWohnorte +"\t\tAbstand zu BasisStation:\t" + this.abstandZuBasisStation +"\n" + this.ladestationList;
    }
}
