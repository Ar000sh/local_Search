
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
public class Eingabe {

    public List<WohnOrt> wohnOrtList;
    public BasisStation basisStation;
    public int num_ladeStationen;
    // Lesen file
    // zuerst Menge der Ladestationen
    // Dann Koordinatenpaar der Basisstation
    // Zu guter letzt Koordinaten der Wohnorte
    public Eingabe(String filename) {
        List<String> l = readFileInList(filename);
        this.num_ladeStationen = Integer.parseInt(l.get(0).trim());
        this.basisStation = new BasisStation(Double.parseDouble(l.get(1).split("\\s+")[0]),Double.parseDouble(l.get(1).split("\\s+")[1]));
        this.wohnOrtList = l.stream().filter(x -> l.indexOf(x) > 1).map(str -> str.split("\\s+")).map(y -> new WohnOrt(Double.parseDouble(y[0].trim()),Double.parseDouble(y[1].trim()))).toList();
    }
    public static void main(String[] args) throws FileNotFoundException {
       Eingabe test = new Eingabe("src/main/resources/Eingaben/test3.txt");
        System.out.println(test.num_ladeStationen);
        System.out.println(test.basisStation);
        System.out.println(test.wohnOrtList);


    }

    // Lesen der Datei und speichern die Zeilen in einer List<String>
    // Diese Liste wird im Konstruktor genutzt um alle Attribute der Klasse zu initialisieren
    public static List<String> readFileInList(String fileName)
    {

        List<String> lines = Collections.emptyList();
        try
        {
            lines =
                    Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }

        catch (IOException e)
        {

            // do something
            e.printStackTrace();
        }
        return lines;
    }


}
