package scheduler;

/**
 * This class represents a patient in the clinic system
 * Each patient has a profile and a linked list of visits (appointments)
 * The class allows adding new visits, calculating total charges for visits,
 * and comparing patients based on the total charge for their visits
 * The patient has a profile that contains personal details, and each visit
 * contains information about an appointment and the provider's specialty
 * The visits are stored as a linked list, and charges are calculated by
 * traversing the list of visits
 * @author Keshav Dave, Daniel Watson
 */
public class Patient extends Person {
    // Variables
    private Visit visit;

    /**
     * Constructor for the Patient class
     * Initializes the patient with a profile and visit list
     * @param profile the profile object containing patient details
     */
    public Patient(Profile profile) {
        super(profile);
    }

    /**
     * Calculates the total charge for all visits associated with the patient
     * Traverses the linked list of visits and sums up the charges based on
     * the provider's specialty
     * @return the total charge for all visits
     */
    public int charge() {
        int totalCharge = 0;
        Visit currentVisit = this.visit;

        // Traverse the linked list of visits
        while (currentVisit != null) {
            // summations charge
            totalCharge += ((Provider) currentVisit.getAppointment().getProvider()).rate();

            // Move to the next visit in the list
            currentVisit = currentVisit.getNext();
        }

        return totalCharge;
    }

    /**
     * Adds a new visit (appointment) to the patient's list of visits
     * If there are no visits, the new visit is set as the first visit
     * Otherwise, the new visit is added at the end of the linked list
     * @param appointment the appointment to be added as a visit*/
    public void addVisit(Appointment appointment) {
        Visit newVisit = new Visit(appointment);

        if (visit == null) {
            visit = newVisit;
        }
        else {
            Visit curr = visit;
            while (curr.getNext() != null) {
                curr = curr.getNext();
            }
            curr.setNext(newVisit); // Add the new visit at the end
        }
    }

    /**
     * Getter method for the patient's profile
     * @return the Profile of the patient
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Getter method for the patient's visits
     * @return the Profile of the patient
     */
    public Visit getVisit() {
        return this.visit;
    }

    /**
     * Checks if this patient is equal to another object
     * Two patients are considered equal if they have the same visits
     * and the super method checks out
     * Overridden method
     * @param patient the object to compare with
     * @return true if the patients are equal, false otherwise
     */
    @Override
    public boolean equals(Object patient) {
        if (this == patient) {
            return true;
        }
        if ( getClass() != patient.getClass()) {
            return false;
        }
        Patient other = (Patient) patient;
        return this.profile.equals(other.profile);
    }
}