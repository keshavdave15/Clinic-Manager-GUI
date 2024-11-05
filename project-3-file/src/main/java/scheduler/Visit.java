package scheduler;


/**
 * This class represents a visit in the clinic system
 * Each visit is associated with an appointment and is part of a linked list
 * of visits
 * A visit contains an appointment and a reference to the next visit in the
 * list
 * @author Keshav Dave, Daniel Watson
 */
public class Visit {
    // Instance variables
    private final Appointment appointment;
    private Visit next;

    /**
     * Constructor for the Visit class
     * Initializes the visit with an appointment and sets the next visit to
     * null
     * @param appointment the appointment associated with this visit
     */
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null; //there are no other visits in the list yet.
    }

    /**
     * Getter method for the appointment associated with this visit
     * @return the appointment for this visit
     */
    public Appointment getAppointment() {
        return this.appointment;
    }
    /**
     * Getter method for the next visit
     * @return the Visit obj for next visit
     */
    public Visit getNext() {
        return this.next;
    }

    /**
     * Setter method to set the next visit in the linked list
     * @param next the next visit to link to this one
     */
    public void setNext(Visit next) {
        this.next = next;
    }
}