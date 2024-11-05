package scheduler;

/**
 * This abstract class represents providers in the clinic
 * Has an abstract method to be implemented in its children, concrete classes
 * @author Keshav Dave, Daniel Watson
 */
public abstract class Provider extends Person {
    private final Location location;

    /**
     * Constructor for the Provider Object
     * Initializes the provider with a profile and location
     * @param profile the profile of the provider
     * @param location the location where the provider works
     */
    Provider(Profile profile, Location location) {
        super(profile);
        this.location = location;
    }

    /**
     * Getter method for the location of the provider
     * @return the location of the provider
     */
    public Location getLocation() {
        return location;
    }

    /**
     * toString override for Provider objects
     * @return String representation of Provider
     */
    @Override
    public String toString() {
        return "[" + super.toString() + ", " + location.toString() + "]";
    }

    /**
     * Abstract method to be implemented by concrete subclasses
     */
    public abstract int rate();
}