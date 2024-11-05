package scheduler;
import util.Date;

/**
 * This class represents an appointment in the clinic system
 * It includes details such as the date, timeslot, patient profile, and
 * provider
 * Implements Comparable to allow for appointment comparison based on the
 * date
 * @author Keshav Dave, Daniel Watson
 */
public class Appointment implements Comparable <Appointment> {
    // Variables
    protected Date date;
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * Parameterized constructor for the Appointment class
     * Initializes the instance variables
     * @param date the date for the appointment
     * @param timeslot the timeslot for the appointment
     * @param patient the patient for the appointment
     * @param provider the provider for the appointment
     */
    public Appointment(Date date, Timeslot timeslot, Person patient,
                       Person provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Getter method for the appointment profile
     * @return Profile object patient
     */
    public Person getPatient() {
        return this.patient;
    }

    /**
     * Getter method for the appointment date
     * @return Date object date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Getter method for the appointment timeslot
     * @return Timeslot object timeslot
     */
    public Timeslot getTimeslot() {
        return this.timeslot;
    }

    /**
     * Getter method for the appointment provider
     * @return Provider object provider
     */
    public Person getProvider() {
        return this.provider;
    }

    /**
     * Overridden toString method to format the appointment details properly
     * @return a string query of appointment details
     */
    @Override
    public String toString() {
        return this.date.toString() + " " + this.timeslot.toString() + " " +
                this.patient.toString() + " " + this.provider.toString();
    }

    /**
     * Overridden equals method to compare an appointment to given object and
     * find out if they are equal
     * @param obj Object type to be compared to this appointment
     * @return true if the abject and appointment are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Appointment appointment = (Appointment) obj; // Cast Object to
        // Appointment

        return this.date.equals(appointment.date) &&
                this.timeslot.equals(appointment.timeslot) &&
                this.patient.equals(appointment.patient);
    }

    /**
     * Overridden compareTo method to compare an appointment to another
     * appointment and find out which is sooner
     * Calls the compareTo method in Date class
     * @param appointment Appointment type to be compared to this
     * appointment
     * @return a negative integer, zero, or a positive integer as this Appointment
     * Date is less than, equal to, or greater than the specified Appointment
     * Date
     */
    @Override
    public int compareTo(Appointment appointment) {
        //call the compareTo() in Date.
        if (this.date.compareTo(appointment.date) > 0)
            return 1;
        if (this.date.compareTo(appointment.date) < 0)
            return -1;
        return 0;
    }
}