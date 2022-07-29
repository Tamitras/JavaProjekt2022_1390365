package Models;

/**
 * Place KLasse welche von der Basis-Klasse Base erbt
 */
public class Place extends Base {

    private boolean indoor;

    public Place(int id, String name, boolean indoor) {
        super(id, name);
        this.indoor = indoor;
    }

    public boolean isIndoor() {
        return indoor;
    }

    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    public String toString() {
        return "Place: " + "Id=" + id + ", Name=" + name + ", Indoor=" + indoor;
    }
}
