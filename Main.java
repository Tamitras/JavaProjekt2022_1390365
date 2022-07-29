import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Enums.SearchType;
import Models.Person;
import Models.Place;
import Services.DataExtractor;
import Services.ModelService;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Incorrect arguments passed. Pass the operation details as argument.");
        } else {

            String keyword = "";
            // Erstellt ein neues ModelService Objekt
            ModelService service = new ModelService();

            // Verwendung der statisches Methode "Extract" des DataExtractor um
            // Informationen aus einer Datei zu extrahieren.
            DataExtractor.Extract(service);

            SearchType searchType = SearchType.DEFAULT;

            if (args[0].startsWith("--personensuche=")) {
                searchType = SearchType.PERSONENSUCHE;
            } else if (args[0].startsWith("--ortssuche=")) {
                searchType = SearchType.ORTSSUCHE;
            } else if (args[0].startsWith("--kontaktpersonen=")) {
                searchType = SearchType.KONTAKTPERSONEN;
            } else if (args[0].startsWith("--besucher=")) {
                searchType = SearchType.BESUCHER;
            }

            keyword = args[0].split("=")[1].replaceAll("\"", "");

            switch (searchType) {
                case PERSONENSUCHE:
                    SearchPerson(keyword, service);
                    break;
                case ORTSSUCHE:
                    SearchPlace(keyword, service);
                    break;
                case KONTAKTPERSONEN:
                    SearchContactPerson(args, service);
                    break;
                case BESUCHER:
                    SearchVisitor(args, service);
                    break;

                default:
                    break;
            }
        }
    }

    private static void SearchVisitor(String[] args, ModelService service) {
        int placeId = Integer.parseInt(args[0].split(",")[0].split("=")[1]);
        String time = args[0].split(",")[1].replaceAll("\"", "");
        time += "Z";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = null;
        try {
            dateTime = formatter.parse(time);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        List<Person> visitorAndContacts = service.visitorsAndContacts(placeId, dateTime);

        for (int i = 0; i < visitorAndContacts.size(); i++) {
            if (i != visitorAndContacts.size() - 1) {
                System.out.print(visitorAndContacts.get(i).getName() + ", ");
            } else {
                System.out.println(visitorAndContacts.get(i).getName());
            }
        }
    }

    private static void SearchContactPerson(String[] args, ModelService service) {
        int personId = Integer.parseInt(args[0].split("=")[1]);
        List<Person> contacts = service.getContacts(personId);
        for (int i = 0; i < contacts.size(); i++) {
            if (i != contacts.size() - 1) {
                System.out.print(contacts.get(i).getName() + ", ");
            } else {
                System.out.println(contacts.get(i).getName());
            }
        }
    }

    private static void SearchPlace(String keyword, ModelService service) {
        List<Place> places = service.searchPlace(keyword);
        for (int i = 0; i < places.size(); i++) {
            if (i != places.size() - 1) {
                System.out.print(places.get(i).getName() + ", ");
            } else {
                System.out.println(places.get(i).getName());
            }
        }
    }

    private static void SearchPerson(String keyword, ModelService service) {
        List<Person> persons = service.searchPerson(keyword);
        for (int i = 0; i < persons.size(); i++) {
            if (i != persons.size() - 1) {
                System.out.print(persons.get(i).getName() + ", ");
            } else {
                System.out.println(persons.get(i).getName());
            }
        }
    }
}
