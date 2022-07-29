package Models;

public class Person extends Base {

    public Person(int id, String name) {
        super(id, name);
    }

    public String toString() {
        return "Person: " + "Id=" + id + ", Name=" + name;
    }
}
