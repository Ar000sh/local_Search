import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CreateCSV {

    // Erzeugung der CSV Files für die Koordinaten der Wohnorte, Ladestationen und Basisstation.
    public static void createFileWohnorte(String filename,List<WohnOrt> wohnOrtList) throws IOException {
        FileWriter out = new FileWriter("src/main/resources/" + filename);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        wohnOrtList.forEach((wohnOrt) -> {
            try {
                printer.printRecord(wohnOrt.x,wohnOrt.y);
                printer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void createFileLadestation(String filename,List<Ladestation> ladestationList) throws IOException {
        FileWriter out = new FileWriter("src/main/resources/" + filename);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        ladestationList.forEach((ladestation) -> {
            try {
                printer.printRecord(ladestation.x,ladestation.y);
                printer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void createFileBasisStation(String filename,BasisStation basisStation) throws IOException {
        FileWriter out = new FileWriter("src/main/resources/" + filename);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);

        try {
            printer.printRecord(basisStation.x,basisStation.y);
            printer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
