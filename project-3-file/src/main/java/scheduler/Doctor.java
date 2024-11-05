package scheduler;

/**
 * The Doctor class is an abstract subclass of Provider
 * It represents a doctor with a specialty and a unique National Provider
 * Identification (NPI) number
 * The rate per visit is based on the doctor's specialty
 * This class implements the rate() method, which calculates the charge
 * based on the doctor's specialty
 * @author Keshav Dave, Danny Watson
 */
public class Doctor extends Provider {
    // Variables
    private Specialty specialty;
    private String npi;

    /**
     * Constructor for the Doctor class
     * Initializes the doctor with the given profile, location, specialty,
     * and NPI
     * @param profile The profile of the doctor
     * @param location The location of the doctor's practice
     * @param specialty The specialty of the doctor, which determines the
     *                  charge rate.
     * @param npi  unique NPI number for the doctor
     */
    public Doctor(Profile profile, Location location, Specialty specialty,
                  String npi) {
        super(profile, location); // Call the constructor of the Provider class
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Getter method for the doctor's specialty
     * @return The doctor's specialty.
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Getter method for the doctor's NPI
     * @return The doctor's National Provider Identification (NPI).
     */
    public String getNpi() {
        return npi;
    }

    /**
     * Implementation of the abstract rate() method from the Provider class
     * Overridden method
     * @return The charge for the doctor's specialty.
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    /**
     * toString override for Doctor objects
     * @return String representation of Doctor
     */
    @Override
    public String toString() {
        return super.toString() + "[" + specialty.toString() + ", #" + npi +
                "]";
    }
}