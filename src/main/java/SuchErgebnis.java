import java.util.List;

public class SuchErgebnis {

    public HillClimbDurchlauf hillClimbDurchlauf;
    public BasisStation basisStation;
    public List<WohnOrt> wohnOrtList;

    public SuchErgebnis(HillClimbDurchlauf newPair, BasisStation basisStation, List<WohnOrt> wohnOrtList) {
        this.hillClimbDurchlauf = newPair;
        this.basisStation = basisStation;
        this.wohnOrtList = wohnOrtList;
    }
    // Ausgabe für Ergebnisse
    public String toString() {
        String result = "\t\tWohnorte\n" + wohnOrtList.get(0);
        int count = 0;
        for (int i = 1; i < wohnOrtList.size(); i++) {
            count++;
            if (count <= 6) {
                result +=  ", " + wohnOrtList.get(i);
            } else {
                result += "\n" + wohnOrtList.get(i);
                count = 0;
            }
        }
        count = 0;

        result += "\n\t\tBasisStation\n" + this.basisStation + "\n\t\tLadeStationen\n" + hillClimbDurchlauf.ladestationList.get(0);
        for (int i = 1; i < hillClimbDurchlauf.ladestationList.size(); i++) {
            if (count <= 6) {
                result += ", "  + hillClimbDurchlauf.ladestationList.get(i);
            } else {
                result += "\n" + hillClimbDurchlauf.ladestationList.get(i) ;
            }
        }
        result += "\nGesamte Abstand der LadeStationen von den Wohnorten beträgt:\t" + hillClimbDurchlauf.abstandZuWohnorte;
        result += "\nGesamte Abstand der LadeStationen von der BasisStation beträgt:\t" + hillClimbDurchlauf.abstandZuBasisStation;
        return result;
    }
}
