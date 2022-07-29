package Tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import Services.ModelService;
import Services.DataExtractor;
import org.junit.Before;
import org.junit.Test;

import Models.Person;
import Models.Place;

import static org.junit.Assert.assertTrue;


public class ServiceTest {

    private ModelService service;

    @Before
    @Test
    public void ScannerTest() {
        service = new ModelService();
        DataExtractor.Extract(service);
    }

    @Test
    public void SearchForPersonTest() {

        List<Person> personExpected = new ArrayList<Person>();
        personExpected.add(new Person(22, "Leonie"));
        personExpected.add(new Person(61, "Antonia"));
        personExpected.add(new Person(149, "Toni"));

        // searchterm oni
        // expected result: Toni, Antonia, Leoni
        String searchTerm = "--personensuche=oni";

        String keyword = searchTerm.split("=")[1].replaceAll("\"", "");

        List<Person> foundPersons = service.searchPerson(keyword);

        if (foundPersons.stream().anyMatch(p -> p.name.equals("Leonie"))) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
        if (foundPersons.stream().anyMatch(p -> p.name.equals("Toni"))) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
        if (foundPersons.stream().anyMatch(p -> p.name.equals("Antonia"))) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void SearchForPlaceTest() {

        List<Place> placeExpected = new ArrayList<Place>();
        placeExpected.add(new Place(6, "Großmarkt", true));

        String searchTerm = "--ortssuche=arkt";

        String keyword = searchTerm.split("=")[1].replaceAll("\"", "");

        List<Place> foundPlaces = service.searchPlace(keyword);

        if (foundPlaces.stream().anyMatch(p -> p.name.equals("Großmarkt"))) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void SearchForContactPersonTest() {
        // Aaron, Amelie, Ben, Emil,
        // Emilia, Emily, Felix, Hannah,
        // Hannes, Julius, Leonard, Levi,
        // Louis, Malia, Marlene, Ole,
        // Rosalie, Sophia, Victoria
        // 19stk

        String searchTerm = "--kontaktpersonen=1";

        int keyword = Integer.parseInt(searchTerm.split("=")[1]);

        List<Person> foundPersons = service.getContacts(keyword);

        if (foundPersons.size() == 19) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void SearchForVisitsTest()
    {
        // Adam, Amelie, Carla, Carlotta,
        // Charlotte, Elli, Emil, Emilia,
        // Emily, Emma, Eva, Fiona,
        // Hannah, Hannes, Jonah, Jonas,
        // Joshua, Konstantin, Lian, Lisa,
        // Luisa, Malia, Mara, Maria,
        // Mattis, Max, Melina, Mia,
        // Mohammed, Noah, Ole, Sophia,
        // Tim, Tom, Toni, Victoria

        // besucher =1 --> Mia
        String searchTerm = "--besucher=1,2021-05-15T14:16:00";

        int placeId = Integer.parseInt(searchTerm.split(",")[0].split("=")[1]);
        String time = searchTerm.split(",")[1].replaceAll("\"", "");
        time += "Z";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = null;

        try {
            dateTime = formatter.parse(time);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        List<Person> visitorAndContacts = service.visitorsAndContacts(placeId, dateTime);

        if (visitorAndContacts.size() == 36) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }
}
