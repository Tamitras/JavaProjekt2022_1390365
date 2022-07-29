package Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Interfaces.IModelService;
import Models.Person;
import Models.Place;
import Models.Visit;

/**
 * Die Klasse ModelService verwaltet die Models, implementiert das Interface
 * IModelService und stellt somit deren vorgesehene Funktionalität bereit.
 */
public class ModelService implements IModelService {
    public ArrayList<Visit> Visits;
    private ArrayList<Person> Persons;
    private ArrayList<Place> Places;

    public ModelService() {
        Persons = new ArrayList<>();
        Places = new ArrayList<>();
        Visits = new ArrayList<>();
    }

    /**
     * Fügt die Person zur Liste der Personen hinzu
     * 
     * @param id   ID der Person
     * @param name Name der Person
     * @returnLiefert den Status - Erfolgreich hinzgefügt / Nicht erfolgreich
     *                hinzugefügt
     */
    public boolean addPerson(int id, String name) {
        if (!personExist(id)) {
            Persons.add(new Person(id, name));
            return true;
        }
        return false;
    }

    /**
     * Fügt den Ort zur Liste der Orte hinzu
     * 
     * @param id      Id des Ortes
     * @param name    Name des Ortes
     * @param indoors Ist der Ort als "Drinnen" bzw. Überdacht Markiert
     * @return Liefert den Status - Erfolgreich hinzgefügt / Nicht erfolgreich
     *         hinzugefügt
     */
    public boolean addPlace(int id, String name, boolean indoors) {
        if (!placeExist(id)) {
            Places.add(new Place(id, name, indoors));
            return true;
        }
        return false;
    }

    /**
     * Fügt den Besuch zur Liste aller Besuche hinzu
     * 
     * @param startDate Startzeitpunkt
     * @param endDate   Endzeitpunkt
     * @param personId  Id der Person
     * @param placeId   Id des Ortes
     * @return Liefert den Status - Erfolgreich hinzgefügt / Nicht erfolgreich
     *         hinzugefügt
     */
    public boolean addVisit(Date startDate, Date endDate, int personId, int placeId) {
        Person person = getPerson(personId);
        Place place = getPlace(placeId);
        if (person != null && place != null) {
            Visits.add(new Visit(startDate, endDate, person, place));
            return true;
        }
        return false;
    }

    /**
     * Liefert eine Person anhand einer ID
     * 
     * @param id ID der Person
     * @return Person Objekt
     */
    public Person getPerson(int id) {
        for (Person person : Persons) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    /**
     * Liefert einen Ort anhand einer ID
     * 
     * @param id ID des Ortes
     * @return Place Objekt
     */
    public Place getPlace(int id) {
        for (Place place : Places) {
            if (place.getId() == id) {
                return place;
            }
        }
        return null;
    }

    /**
     * Liefert eine Liste mit Personen anhand eines "searchkeys"
     * 
     * @param keyword String nachdem in der Liste gesucht werden soll
     * @return ArrayList of Persons
     */
    public ArrayList<Person> searchPerson(String keyword) {
        ArrayList<Person> searchPersons = new ArrayList<>();
        keyword = keyword.toLowerCase();
        String name = "";
        for (Person person : Persons) {
            name = person.getName().toLowerCase();
            if (name.contains(keyword)) {
                searchPersons.add(person);
            }
        }
        return searchPersons;
    }

    /**
     * Liefert eine Liste mit Orten anhand eines "searchkeys"
     * 
     * @param keyword String nachdem in der Liste gesucht werden soll
     * @return ArrayList of Places
     */
    public ArrayList<Place> searchPlace(String keyword) {
        ArrayList<Place> searchPlaces = new ArrayList<>();
        keyword = keyword.toLowerCase();
        String name = "";
        for (Place place : Places) {
            name = place.getName().toLowerCase();
            if (name.contains(keyword)) {
                searchPlaces.add(place);
            }
        }
        return searchPlaces;
    }

    /**
     * Liefert eine Liste mit Kontakten anhand einer ID
     * 
     * @param personID ID der Person
     * @return List<Person>
     */
    public ArrayList<Person> getContacts(int personID) {
        ArrayList<Person> contacts = new ArrayList<>();
        Person person = getPerson(personID);
        if (person != null) {
            for (Visit visit : Visits) {
                if (visit.getPerson().getId() == person.getId() && visit.getPlace().isIndoor()) {
                    for (Visit matchVisit : Visits) {
                        if (matchVisit.getPlace().getId() == visit.getPlace().getId()
                                && isOverlapping(visit.getStartDate(), visit.getEndDate(), matchVisit.getStartDate(),
                                        matchVisit.getEndDate())
                                && !contacts.contains(matchVisit.getPerson())
                                && matchVisit.getPerson().getId() != person.getId()) {
                            contacts.add(matchVisit.getPerson());
                        }
                    }
                }
            }

            // Absteigendes Sortieren der Liste
            Collections.sort(contacts, new Comparator<Person>() {
                public int compare(Person p1, Person p2) {
                    return p1.getName().compareToIgnoreCase(p2.getName());
                }
            });
        }
        return contacts;
    }

    /**
     * Liefert eine Liste mit Personen, welche zu einer bestimmmten Zeit an einem
     * bestimmten Ort waren
     * 
     * @param placeId   ID des Ortes
     * @param timeStamp Der Zeitstempel
     * @return List<Person>
     */
    public ArrayList<Person> visitorsAndContacts(int placeId, Date timeStamp) {
        ArrayList<Person> visitors = new ArrayList<>();
        ArrayList<Person> visitorsAndContacts = new ArrayList<>();
        Place place = getPlace(placeId);
        if (place != null) {
            for (Visit visit : Visits) {
                if (visit.getPlace().getId() == placeId) {
                    if (isLyingBetween(visit.getStartDate(), visit.getEndDate(), timeStamp)) {
                        visitors.add(visit.getPerson());
                    }
                }
            }

            if (place.isIndoor()) {
                for (Person visitor : visitors) {
                    visitorsAndContacts.addAll(getContacts(visitor.getId()));
                    visitorsAndContacts.addAll(visitors);
                }
            } else {
                visitorsAndContacts = visitors;
            }
            visitorsAndContacts = removeDuplicates(visitorsAndContacts);

            // Absteigendes Sortieren der Liste
            Collections.sort(visitorsAndContacts, new Comparator<Person>() {
                public int compare(Person p1, Person p2) {
                    return p1.getName().compareToIgnoreCase(p2.getName());
                }
            });
        }
        return visitorsAndContacts;
    }

    private ArrayList<Person> removeDuplicates(ArrayList<Person> list) {

        ArrayList<Person> newList = new ArrayList<Person>();

        for (Person element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    private boolean personExist(int id) {
        for (Person person : Persons) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean placeExist(int id) {
        for (Place place : Places) {
            if (place.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean isLyingBetween(Date start, Date end, Date checkTime) {
        return (start.before(checkTime) && end.after(checkTime)) || start.compareTo(checkTime) == 0
                || end.compareTo(checkTime) == 0;
    }

    private boolean isOverlapping(Date startOne, Date endOne, Date startTwo, Date endTwo) {
        return startOne.before(endTwo) && startTwo.before(endOne);
    }
}
