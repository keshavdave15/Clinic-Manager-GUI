package util;
import java.util.Calendar;

/**
 * This class represents a date and provides methods for date validation,
 * comparison, and formatting. It includes utility methods for checking
 * leap years, determining if a date is valid, checking if a date is within
 * six months from today, and comparing dates
 * @author Keshav Dave, Danny Watson
 */
public class Date implements Comparable<Date> {
    // Constants
    public static final int MAX_VALIDITY_AMOUNT = 6;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int MAX_MONTHS = 12;
    public static final int DAYS_IN_JANUARY = 31;
    public static final int DAYS_IN_FEBRUARY = 28; // Non-leap year by default
    public static final int DAYS_IN_LEAP_FEBRUARY = 29;
    public static final int DAYS_IN_MARCH = 31;
    public static final int DAYS_IN_APRIL = 30;
    public static final int DAYS_IN_MAY = 31;
    public static final int DAYS_IN_JUNE = 30;
    public static final int DAYS_IN_JULY = 31;
    public static final int DAYS_IN_AUGUST = 31;
    public static final int DAYS_IN_SEPTEMBER = 30;
    public static final int DAYS_IN_OCTOBER = 31;
    public static final int DAYS_IN_NOVEMBER = 30;
    public static final int DAYS_IN_DECEMBER = 31;
    public static final int[] DAYS_IN_MONTH = {
            DAYS_IN_JANUARY,
            DAYS_IN_FEBRUARY,
            DAYS_IN_MARCH,
            DAYS_IN_APRIL,
            DAYS_IN_MAY,
            DAYS_IN_JUNE,
            DAYS_IN_JULY,
            DAYS_IN_AUGUST,
            DAYS_IN_SEPTEMBER,
            DAYS_IN_OCTOBER,
            DAYS_IN_NOVEMBER,
            DAYS_IN_DECEMBER
    };

    // Variables
    private int year;
    private int month;
    private int day;

    /**
     * Constructs a Date object with the given month, day, and year
     * @param month The month of the date
     * @param day The day of the date
     * @param year The year of the date
     */
    public Date(int month, int day, int year) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Checks whether the current date is valid, considering leap years
     * Is tested in JUnit class
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        if (this.month > MAX_MONTHS || this.month < 1)
            return false;
        if (this.isLeapYear()) {
            if (this.month == Calendar.FEBRUARY + 1)
                return this.day > 0 && this.day <= DAYS_IN_LEAP_FEBRUARY;
        }
        return this.day > 0 && this.day <= DAYS_IN_MONTH[this.month - 1];
    }

    /**
     * Checks whether the date is not today
     * @return true if the date is not today, false otherwise
     */
    public boolean isNotToday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (this.year != year)
            return true;
        if (this.month != month)
            return true;
        return this.day != day;
    }

    /**
     * Checks whether the date is after today
     * @return true if the date is after today, false otherwise
     */
    public boolean isAfterToday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (this.year > year)
            return true;
        if (this.month > month && this.year == year)
            return true;
        if (this.month == month && this.year == year)
            return this.day > day;

        return false;
    }

    /**
     * Checks whether the date is within six months from today
     * @return true if the date is within six months, false otherwise
     */
    public boolean withinSixMonths() {
        Calendar calendar = Calendar.getInstance();
        Calendar targetDate = Calendar.getInstance();
        targetDate.set(this.year, this.month - 1, this.day);
        calendar.add(Calendar.MONTH, MAX_VALIDITY_AMOUNT);

        return targetDate.before(calendar);
    }

    /**
     * Checks whether the date is not a weekend
     * @return true if the date is not a Saturday or Sunday, false otherwise
     */
    public boolean isNotWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month - 1, this.day);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }


    /**
     * Determines if the current year is a leap year
     * @return true if the year is a leap year, false otherwise
     */
    public boolean isLeapYear() {
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0) {
                return this.year % QUATERCENTENNIAL == 0;
            }
            return true;
        }
        return false;
    }

    /**
     * simple getter method
     * @return The year of date
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the string representation of the date in the format MM/DD/YYYY
     * @return The formatted string of the date
     */
    @Override
    public String toString() {
        return String.format("%d/%d/%d", month, day, year);
    }

    /**
     * Compares the current date with another date
     * @param date The other date to compare
     * @return -1, 0, or 1 depending on whether the current date is before,
     * equal to, or after the other date
     */
    @Override
    public int compareTo(Date date) {
        if (this.year != date.year) {
            return Integer.compare(this.year, date.year);
        } else if (this.month != date.month) {
            return Integer.compare(this.month, date.month);
        } else if (this.day != date.day) {
            return Integer.compare(this.day, date.day);
        }
        return 0;
    }

    /**
     * Checks if the current date is equal to another date
     * @param date The object to compare with
     * @return true if the dates are equal, false otherwise
     */
    @Override
    public boolean equals(Object date) {
        if (this == date) {
            return true;
        }
        if (date == null || getClass() != date.getClass()) {
            return false;
        }
        Date compareDate = (Date) date;

        return this.year == compareDate.year && this.month ==
                compareDate.month && this.day == compareDate.day;
    }
}