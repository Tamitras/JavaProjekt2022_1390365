package Services;

import java.util.Scanner;
import Enums.ModelType;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Die Klasse ExtractService wird verwendet um Informationen aus einer Datei zu
 * extrahieren.
 */
public class DataExtractor {

    // Relativer Pfad zu der Datei im Ordner Contacts
    // private final static String Contacts = "Contacts/contacts2021.db";
    private final static String Contacts = "C:/Users/Eriktion/Desktop/JavaProject2022/JavaProjekt2022_1390365/Contacts/contacts2021.db";

    /**
     * Die Methode Extract ließt die Datei Zeile für Zeile aus und speichert alle
     * Daten in den Models des ModelService
     * 
     * @param service
     */
    public static void Extract(ModelService service) {
        try {

            // Definition lokaler Variablen
            String line = "";
            ModelType readingEntity = ModelType.DEFAULT;

            /*
             * Das Scanner Objekt wird verwendet um Informationen aus einer Datei zu
             * extrahieren
             */
            Scanner scan = new Scanner(new File(Contacts));

            /* "Solange" das scan Objekt noch Bytes zum Lesen hat */
            while (scan.hasNext()) {
                line = scan.nextLine();

                if (line.startsWith("New_Entity: \"person_id\"")) {
                    readingEntity = ModelType.PERSON;
                    continue;
                } else if (line.startsWith("New_Entity: \"location_id\"")) {
                    readingEntity = ModelType.PLACE;
                    continue;
                } else if (line.startsWith("New_Entity: \"start_date\"")) {
                    readingEntity = ModelType.VISIT;
                    continue;
                }

                switch (readingEntity) {
                    // Abschnitt Person
                    case PERSON:
                        ReadingPerson(service, line);
                        break;

                    case PLACE:
                        // Abschnitt Ort
                        ReadingPlace(service, line);
                        break;

                    case VISIT:
                        // Abschnitt Besuch
                        ReadingVisit(service, line);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Contacts file loading error.");
            System.exit(0);
        }
    }

    private static void ReadingVisit(ModelService service, String line) {
        String[] attr = line.split(",");
        String start = attr[0].replaceAll("\"", "").trim() + "Z";
        String end = attr[1].replaceAll("\"", "").trim() + "Z";

        int personId = Integer.parseInt(attr[2].replaceAll("\"", "").trim());
        int placeId = Integer.parseInt(attr[3].replaceAll("\"", "").trim());

        SimpleDateFormat fullFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat withoutSSFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

        Date startDate = null;
        Date endDate = null;

        try {
            startDate = fullFormatter.parse(start);
        } catch (ParseException ex) {
            try {
                startDate = withoutSSFormatter.parse(start);
            } catch (ParseException ex2) {
                ex2.printStackTrace();
            }
        }

        try {
            endDate = fullFormatter.parse(end);
        } catch (ParseException ex) {
            try {
                endDate = withoutSSFormatter.parse(end);
            } catch (ParseException ex2) {
                ex2.printStackTrace();
            }
        }

        service.addVisit(startDate, endDate, personId, placeId);
    }

    private static void ReadingPlace(ModelService service, String line) {
        String[] attr = line.split(",");
        int id = Integer.parseInt(attr[0].replaceAll("\"", "").trim());
        String name = attr[1].replaceAll("\"", "").trim();
        boolean indoor;
        if (attr[2].replaceAll("\"", "").trim().equals("in_door")) {
            indoor = true;
        } else {
            indoor = false;
        }
        service.addPlace(id, name, indoor);
    }

    private static void ReadingPerson(ModelService service, String line) {
        String[] attr = line.split(",");
        int id = Integer.parseInt(attr[0].replaceAll("\"", "").trim());
        String name = attr[1].replaceAll("\"", "").trim();
        service.addPerson(id, name);
    }
}
