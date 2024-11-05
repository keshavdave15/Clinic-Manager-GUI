package scheduler;

/**
 * The Technician class is an abstract subclass of Provider
 * It represents a technician who has a fixed rate per visit
 * It does not calculate charges based on specialty as doctors do
 * Instead, the rate is predetermined and fixed per visit
 * This class also supports the creation of the circular linked list of
 * Technicians
 * @author Keshav Dave, Danny Watson
 */
public class Technician extends Provider {
    private final int ratePerVisit;
    private Technician next;
    private Technician head;

    /**
     * Constructor for the Technician class
     * Initializes the profile, location, and rate per visit
     * @param profile the profile of the technician
     * @param location the location where the technician works
     * @param ratePerVisit the rate per visit charged by the technician
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location);
        this.ratePerVisit = ratePerVisit;
        this.next = null;
        this.head = null;
    }

    /**
     * Override the rate method from the Provider class
     * @return the rate per visit for the technician
     */
    @Override
    public int rate() {
        return this.ratePerVisit;
    }

    /**
     * Getter for the next Technician in the circular list
     * @return the next Technician
     */
    public Technician getNext() {
        return next;
    }

    /**
     * Setter for the next Technician in the circular list
     * @param next the next Technician
     */
    public void setNext(Technician next) {
        this.next = next;
    }

    /**
     * Getter for the head of the circular list
     * @return the head Technician
     */
    public Technician getHead() {
        return this.head;
    }

    /**
     * Setter for the head of the circular list
     * @param head the head Technician
     */
    public void setHead(Technician head) {
        this.head = head;
    }

    /**
     * Method to calculate the size of the circular linked list of Technicians
     * @return the size (number of technicians) in the circular list
     */
    public int getSize() {
        if (this.head == null) {
            return 0;
        }

        Technician current = this.head;
        int size = 1;

        do {
            size++;
            current = current.getNext();
        } while (current != head);

        return size;
    }



    /**
     * toString override for Technician objects
     * @return String representation of Technician
     */
    @Override
    public String toString() {
        return super.toString() + "[rate: $" + ratePerVisit + ".00]";
    }
}