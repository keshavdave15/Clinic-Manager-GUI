package util;
import scheduler.*;
/**
 * The Sort class provides utility methods for sorting various types of lists.
 * This class contains only static methods, so it does not need to be instantiated.
 * The methods in this class sort lists of appointments and providers. For appointments,
 * sorting can be done by different keys (such as patient name, date, or time).
 * The sorting is done in-place, meaning that the provided list is directly modified.
 * Usage:
 * - Sort a list of appointments by a given key (e.g., by patient name or date).
 * - Sort a list of providers based on their name or other attributes.
 * Since the methods are static, they can be called directly using:
 * Sort.appointment(list, key);
 * Sort.provider(list);
 * @author Keshav Dave, Daniel Watson
 */
public class Sort {
    /**
     *  Method to sort appointments by a certain key
     *  Call this method when dealing with the PA PP or PL methods
     * 'A' key for PA command
     * 'P' key for PP command
     * 'L' key for PL command
     * 'S' key for PS command
     * 'O' and 'I' commands expect a list of imaging and office appointments,
     * and they will sort it correctly by the location logic
     * The appointments are sorted by bubble sort
     * WARNING: Lines must exceed the 78 column count limit in this method
     * @param appointments list of Appointments
     * @param key char key to choose which way to sort appointments
     */
    public static void appointment(List<Appointment> appointments, char key) {
        switch (key) {
            case 'A' -> sortByDate(appointments);
            case 'P' -> sortByPatient(appointments);
            case 'L', 'O', 'I' -> sortByLocation(appointments);
            case 'S' -> sortByStatements(appointments);
        }
    }

    /**
     * Method to sort providers by provider profile
     * Call this method when dealing with the PC command or when
     * needing to sort a Provider list
     * The appointments are sorted by bubble sort
     * WARNING: Lines must exceed the 78 column count limit in this method
     * @param providers list of Providers
     */
    public static void provider(List<Provider> providers) {
        int size = providers.size();

        if (size == 0) {
            return;
        }

        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                // Compare by last name first
                int lastNameComparison = providers.get(j).getProfile().getlname()
                        .compareToIgnoreCase(providers.get(j + 1).getProfile().getlname());

                if (lastNameComparison > 0) {
                    Provider temp = providers.get(j);
                    providers.set(j, providers.get(j + 1));
                    providers.set(j + 1, temp);
                    swapped = true; // A swap was made
                }
                // If last names are the same, compare by first name
                else if (lastNameComparison == 0) {
                    int firstNameComparison = providers.get(j).getProfile().getfname()
                            .compareToIgnoreCase(providers.get(j + 1).getProfile().getfname());

                    if (firstNameComparison > 0) {
                        Provider temp = providers.get(j);
                        providers.set(j, providers.get(j + 1));
                        providers.set(j + 1, temp);
                        swapped = true;
                    }
                }
            }

