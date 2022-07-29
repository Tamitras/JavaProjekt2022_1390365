package Interfaces;

import java.util.Date;
import java.util.List;

import Models.Person;
import Models.Place;
import Models.Visit;

/**
 * Das Interface IModelService verwaltet die Models und gibt vor, welche Funktionalität gewährleistet werden muss.
 */
public interface IModelService
{
    public List<Visit> Visits = null;

    public boolean addPerson(int id, String name);

    public boolean addPlace(int id, String name, boolean indoors);

    public boolean addVisit(Date startDate, Date endDate, int personId, int placeId);

    public Person getPerson(int id);

    public Place getPlace(int id);

    public List<Person> searchPerson(String keyword);

    public List<Place> searchPlace(String keyword);

    public List<Person> getContacts(int personID);

    public List<Person> visitorsAndContacts(int placeId, Date timeStamp);
}
