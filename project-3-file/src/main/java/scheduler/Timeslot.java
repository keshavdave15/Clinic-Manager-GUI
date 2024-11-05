package scheduler;

/**
 * The Timeslot class represents specific times of the day and supports
 * comparison, string representation, and retrieval of specific timeslots
 * by their number. It also provides methods to convert times to string
 * formats and to compare timeslots
 * @author Keshav Dave, Daniel Watson
 */
public class Timeslot implements Comparable<Timeslot>{
    // Constants
    private static final int SLOT_ONE = 1;
    private static final int SLOT_TWO = 2;
    private static final int SLOT_THREE = 3;
    private static final int SLOT_FOUR = 4;
    private static final int SLOT_FIVE = 5;
    private static final int SLOT_SIX = 6;
    private static final int SLOT_SEVEN = 7;
    private static final int SLOT_EIGHT = 8;
    private static final int SLOT_NINE = 9;
    private static final int SLOT_TEN = 10;
    private static final int SLOT_ELEVEN = 11;
    private static final int SLOT_TWELVE = 12;
    private static final int NOONTIME = 12;

    //Variables
    private static Timeslot tslot1 = new Timeslot(9, 0);
    private static Timeslot tslot2 = new Timeslot(9, 30);
    private static Timeslot tslot3 = new Timeslot(10,0);
    private static Timeslot tslot4 = new Timeslot(10,30);
    private static Timeslot tslot5 = new Timeslot(11,0);
    private static Timeslot tslot6 = new Timeslot (11, 30);
    private static Timeslot tslot7 = new Timeslot(14, 0);
    private static Timeslot tslot8 = new Timeslot(14,30);
    private static Timeslot tslot9 = new Timeslot(15,0);
    private static Timeslot tslot10 = new Timeslot(15,30);
    private static Timeslot tslot11 = new Timeslot(16,0);
    private static Timeslot tslot12 = new Timeslot(16, 30);
    private int hour;
    private int minute;

    /**
     * Constructor for Timeslot
     * @param hour the hour for the timeslot (in 24-hour format)
     * @param minute the minute for the timeslot
     */
    Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Returns the string representation of the timeslot in the format hh:mm AM/PM
     * @return formatted timeslot string
     */
    @Override
    public String toString() {
        if(this.hour <= NOONTIME){
            return this.hour + ":" + String.format("%02d", this.minute) + " AM";
        }
        else{
            int afternoonHour = this.hour - NOONTIME;
            return afternoonHour + ":" + String.format("%02d", this.minute) + " PM";
        }

    }

    /**
     * Retrieves a static predefined Timeslot object based on a number
     * @param number the number of the timeslot (1 to 12)
     * @return the corresponding Timeslot object
     */
    public static Timeslot getTimeslotByNumber(int number){
        return switch (number) {
            case SLOT_ONE -> tslot1;
            case SLOT_TWO -> tslot2;
            case SLOT_THREE -> tslot3;
            case SLOT_FOUR -> tslot4;
            case SLOT_FIVE -> tslot5;
            case SLOT_SIX -> tslot6;
            case SLOT_SEVEN -> tslot7;
            case SLOT_EIGHT -> tslot8;
            case SLOT_NINE -> tslot9;
            case SLOT_TEN -> tslot10;
            case SLOT_ELEVEN -> tslot11;
            case SLOT_TWELVE -> tslot12;

            default -> throw new IllegalArgumentException(number + " is not a valid time slot.");
        };
    }

    /**
     * Finds a matching Timeslot based on a formatted time string
     * The formatted time string should be in the format "hh:mm AM/PM"
     * The method converts the time to a 24-hour format, then searches
     * for an equivalent Timeslot in a predefined set of timeslots
     * @param formattedTime The formatted time string (e.g., "10:30 AM")
     * @return The matching Timeslot if found; otherwise, null
     */
    public static Timeslot findTimeslot(String formattedTime) {
        String[] timeParts = formattedTime.split(":");

        String hourPart = timeParts[0].trim();
        String minutePart = timeParts[1].trim();

        boolean isAM = minutePart.toUpperCase().contains("AM");
        boolean isPM = minutePart.toUpperCase().contains("PM");

        minutePart = minutePart.replaceAll("[^0-9]", "");
        int hour = Integer.parseInt(hourPart);
        int minute = Integer.parseInt(minutePart);

        if (isPM && hour != 12) {
            hour += 12;
        }
        Timeslot targetTimeslot = new Timeslot(hour, minute);
        for (int i = 1; i <= 12; i++) {
            if (getTimeslotByNumber(i).equals(targetTimeslot)) {
                return getTimeslotByNumber(i);
            }
        }
        return null;
    }

    /**
     * Converts timeslot number to a string that prints during the error message
     * @param timeslot the timeslot number
     * @return the error string for invalid timeslot
     */
    public String toErrorString(String timeslot){
        return "slot " + timeslot;
    }

    /**
     * Compares this timeslot to another timeslot
     * @param timeslot the other timeslot to compare to
     * @return -1 if this timeslot is earlier, 1 if later, 0 if equal
     */
    @Override
    public int compareTo(Timeslot timeslot) {
        if(this.hour == timeslot.hour)
            return Integer.compare(this.minute, timeslot.minute);
        else
            return Integer.compare(this.hour, timeslot.hour);
    }

    /**
     * Checks if this timeslot is equal to another object (only equal if other object is a timeslot)
     * @param timeslot the timeslot that is compared with
     * @return true if the timeslots are equal, false otherwise
     */
    @Override
    public boolean equals(Object timeslot) {
        return this.compareTo((Timeslot) timeslot) == 0;
    }
}