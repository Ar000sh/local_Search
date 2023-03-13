import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Suche {

    public int height;
    public int width;
    public int num_ladestationen;

    public List<WohnOrt> wohnortList;



    public BasisStation basisStation;



        // Constructor mit Größe des Suchraums
        // Menge an ladestationen, Wohnorten und der Basisstation
    public Suche(int height, int width, Eingabe eingabe) {
        this.height = height;
        this.width = width;
        this.num_ladestationen = eingabe.num_ladeStationen;
        this.wohnortList = eingabe.wohnOrtList;
        this.basisStation = eingabe.basisStation;
    }


    // Berechneter Quadratischer Abstand wird auf 3 Dezimalzahlen aufgerundet um einfacher
    // Evaluieren zu können
    public double abstand(double x1, double y1, double x2, double y2) {
        return roundTo3decimals((Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2)));
    }
    // Auf 3 Dezimalzahlen aufgerundeter Rückgabewert, des Gesamten Abstands von jeder Ladestation summiert
    // zur Basisstation
    //
    public double getGesamtAbstandZuBasis(List<Ladestation> list) {
        double result = 0.0;
        for (Ladestation ladestation : list) {
            result += abstand(ladestation.x,ladestation.y, basisStation.x, basisStation.y);
        }
        return roundTo3decimals(result);
    }

    // Bewertungsfunktion mit Gewichtung die gegen die Basisstation geht mit 70% Basisstationsgewichtung und
    // 100% Wohnorte
    public double getBewertungsFunktion(double abstandZuWohnorten,double abstandZuBasis ) {

        return   abstandZuWohnorten  + (0.7 * abstandZuBasis);
    }
    // Auf 3 Dezimalzahlen aufgerundeter Rückgabewert, des Gesamten Abstands von jeder Ladestation summiert
    // zu Wohnorten. Für die jeweiligen Wohnorte, werden alle Ladestationen durchiteriert, und der Abstand zum
    // jeweiligen Wohnort berechnet. Die Ladestation und der berechnete Abstand wird in einer Liste gespeichert
    // Die Liste wird sortiert um die Ladestation zu wählen, die den minimalsten Abstand zum jeweiligen Wohnort
    // hat. Prüfung ob Lastverteilung der gewählten Ladestation noch einen Wohnort zulässt.
    // Wenn nicht wird der nächstbeste gewählt.
    // Summe der Abstände werden auf 3 Nachkommastellen zurückgegeben um die Suche zu vereinfachen
    public double getGesamtAbstandZuWohnorten(List<Ladestation> list) {

        double result = 0.0;
        List<LadestationZuweisung> zwischenSpeicher = new ArrayList<>();

        for (WohnOrt wohnort : wohnortList) {

            for (Ladestation ladestation : list) {
                zwischenSpeicher.add(new LadestationZuweisung(abstand(wohnort.x, wohnort.y, ladestation.x,ladestation.y),ladestation));
            }
            zwischenSpeicher = zwischenSpeicher.stream().sorted().toList();

            if (zwischenSpeicher.stream().allMatch(x -> x.ladestation.lastverteilung == 0)) {
                list.stream().forEach(x -> x.lastverteilung = 1);

            }
            for (int i = 0; i < zwischenSpeicher.size(); i++) {
                if (zwischenSpeicher.get(i).ladestation.lastverteilung > 0) {
                    result += zwischenSpeicher.get(i).abstand;

                    zwischenSpeicher.get(i).ladestation.lastverteilung -= 1;
                    break;

                }
            }



            zwischenSpeicher = new ArrayList<>();
        }

        list.stream().forEach(x -> x.resetLastveteilung());


        return roundTo3decimals(result);
    }


    // Wird abgeprüft ob die übergebenen Koordinaten bereits von Ladestationen, Wohnorte oder Basistation
    // besetzt sind
    public boolean BesetzAbprüfung(double x , double y, List<Ladestation> ladestationList) {
        boolean foundladestation = ladestationList.stream().filter(ladestation -> (ladestation.x == x && ladestation.y == y)).count() > 0;

        return WohnBasisAbprüfung(x,y) || foundladestation;
    }
    // Wird geprüft ob im folgenden Koordinatenpaar ein Wohnort oder eine Basisstation bereits besetzt ist
    public boolean WohnBasisAbprüfung(double x , double y) {
        boolean foundwohnort = wohnortList.stream().filter(wohnort -> (wohnort.x == x && wohnort.y == y)).count() > 0;
        boolean foundBasis = x == basisStation.x && y == basisStation.y;

        return foundwohnort || foundBasis;
    }

    // Geben die Verfügbaren Slots im Suchraum zurück
    // Wichtig für Ladestation Plazierung bei Initialisierung
    public List<XYPair> getVerfügbareSlots() {
        List<XYPair> result = new ArrayList<>();
        for(int i = 0; i < this.height; i++) {

            for (int j = 0; j < this.width; j++) {
                if (!WohnBasisAbprüfung(i,j)) {
                    result.add(new XYPair(i,j));
                }
            }
        }
        return result;
    }

    // Gehen von den Koordinaten 0,1 KM in jede Richtung
    // Prüfen mit BesetzAbprüfung ob bereits besetzt
    // Wenn nicht dann wird potentieller Nachbar in die Liste geadded die später zurückgegeben wird
    // aufgerundet auf 1 Decimal um die Voraussetzung mit 100 Meter Schritten zu erfüllen
    public List<XYPair> findPotentialeNachbarn(double x, double y, List<Ladestation> ladestationList) {
        List<XYPair> result = new ArrayList<>();
        if (!BesetzAbprüfung(x + 0.1,y,ladestationList) && (x + 0.1) >= 0 && (x + 0.1) <= this.width) {
            result.add(new XYPair(roundTo1decimals(x + 0.1),roundTo1decimals(y)));
        }
        if (!BesetzAbprüfung(x - 0.1,y,ladestationList) && (x - 0.1) >= 0 && (x - 0.1) <= this.width) {
            result.add(new XYPair(roundTo1decimals(x - 0.1),roundTo1decimals(y)));
        }
        if (!BesetzAbprüfung(x,y + 0.1,ladestationList) && (y + 0.1) >= 0 && (y + 0.1) <= this.height) {
            result.add(new XYPair(roundTo1decimals(x),roundTo1decimals(y + 0.1)));
        }
        if (!BesetzAbprüfung(x,y - 0.1,ladestationList) && (y - 0.1) >= 0 && (y - 0.1) <= this.height) {
            result.add(new XYPair(roundTo1decimals(x),roundTo1decimals(y - 0.1)));
        }
        return result;
    }
    // Random Verfügbarer Slot im Raum bekommen um zufällig die Ladestationen zu platzieren
    // nur bei Initialisierung der Ladestationen
    public XYPair getRandomVerfügbarerSlot() {
        List<XYPair> candidates = getVerfügbareSlots();
        int max = candidates.size() - 1;
        int min = 0;
        int index = new Random().nextInt(max - min) + min;
        return candidates.get(index);
    }
    // Methode um einzelne Hill Climbing Durchläufe zu vergleichen und den besten Durchlauf zurückzugeben
    // Durchlauf mit Minimum Bewertung wird als bester Durchlauf gezählt
    public HillClimbDurchlauf getLadeStationenWithMin(List<HillClimbDurchlauf> values) {
        HillClimbDurchlauf min = new HillClimbDurchlauf(1000.0,1000.0,values.get(0).ladestationList);
        for (int i = 0; i < values.size(); i++) {
            double abstandWohnorte = values.get(i).abstandZuWohnorte;
            double abstandBasis = values.get(i).abstandZuBasisStation;
            if (getBewertungsFunktion(min.abstandZuWohnorte , min.abstandZuBasisStation)  > getBewertungsFunktion(abstandBasis , abstandWohnorte)) {
                min = values.get(i);
            }

        }
        return min;
    }


    // Erzeugen mehrere Hill Climbing Durchläufe
    // Speichern die Hill Climbing Durchläufe in einer Liste die wir der
    // GetLadestationwithmin geben
    public SuchErgebnis randomhillClimbing(int iterations, int maxiterationperClimb) {

        List<HillClimbDurchlauf> use = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            List<Ladestation> temp = hillClimbing(maxiterationperClimb);
            use.add(new HillClimbDurchlauf(getGesamtAbstandZuWohnorten(temp), getGesamtAbstandZuBasis(temp),temp));
        }
        SuchErgebnis result = new SuchErgebnis(getLadeStationenWithMin(use),this.basisStation,this.wohnortList);
        return result;

    }

    // Hill Climbing Methode
    // Lastverteilung wird berechnet, bei der Initialisierung der Ladestationen mitgegeben
    // Multiple Suchdurchläufe mit der Abbruchbedingung maxIterations oder
    // Die Bewertung hat sich durch eine weitere Iteration nicht mehr verbessert
    // Alle Ladestationen werden durchiteriert und es werden für die jeweilige Ladestation
    // Alle Nachbarn aufgesucht. Nachbarn werden auf Verbesserung der Bewertung geprüft
    // Sollte eine Verbesserung der Bewertung, durch das ersetzen der derzeitigen Ladestation,
    // durch einen der 4 potentiellen Nachbarn erfolgen, wird die Ladestation ersetzt.
    public List<Ladestation> hillClimbing(int maxIterations) {
        int count = 0;


        int last = wohnortList.size() / num_ladestationen;

        List<Ladestation> result = new ArrayList<>();

        for (int i = 0; i < this.num_ladestationen; i++) {
            XYPair temp = getRandomVerfügbarerSlot();
            result.add(new Ladestation(temp.x,temp.y,i + 1,last));
        }

        double startBewertung = 0.0;
        double vergleichBewertung = 0.1;
        while ((startBewertung != vergleichBewertung)) {
            count++;

            if (count > maxIterations) {
                break;
            }

            XYPair nachbar;
            startBewertung = getBewertungsFunktion(getGesamtAbstandZuWohnorten(result),getGesamtAbstandZuBasis(result));


            for (int x = 0; x < result.size(); x++) {

                double bewertungBesterNachbar = 0.0;
                Ladestation ladestation = result.get(x);

                nachbar = new XYPair(ladestation.x,ladestation.y);
                for (XYPair replacement : findPotentialeNachbarn(ladestation.x,ladestation.y,result)) {
                    List<Ladestation> ladestationtemp = new ArrayList<>(result);


                    int id =  ladestation.id;
                    ladestationtemp.remove(ladestation);


                    ladestationtemp.add(new Ladestation(replacement.x,replacement.y,id,ladestation.lastverteilung));

                    double bewertungsCurrentNachbar = getBewertungsFunktion(getGesamtAbstandZuWohnorten(ladestationtemp),getGesamtAbstandZuBasis(ladestationtemp));


                    if (bewertungBesterNachbar == 0.0 || bewertungsCurrentNachbar < bewertungBesterNachbar) {
                        bewertungBesterNachbar = bewertungsCurrentNachbar;
                        nachbar = replacement;
                    }
                }

                if (bewertungBesterNachbar < getBewertungsFunktion(getGesamtAbstandZuWohnorten(result),getGesamtAbstandZuBasis(result))) {

                    int id  = ladestation.id;
                    int getIndex = result.indexOf(ladestation);
                    result.remove(ladestation);
                    result.add(getIndex,new Ladestation(nachbar.x, nachbar.y, id,ladestation.lastverteilung));

                }
            }
            vergleichBewertung = getBewertungsFunktion(getGesamtAbstandZuWohnorten(result),getGesamtAbstandZuBasis(result));


        }


        return result;

    }
    // Runden hoch ab der Hälfte der Value, auf 3 Nachkommastellen
    public static double roundTo3decimals(double value) {
        return new BigDecimal(value).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }
    // Runden hoch ab der Hälfte der Value, auf 1 Nachkommastelle
    public static double roundTo1decimals(double value) {
        return new BigDecimal(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    public static void main(String[] args) throws IOException {

        // Aufgabe1

        Suche szenario1 = new Suche(9,9,new Eingabe("src/main/resources/Eingaben/test1.txt"));
        SuchErgebnis szenario1randomhillclimb = szenario1.randomhillClimbing(10,1000);
        System.out.println("\n==================================== szenario1 ====================================\n" + szenario1randomhillclimb);
        CreateCSV.createFileWohnorte("wohnortszenario2.csv",szenario1randomhillclimb.wohnOrtList);
        CreateCSV.createFileBasisStation("basisstationszenario2.csv",szenario1randomhillclimb.basisStation);
        CreateCSV.createFileLadestation("ladestationszenario2.csv",szenario1randomhillclimb.hillClimbDurchlauf.ladestationList);



        //Aufgabe2
        Suche szenario2 = new Suche(10,10,new Eingabe("src/main/resources/Eingaben/test2.txt"));
        SuchErgebnis szenario2randomhillclimb = szenario2.randomhillClimbing(20,1000);
        System.out.println("\n==================================== szenario2 ====================================\n" + szenario2randomhillclimb);
        CreateCSV.createFileWohnorte("wohnortszenario2.csv",szenario2randomhillclimb.wohnOrtList);
        CreateCSV.createFileLadestation("ladestationszenario2.csv",szenario2randomhillclimb.hillClimbDurchlauf.ladestationList);
        CreateCSV.createFileBasisStation("basisstationszenario2.csv",szenario2randomhillclimb.basisStation);



        // Aufgabe3

        Suche szenario3 = new Suche(10,10,new Eingabe("src/main/resources/Eingaben/test3.txt"));
        SuchErgebnis szenario3randomhillclimb = szenario3.randomhillClimbing(70,1000);
        System.out.println("\n==================================== szenario3 ====================================\n" + szenario3randomhillclimb);
        CreateCSV.createFileWohnorte("wohnortszenario3.csv",szenario3randomhillclimb.wohnOrtList);
        CreateCSV.createFileLadestation("ladestationszenario3.csv",szenario3randomhillclimb.hillClimbDurchlauf.ladestationList);
        CreateCSV.createFileBasisStation("basisstationszenario3.csv",szenario3randomhillclimb.basisStation);


        // Aufgabe4

        Suche szenario4 = new Suche(10,10,new Eingabe("src/main/resources/Eingaben/test4.txt"));
        SuchErgebnis szenario4randomhillclimb = szenario4.randomhillClimbing(150,1000);
        System.out.println("\n==================================== szenario4 ====================================\n" + szenario4randomhillclimb);
        CreateCSV.createFileWohnorte("wohnortszenario4.csv",szenario4randomhillclimb.wohnOrtList);
        CreateCSV.createFileLadestation("ladestationszenario4.csv",szenario4randomhillclimb.hillClimbDurchlauf.ladestationList);
        CreateCSV.createFileBasisStation("basisstationszenario4.csv",szenario4randomhillclimb.basisStation);



        // Aufgabe5
        Suche szenario5 = new Suche(10,10,new Eingabe("src/main/resources/Eingaben/test5.txt"));
        SuchErgebnis szenario5randomhillclimb = szenario5.randomhillClimbing(450,1000);
        System.out.println("\n==================================== szenario5 ====================================\n" + szenario5randomhillclimb);
        CreateCSV.createFileWohnorte("wohnortszenario5.csv",szenario5randomhillclimb.wohnOrtList);
        CreateCSV.createFileLadestation("ladestationszenario5.csv",szenario5randomhillclimb.hillClimbDurchlauf.ladestationList);
        CreateCSV.createFileBasisStation("basisstationszenario5.csv",szenario5randomhillclimb.basisStation);




    }

}
