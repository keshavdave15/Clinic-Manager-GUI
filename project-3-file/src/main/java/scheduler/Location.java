package scheduler;

/**
 * This class represents various clinic locations, each with a city, county,
 * and zip code
 * It provides methods to retrieve the city, county, and zip code for a
 * location, and overrides the toString method to return a formatted string
 * for each of the locations
 * @author Keshav Dave, Daniel Watson
 */
public enum Location {
    // Enums
    BRIDGEWATER( "Somerset", "08807"),
    EDISON("Middlesex", "08817"),
    PISCATAWAY("Middlesex", "08854"),
    PRINCETON("Mercer", "08542"),
    MORRISTOWN("Morris", "07960"),
    CLARK("Union", "07066");

    // Variables
    private final String county;
    private final String zip;

    /**
     * Constructor to initialize the Location enum instance
     * @param county the county of the location
     * @param zip    the zip code of the location
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Returns a formatted string representation of the location, including
     * the city, county, and zip code
     * Overridden method
     * @return a string representing the location in format CITY, COUNTY ZIP
     */
    @Override
    public String toString() {
        return switch (this) {
            case BRIDGEWATER -> "BRIDGEWATER, Somerset 08807";
            case EDISON -> "EDISON, Middlesex 08817";
            case PISCATAWAY -> "PISCATAWAY, Middlesex 08854";
            case PRINCETON -> "PRINCETON, Mercer 08542";
            case MORRISTOWN -> "MORRISTOWN, Morris 07960";
            case CLARK -> "CLARK, Union 07066";
        };
    }

    /**
     * Getter method for a location type that exists in the enum
     * @return the location verified that it exists
     */
    public static Location valueOfLocation(String locationString) {
        for (Location location : Location.values()) {
            // Check if the input string matches the city name of the location
            if (location.getCity().equalsIgnoreCase(locationString)) {
                return location;
            }
        }
        throw new IllegalArgumentException("No Location constant found for " +
                "input: " + locationString);
    }

    /**
     * Getter method for the city for the given location
     * @return the city as a string
     */
    public String getCity() {
        return switch (this) {
            case BRIDGEWATER -> "BRIDGEWATER";
            case EDISON -> "EDISON";
            case PISCATAWAY -> "PISCATAWAY";
            case PRINCETON -> "PRINCETON";
            case MORRISTOWN -> "MORRISTOWN";
            case CLARK -> "CLARK";
        };
    }

    /**
     * Gets the zip code for the given location.
     *
     * @return the zip code as a string
     */
    public String getZip() {
        return zip;
    }

    /**
     * Getter method for the county for the given location
     * @return the county as a string
     */
    public String getCounty() {
        return county;
    }
}