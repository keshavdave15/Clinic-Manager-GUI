package scheduler;
/**
 * The Person class represents a person in the clinic system with a profile that contains
 * personal details such as full name, date of birth, and related charges.
 * Has the .equals(), .toString(), and .compareTo() methods the can be used by children classes
 * Parent class for Provider and Patient
 * @author Keshav Dave, Daniel Watson
 */
public class Person implements Comparable<Person>{
    // Variables
    protected Profile profile;

    /**
     * Constructor for the Person class
     * Initializes profile with the profile sent in
     * @param profile associated with this Person
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Getter method for the profile variable
     * @return profile object of this person
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Returns a string representation of the patient
     * The string includes the patient's full name, date of birth, and the
     * total charge
     * Overridden method
     * @return a string representing the patient
     */
    @Override
    public String toString() {
        return this.profile.toString();
    }

    /**
     * Returns whether the People are equivalent
     * Overridden method
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if ( getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
        return this.profile.equals(person.getProfile());
    }

    /**
     * Compares this Person to another person based on their profile
     * comparison
     * Overridden method
     * @param person the patient to compare with
     * @return a negative integer, zero, or a positive integer as this
     * patient's charge is less than, equal to, or greater than the
     * specified patient's charge
     */
    @Override
    public int compareTo(Person person) {
        return this.profile.compareTo(person.getProfile());
    }
}