package scheduler;
import util.Date;
/**
 * The Imaging subclass represents a type of appointment for radiology
 * It extends the Appointment class and adds an attribute for the
 * radiology room where the imaging appointment takes place
 * @author Keshav Dave, Danny Watson
 */
public class Imaging extends Appointment {
    private final Radiology room;
    /**
     * Constructor for the Imaging class
     * Initializes the Imaging appointment with the given date, timeslot,
     * patient, provider, and radiology
     * @param date The date of the imaging appointment
     * @param timeslot The timeslot of the imaging appointment
     * @param patient The patient for the imaging appointment
     * @param provider The provider for the imaging appointment
     * @param room The radiology room where the appointment takes place
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    /**
     * Getter method for the radiology room
     * @return The radiology room for the imaging appointment
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Overridden toString() method to include the room information in the output
     * @return A string representation of the imaging appointment, including the room
     */
    @Override
    public String toString() {
        return super.toString() + "[" + room + "]";
    }
}