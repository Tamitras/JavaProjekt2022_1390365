package Models;
import java.util.Date;

/**
 * Klasse Visit repräsentiert ein Besuch an einem bestimmten Ort, zu einer bestimmten Zeit
 */
public class Visit {

    private Date startDate;
    private Date endDate;
    private Person person;
    private Place place;

    public Visit(Date startDate, Date endDate, Person person, Place place) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.person = person;
        this.place = place;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