            // If no swaps were made during the inner loop, the list is already sorted
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Method to sort appointments sorted by date and timeslot, then
     * by provider name
     * 'A' key for PA command
     * @param appointments list of Appointments
     */
    public static void sortByDate(List<Appointment> appointments) {
        int size = appointments.size();

        // If list is empty do nothing
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                // Compare by date
                if (appointments.get(j).getDate().compareTo(appointments.get(i).getDate()) < 0) {
                    // Swap appointments[i] and appointments[j] if they are out of order
                    Appointment temp = appointments.get(j);
                    appointments.set(j, appointments.get(i));
                    appointments.set(i, temp);
                }

                // If dates are the same, compare by timeslot
                else if (appointments.get(j).getDate().compareTo(appointments.get(i).getDate()) == 0) {
                    if (appointments.get(j).getTimeslot().compareTo(appointments.get(i).getTimeslot()) < 0) {
                        // Swap appointments[i] and appointments[j] if timeslots are out of order
                        Appointment temp = appointments.get(j);
                        appointments.set(j, appointments.get(i));
                        appointments.set(i, temp);
                    }

                    // If timeslots are the same, compare by provider's last name
                    else if (appointments.get(j).getTimeslot().compareTo(appointments.get(i).getTimeslot()) == 0) {
                        if ((((Provider) appointments.get(j).getProvider()).getProfile().getlname().compareTo(((Provider) appointments.get(i).getProvider()).getProfile().getlname()) < 0)) {
                            // Swap appointments[i] and appointments[j] if provider names are out of order
                            Appointment temp = appointments.get(j);
                            appointments.set(j, appointments.get(i));
                            appointments.set(i, temp);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to sort appointments sorted by patient profile, then by date
     * and timeslot
     * 'P' key for PP command
     * @param appointments list of Appointments
     */
    public static void sortByPatient(List<Appointment> appointments) {
        int size = appointments.size();

        // If list is empty do nothing
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                // Compare by patient
                if (appointments.get(j).getPatient().compareTo(appointments.get(i).getPatient()) < 0) {
                    // Swap appointments[i] and appointments[j] if they are out of order
                    Appointment temp = appointments.get(j);
                    appointments.set(j, appointments.get(i));
                    appointments.set(i, temp);
                }

                // if patients are the same, sort by date
                else if (((Provider) appointments.get(j).getProvider()).getLocation().compareTo(((Provider) appointments.get(i).getProvider()).getLocation()) == 0) {
                    if (appointments.get(j).getDate().compareTo(appointments.get(i).getDate()) < 0) {
                        // Swap appointments[i] and appointments[j] if they are out of order
                        Appointment temp = appointments.get(j);
                        appointments.set(j, appointments.get(i));
                        appointments.set(i, temp);
                    }

                    // if dates are the same, sort by timeslot
                    else if (appointments.get(j).getDate().compareTo(appointments.get(i).getDate()) == 0) {
                        if (appointments.get(j).getTimeslot().compareTo(appointments.get(i).getTimeslot()) < 0) {
                            // Swap appointments[i] and appointments[j] if they are out of order
                            Appointment temp = appointments.get(j);
                            appointments.set(j, appointments.get(i));
                            appointments.set(i, temp);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to sort appointments sorted by county, then by date and
     * timeslot
     * 'L' key for PL command
     * @param appointments list of Appointments
     */
    public static void sortByLocation(List<Appointment> appointments) {
        int size = appointments.size();

        if (size == 0) {
            return;
        }

        // Sort by county (alphabetically, ignoring case), then date, then timeslot
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                // Compare by county (case-insensitive)
                int countyComparison = ((Provider) appointments.get(j).getProvider()).getLocation().getCounty()
                        .compareToIgnoreCase(((Provider) appointments.get(i).getProvider()).getLocation().getCounty());

                if (countyComparison < 0) {
                    // Swap appointments if out of order by county
                    Appointment temp = appointments.get(j);
                    appointments.set(j, appointments.get(i));
                    appointments.set(i, temp);
                }
                // If counties are the same, compare by date
                else if (countyComparison == 0) {
                    int dateComparison = appointments.get(j).getDate().compareTo(appointments.get(i).getDate());
                    if (dateComparison < 0) {
                        // Swap appointments if out of order by date
                        Appointment temp = appointments.get(j);
                        appointments.set(j, appointments.get(i));
                        appointments.set(i, temp);
                    }
                    // If dates are the same, compare by timeslot
                    else if (dateComparison == 0) {
                        if (appointments.get(j).getTimeslot().compareTo(appointments.get(i).getTimeslot()) < 0) {
                            // Swap appointments if out of order by timeslot
                            Appointment temp = appointments.get(j);
                            appointments.set(j, appointments.get(i));
                            appointments.set(i, temp);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to sort appointments by the patient's last name first,
     * then by the first name, and finally by the appointment date.
     * 'S' key for PS command
     * @param appointments list of Appointments
     */
    public static void sortByStatements(List<Appointment> appointments) {
        int size = appointments.size();

        if (size == 0) {
            return;
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                Patient patient1 = (Patient) appointments.get(i).getPatient();
                Patient patient2 = (Patient) appointments.get(j).getPatient();

                // Compare by patient's last name
                int lastNameComparison = patient1.getProfile().getlname()
                        .compareToIgnoreCase(patient2.getProfile().getlname());

                if (lastNameComparison > 0) {
                    Appointment temp = appointments.get(j);
                    appointments.set(j, appointments.get(i));
                    appointments.set(i, temp);
                }
                // If last names are the same, compare by first name
                else if (lastNameComparison == 0) {
                    int firstNameComparison = patient1.getProfile().getfname()
                            .compareToIgnoreCase(patient2.getProfile().getfname());

                    if (firstNameComparison > 0) {
                        Appointment temp = appointments.get(j);
                        appointments.set(j, appointments.get(i));
                        appointments.set(i, temp);
                    }
                    // If both first and last names are the same, compare by year
                    else if (firstNameComparison == 0) {
                        if (appointments.get(j).getDate().getYear() < appointments.get(i).getDate().getYear()) {
                            // Swap appointments if out of order by date
                            Appointment temp = appointments.get(j);
                            appointments.set(j, appointments.get(i));
                            appointments.set(i, temp);
                        }
                    }
                }
            }
        }
    }
}