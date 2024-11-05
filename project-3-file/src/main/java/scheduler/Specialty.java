package scheduler;
/**
 * This enum represents different medical specialties in the clinic system
 * Each specialty has a name and a charge associated with it
 * The enum provides methods to retrieve the charge for each specialty,
 * and it can be extended to return the specialty name as a string if needed
 * @author Keshav Dave, Daniel Watson
 */
public enum Specialty {
    // Enums
    SPEC1("PEDIATRICIAN", 300),
    SPEC2("ALLERGIST", 350),
    SPEC3("FAMILY", 250);

    // Instance variables
    private final String name; // Name of the specialty
    private final int charge;

    /**
     * Constructor for the Specialty enum
     * Initializes the specialty with a name and a charge
     * @param specialty the name of the specialty (e.g., Pediatrician,
     *                  Allergist, Family)
     * @param charge the charge associated with the specialty
     */
    Specialty(String specialty, int charge) {
        this.name = specialty; // Store the specialty name
        this.charge = charge;
    }

    /**
     * Getter method for the charge associated with the specialty
     * @return the charge for the specialty
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Returns a formatted string representation of the specialty
     * Overridden method
     * @return a string representing the specialty
     */
    @Override
    public String toString() {
        return switch (this) {
            case SPEC1 -> "PEDIATRICIAN";
            case SPEC2 -> "ALLERGIST";
            case SPEC3 -> "FAMILY";
        };
    }

    /**
     * Returns a Specialty enum constant from a string input.
     * This method is case-insensitive.
     * @param specialtyString the string representation of the specialty
     * @return the corresponding Specialty enum constant
     * @throws IllegalArgumentException if no matching Specialty is found
     */
    public static Specialty valueOfSpecialty(String specialtyString) {
        for (Specialty specialty : Specialty.values()) {
            // Check if the input string matches the name of the specialty
            if (specialty.name.equalsIgnoreCase(specialtyString)) {
                return specialty;
            }
        }
        throw new IllegalArgumentException("No Specialty constant found for input: "
                + specialtyString);
    }
}