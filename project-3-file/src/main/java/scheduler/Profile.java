package scheduler;
import util.Date;
/**
 * This class represents a profile for a patient in the clinic system.
 * Implements Comparable to allow comparison of profiles based on last name,
 * first name, and DOB in that order
 * The profile comparison is used to order patients in lists, and the class
 * includes utility methods for equality checking and string representation
 * @author Keshav Dave, Daniel Watson
 */
public class Profile implements Comparable <Profile> {
    // Variables
    private String fname;
    private String lname;
    private Date   dob;

    /**
     * Constructor for the Profile class
     * Initializes the profile with the given first name, last name, and date of birth
     * @param fname the first name of the patient
     * @param lname the last name of the patient
     * @param dob   the date of birth of the patient
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Getter method for the patient's first name
     * @return the first name of the patient
     */
    public String getfname() {
        return this.fname;
    }

    /**
     * Getter method for the patient's last name
     * @return the last name of the patient
     */
    public String getlname() {
        return this.lname;
    }

    /**
     * Getter method for the patient's date of birth
     * @return the date of birth of the patient
     */
    public Date getDOB() {
        return this.dob;
    }

    /**
     * Checks if this profile is equal to another object
     * Two profiles are considered equal if they have the same first name,
     * last name, and date of birth
     * Overridden method
     * @param obj the object to compare with
     * @return true if the profiles are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass()){
            return false;}

        Profile profile = (Profile) obj;
        return this.fname.equalsIgnoreCase(profile.fname) && lname.equalsIgnoreCase(profile.lname) && dob.equals(profile.dob);
    }

    /**
     * Compares this profile to another profile for ordering
     * The comparison is done based on last name, first name, and date of
     * birth in that order
     * Overridden method
     * @param profile the profile to compare to
     * @return a negative integer, zero, or a positive integer as this
     * profile is less than, equal to, or greater than the specified profile
     */
    @Override
    public int compareTo(Profile profile) {
        if (this.lname.compareTo(profile.lname) == 0) {
            if ((this.fname.compareTo(profile.fname)) == 0) {
                return (this.dob.compareTo(profile.dob));
            }
            if ((this.fname.compareTo(profile.fname)) > 0){
                return 1;
            }
            if ((this.fname.compareTo(profile.fname)) < 0){
                return -1;
            }
        }
        if (this.lname.compareTo(profile.lname) > 0) {
            return 1;
        }
        if (this.lname.compareTo(profile.lname) < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * Returns a string representation of the profile
     * The string includes the patient's first name, last name, and date of
     * birth
     * Overridden method
     * @return a string representing the profile
     */
    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob;
    }
}