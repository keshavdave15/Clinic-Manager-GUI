package project3scheduler;

import scheduler.*;
import util.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import java.io.File;
import java.time.*;
import java.time.format.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for managing the Clinic Manager application. Handles initialization
 * of UI elements, validation of input fields, and event handling for scheduling
 * and rescheduling appointments.
 * Should not crash under not instance
 * Please run the window on full screen and do not resize window.
 * When rescheduling an imaging appointment, it automatically converts it to an office appointment
 * @author Danny Watson, Keshav Dave
 */
public class ClinicManagerController {
    // FXML variables
    @FXML
    private DatePicker appointmentDatePicker, dobPicker, originalAppointmentDate, rPatientBirthday;
    @FXML
    private TextField firstNameField, lastNameField, rPatientFirstName, rPatientLastName;
    @FXML
    private RadioButton officeVisitRadio, imagingServiceRadio;
    @FXML
    private ComboBox<String> timeslotComboBox, providerComboBox, currentTimeslot, newTimeslot;
    @FXML
    private Button scheduleButton, cancelButton, reschedule;
    @FXML
    private TextArea outputArea;
    @FXML
    private Label dateErrorLabel, firstNameErrorLabel, lastNameErrorLabel, appointmentDateErrorLabel,
            originalAppointmentDateErrorLabel, rFirstNameErrorLabel, rLastNameErrorLabel, rDateErrorLabel;
    @FXML
    private ListView<String> appointmentListView, statementListView;
    @FXML
    private TableView<Location> tbl_location;
    @FXML
    private TableColumn<Location, String> col_city, col_zip, col_county;

    // Variables
    private ToggleGroup toggleGroup;
    private List<Provider> providerList = new List<>();
    private List<Appointment> appointmentList = new List<>();
    private boolean birthdayValid, rBirthdayValid;
    private boolean appointmentValid, rAppointmentValid;

    /**
     * Initializes the UI components and event listeners for the Clinic Manager.
     */
    @FXML
    public void initialize() {
        // Initialize table
        Location[] locationsArray = {Location.BRIDGEWATER, Location.EDISON, Location.PISCATAWAY, Location.PRINCETON, Location.MORRISTOWN, Location.CLARK};
        ObservableList<Location> locations = FXCollections.observableArrayList(locationsArray);
        tbl_location.setItems(locations);
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_county.setCellValueFactory(new PropertyValueFactory<>("county"));
        col_zip.setCellValueFactory(new PropertyValueFactory<>("zip"));

        // Disables typing for the date pickers to avoid invalid date exceptions
        appointmentDatePicker.getEditor().setDisable(true);
        dobPicker.getEditor().setDisable(true);
        originalAppointmentDate.getEditor().setDisable(true);
        rPatientBirthday.getEditor().setDisable(true);

        // Display appointments
        showAppointments(appointmentList, appointmentListView);

        // Initialize toggle group first
        toggleGroup = toggleAppointmentType(officeVisitRadio, imagingServiceRadio);

        // Event listener setup now that toggleGroup is initialized
        setupEventListeners();
    }

    /**
     * Sets up the location table with city, county, and zip columns.
     */
    private void initializeLocationTable() {
        ObservableList<Location> locations = FXCollections.observableArrayList(Location.values());
        tbl_location.setItems(locations);
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_county.setCellValueFactory(new PropertyValueFactory<>("county"));
        col_zip.setCellValueFactory(new PropertyValueFactory<>("zip"));
    }

    /**
     * Sets up the event listeners for input validation and field updates.
     */
    private void setupEventListeners() {
        TextField appointmentDateEditor = appointmentDatePicker.getEditor();
        appointmentDateEditor.setOnKeyReleased(this::handleAppointmentDate);
        TextField rAppointmentDateEditor = originalAppointmentDate.getEditor();
        rAppointmentDateEditor.setOnKeyReleased(this::handleRAppointmentDate);

        firstNameField.setOnKeyReleased(this::checkFirstNameFieldEmpty);
        lastNameField.setOnKeyReleased(this::checkLastNameFieldEmpty);
        rPatientFirstName.setOnKeyReleased(this::checkRFirstNameFieldEmpty);
        rPatientLastName.setOnKeyReleased(this::checkRLastNameFieldEmpty);

        TextField dateEditor = dobPicker.getEditor();
        dateEditor.setOnKeyReleased(this::handleBirthday);
        TextField rDateEditor = rPatientBirthday.getEditor();
        rDateEditor.setOnKeyReleased(this::handleRBirthday);

        timeslotComboBox.setItems(createTimeslotList());
        providerComboBox.setItems(createProviderSettings(providerList));
        currentTimeslot.setItems(createTimeslotList());
        newTimeslot.setItems(createTimeslotList());

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
        timeslotComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
        providerComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> updateButtonState());

        rPatientFirstName.textProperty().addListener((observable, oldValue, newValue) -> updateRButtonState());
        rPatientLastName.textProperty().addListener((observable, oldValue, newValue) -> updateRButtonState());
        currentTimeslot.valueProperty().addListener((observable, oldValue, newValue) -> updateRButtonState());
        newTimeslot.valueProperty().addListener((observableValue, oldValue, newValue) -> updateRButtonState());
    }

    /**
     * Configures ComboBoxes with necessary items for selection.
     */
    private void configureComboBoxes() {
        timeslotComboBox.setItems(createTimeslotList());
        providerComboBox.setItems(createProviderSettings(providerList));
        currentTimeslot.setItems(createTimeslotList());
        newTimeslot.setItems(createTimeslotList());
        toggleGroup = toggleAppointmentType(officeVisitRadio, imagingServiceRadio);
    }

    /**
     * Validates the birthday input field.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void handleBirthday(KeyEvent event) {
        try {
            String input = dobPicker.getEditor().getText();
            if (input != null) {
                LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy"));
                Date bday = new Date(date.getMonthValue(), date.getDayOfMonth(), date.getYear());
                birthdayValid = checkBirthdayValidity(bday, dobPicker, dateErrorLabel);
                updateButtonState();
            }
        } catch (DateTimeParseException e) {
            birthdayValid = false;
            setDateFieldInvalid(dobPicker, dateErrorLabel, "Invalid calendar date.");
            updateButtonState();
        }
    }

    /**
     * Validates the birthday input field for the rescheduled patient.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void handleRBirthday(KeyEvent event) {
        try {
            String in = rPatientBirthday.getEditor().getText();
            if (in != null) {
                LocalDate date = LocalDate.parse(in, DateTimeFormatter.ofPattern("M/d/yyyy"));
                Date birthday = new Date(date.getMonthValue(), date.getDayOfMonth(), date.getYear());
                rBirthdayValid = checkBirthdayValidity(birthday, rPatientBirthday, rDateErrorLabel);
                updateRButtonState();
            }
        } catch (DateTimeParseException e) {
            rBirthdayValid = false;
            setDateFieldInvalid(rPatientBirthday, rDateErrorLabel, "Invalid calendar date.");
            updateRButtonState();
        }
    }

    /**
     * Validates the appointment date for rescheduling.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void handleRAppointmentDate(KeyEvent event) {
        try {
            String in = originalAppointmentDate.getEditor().getText();
            if (in != null) {
                LocalDate localDate = LocalDate.parse(in, DateTimeFormatter.ofPattern("M/d/yyyy"));
                Date date = new Date(localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getYear());
                rAppointmentValid = checkAppointmentDateValidity(date, originalAppointmentDate, appointmentDateErrorLabel);
                updateButtonState();
            }
        } catch (DateTimeParseException e) {
            rAppointmentValid = false;
            setDateFieldInvalid(originalAppointmentDate, originalAppointmentDateErrorLabel, "Invalid calendar date.");
            updateButtonState();
        }
    }

    /**
     * Validates the appointment date input field.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void handleAppointmentDate(KeyEvent event) {
        try {
            String in = appointmentDatePicker.getEditor().getText();
            if (in != null) {
                LocalDate localDate = LocalDate.parse(in, DateTimeFormatter.ofPattern("M/d/yyyy"));
                Date date = new Date(localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getYear());
                appointmentValid = checkAppointmentDateValidity(date, appointmentDatePicker, appointmentDateErrorLabel);
                updateButtonState();
            }
        } catch (DateTimeParseException e) {
            appointmentValid = false;
            setDateFieldInvalid(appointmentDatePicker, appointmentDateErrorLabel, "Invalid calendar date.");
            updateButtonState();
        }
    }

    /**
     * Parses the birthday from DatePicker and validates it.
     */
    @FXML
    public void parseBirthday() {
        LocalDate date = dobPicker.getValue();
        if (date != null) {
            Date birthday = new Date(date.getMonthValue(), date.getDayOfMonth(), date.getYear());
            birthdayValid = checkBirthdayValidity(birthday, dobPicker, dateErrorLabel);
            updateButtonState();
        }
    }

    /**
     * Parses the rescheduled patient's birthday and validates it.
     */
    @FXML
    public void parseRBirthday() {
        LocalDate date = rPatientBirthday.getValue();
        if (date != null) {
            Date birthday = new Date(date.getMonthValue(), date.getDayOfMonth(), date.getYear());
            rBirthdayValid = checkBirthdayValidity(birthday, rPatientBirthday, rDateErrorLabel);
            updateRButtonState();
        }
    }

    /**
     * Parses and validates the appointment date.
     */
    @FXML
    public void parseAppointmentDate() {
        LocalDate localDate = appointmentDatePicker.getValue();
        if (localDate != null) {
            Date date = new Date(localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getYear());
            appointmentValid = checkAppointmentDateValidity(date, appointmentDatePicker, appointmentDateErrorLabel);
            updateButtonState();
        }
    }

    /**
     * Parses and validates the rescheduled appointment date.
     */
    @FXML
    public void parseRAppointmentDate() {
        LocalDate localDate = originalAppointmentDate.getValue();
        if (localDate != null) {
            Date date = new Date(localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getYear());
            rAppointmentValid = checkAppointmentDateValidity(date, originalAppointmentDate, originalAppointmentDateErrorLabel);
            updateRButtonState();
        }
    }

    /**
     * Checks if the first name field is empty and updates the field format.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void checkFirstNameFieldEmpty(KeyEvent event) {
        String fName = firstNameField.getText().trim();
        textFieldFormat(firstNameField, firstNameErrorLabel, fName.isEmpty());
    }

    /**
     * Checks if the rescheduled patient's first name field is empty and updates the field format.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void checkRFirstNameFieldEmpty(KeyEvent event) {
        String fName = rPatientFirstName.getText().trim();
        textFieldFormat(rPatientFirstName, rFirstNameErrorLabel, fName.isEmpty());
    }

    /**
     * Checks if the last name field is empty and updates the field format.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void checkLastNameFieldEmpty(KeyEvent event) {
        String lName = lastNameField.getText().trim();
        textFieldFormat(lastNameField, lastNameErrorLabel, lName.isEmpty());
    }

    /**
     * Checks if the rescheduled patient's last name field is empty and updates the field format.
     * @param event the KeyEvent triggered by user input
     */
    @FXML
    public void checkRLastNameFieldEmpty(KeyEvent event) {
        String lName = rPatientLastName.getText().trim();
        textFieldFormat(rPatientLastName, rLastNameErrorLabel, lName.isEmpty());
    }

    /**
     * Configures the ComboBox for appointment types and sets appropriate providers.
     */
    @FXML
    public void checkAppointmentType() {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        timeslotComboBox.setPromptText("Timeslot");
        timeslotComboBox.setItems(createTimeslotList());
        timeslotComboBox.setVisible(true);
        providerComboBox.setPromptText("Providers");

        if (selectedRadioButton == imagingServiceRadio) {
            providerComboBox.setPromptText("Imaging Service");
            providerComboBox.setVisible(true);
            ObservableList<String> imProviders = FXCollections.observableArrayList("CATSCAN", "ULTRASOUND", "XRAY");
            providerComboBox.setItems(imProviders);
        } else if (officeVisitRadio == selectedRadioButton) {
            providerComboBox.setVisible(true);
            providerComboBox.setPromptText("Providers");
            providerComboBox.setItems(createProviderSettings(providerList));
        }
    }

    /**
     * Shows the time slots for the timeslot ComboBox
     */
    @FXML
    public void showTimeslots() {
        currentTimeslot.setPromptText("Current Timeslot");
        currentTimeslot.setItems(createTimeslotList());
        currentTimeslot.setVisible(true);
        newTimeslot.setPromptText("New Timeslot");
        newTimeslot.setItems(createTimeslotList());
        newTimeslot.setVisible(true);
    }

    /**
     * Updates the state of the schedule and cancel buttons based on
     * input validation results.
     */
    private void updateButtonState() {
        boolean ifBirthdayEmpty = (dobPicker.getValue() == null);
        boolean ifDateEmpty = (appointmentDatePicker.getValue() == null);
        boolean ifFirstNameEmpty = firstNameField.getText().trim().isEmpty();
        boolean ifLastNameEmpty = lastNameField.getText().trim().isEmpty();
        boolean ifToggleGroupUnselected = (toggleGroup.getSelectedToggle() == null);
        boolean ifTimeslotEmpty = (timeslotComboBox.getValue() == null || timeslotComboBox.getValue().isEmpty());
        boolean ifProviderUnselected = (providerComboBox.getValue() == null || providerComboBox.getValue().isEmpty());
        RadioButton chosenRadioButton = (RadioButton) toggleGroup.getSelectedToggle();

        boolean shouldDisable = ifFirstNameEmpty || ifLastNameEmpty || ifBirthdayEmpty || ifDateEmpty
                || ifTimeslotEmpty || !birthdayValid || !appointmentValid;
        cancelButton.setDisable(shouldDisable);

        shouldDisable = ifFirstNameEmpty || ifLastNameEmpty || ifBirthdayEmpty || ifDateEmpty || ifTimeslotEmpty
                || !birthdayValid || !appointmentValid;
        if (chosenRadioButton == officeVisitRadio)
            shouldDisable = shouldDisable || ifProviderUnselected;
        else
            shouldDisable = shouldDisable || ifToggleGroupUnselected;

        scheduleButton.setDisable(shouldDisable);
    }

    /**
     * Schedules an appointment based on input fields and displays it in the output area.
     */
    @FXML
    public void scheduleAppointment() {
        LocalDate date = appointmentDatePicker.getValue();
        LocalDate date2 = dobPicker.getValue();
        String fName = firstNameField.getText();
        String lName = lastNameField.getText();
        Date appointmentDate = new Date(date.getMonthValue(), date.getDayOfMonth(), date.getYear());
        String timeslotString = timeslotComboBox.getValue();
        Timeslot timeslot = Timeslot.findTimeslot(timeslotString);
        Date birthday = new Date(date2.getMonthValue(), date2.getDayOfMonth(), date2.getYear());
        Profile patientsProfile = new Profile(fName, lName, birthday);

        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton == officeVisitRadio)
            dCommand(timeslot, appointmentDate, patientsProfile);
        if (selectedRadioButton == imagingServiceRadio)
            tCommand(timeslot, appointmentDate, patientsProfile);

        showAppointments(appointmentList, appointmentListView);
        clear();
    }

    /**
     * Command for scheduling an imaging appointment with a technician.
     * @param timeslot        the chosen appointment timeslot
     * @param aptDate         the chosen appointment date
     * @param patientProfile  the profile of the patient
     */
    private void tCommand(Timeslot timeslot, Date aptDate, Profile patientProfile) {
        Patient patient = new Patient(patientProfile);
        checkAppointmentExists(patientProfile, aptDate, timeslot, appointmentList);
        String imaging = providerComboBox.getValue();
        Radiology radiology = Radiology.valueOf(imaging.toUpperCase());
        Imaging appointment = findTechnicianAvailability(timeslot, aptDate, patient, radiology, appointmentList, providerList, outputArea);

        outputArea.setText(appointment + " booked.");
        appointmentList.add(appointment);
    }

    /**
     * Command for scheduling an office visit appointment with a doctor.
     * @param timeslot        the chosen appointment timeslot
     * @param aptDate         the chosen appointment date
     * @param patientProfile  the profile of the patient
     */
    private void dCommand(Timeslot timeslot, Date aptDate, Profile patientProfile) {
        String npi = extractNpi(providerComboBox.getValue());
        Doctor doctor = checkNpi(npi, providerList);
        if (checkAppointmentExists(patientProfile, aptDate, timeslot, appointmentList)) {
            outputArea.setText(patientProfile + " has an existing appointment at the same time slot.");
            return;
        }
        if (providerAvailability(npi, timeslot, aptDate, appointmentList)) {
            outputArea.setText(doctor + " is not available at " + timeslot);
            return;
        }
        Patient patient = new Patient(patientProfile);



        Appointment appointment = new Appointment(aptDate, timeslot, patient, doctor);
        outputArea.setText(appointment + " booked.");
        appointmentList.add(appointment);
    }

    /**
     * Cancels an existing appointment based on the input fields.
     */
    @FXML
    public void cCommand() {
        LocalDate date = appointmentDatePicker.getValue();
        Date appointmentDate = new Date(date.getMonthValue(), date.getDayOfMonth(), date.getYear());

        String timeslotString = timeslotComboBox.getValue();
        Timeslot timeslot = Timeslot.findTimeslot(timeslotString);

        String fName = firstNameField.getText();
        String lName = lastNameField.getText();

        LocalDate date2 = dobPicker.getValue();
        Date birthday = new Date(date2.getMonthValue(), date2.getDayOfMonth(), date2.getYear());

        Profile patientProfile = new Profile(fName, lName, birthday);
        Patient patient = new Patient(patientProfile);
        Appointment appointment = foundAppointment(appointmentDate, timeslot, patient, appointmentList);
        if (appointment != null) {
            appointmentList.remove(appointment);
            outputArea.setText(appointment.getDate() + " " + appointment.getTimeslot() + " " + patient.getProfile()
                    + "- appointment has been cancelled.");
        } else
            outputArea.setText(appointmentDate + " " + timeslot + " " + patient + "- appointment does not exist.");

        showAppointments(appointmentList, appointmentListView);
        clear();
    }

    /**
     * Reschedules an appointment based on the input fields for new timeslot and date.
     */
    @FXML
    public void rCommand() {
        LocalDate localDate = originalAppointmentDate.getValue();
        Date aptDate = new Date(localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getYear());
        String timeslotString = currentTimeslot.getValue();
        Timeslot timeslot = Timeslot.findTimeslot(timeslotString);
        String firstName = rPatientFirstName.getText();
        String lastName = rPatientLastName.getText();
        LocalDate localDate2 = rPatientBirthday.getValue();
        Date birthday = new Date(localDate2.getMonthValue(), localDate2.getDayOfMonth(), localDate2.getYear());
        Profile patientProfile = new Profile(firstName, lastName, birthday);
        Patient patient = new Patient(patientProfile);

        if (foundAppointment(aptDate, timeslot, patient, appointmentList) == null) {
            outputArea.setText("appointment does not exist");
            return;
        }
        Appointment appointment = foundAppointment(aptDate, timeslot, patient, appointmentList);

        String timeslotString2 = newTimeslot.getValue();
        Timeslot timeslotChange = Timeslot.findTimeslot(timeslotString2);

        Appointment appointmentChange = new Appointment(aptDate, timeslotChange, patient, appointment.getProvider());

        // Determine provider type and availability checks accordingly
        if (appointment.getProvider() instanceof Doctor doctor) {
            // Check if patient already has an appointment in the same slot
            if (checkAppointmentExists(patientProfile, aptDate, timeslotChange, appointmentList)) {
                outputArea.setText(patientProfile + " has an existing appointment at the same time slot.");
                return;
            }
            // Check if the doctor is available
            if (providerAvailability(doctor.getNpi(), timeslotChange, appointmentChange.getDate(), appointmentList)) {
                outputArea.setText(doctor + " is not available at " + timeslotChange);
                return;
            }
        } else if (appointment.getProvider() instanceof Technician technician) {
            // Check for technician or imaging-specific availability
            if (checkAppointmentExists(patientProfile, aptDate, timeslotChange, appointmentList)) {
                outputArea.setText("Imaging appointment already exists for " + patientProfile + " at this timeslot.");
                return;
            }
            if (technicianAvailability(timeslotChange, appointmentChange.getDate(), appointmentList)) {
                outputArea.setText(technician + " is not available at " + timeslotChange);
                return;
            }
        }

        this.appointmentList.remove(appointment);
        this.appointmentList.add(appointmentChange);
        outputArea.setText("Rescheduled to " + appointmentChange);
        showAppointments(appointmentList, appointmentListView);

        rClear();
    }

    /**
     * Clears all fields in the scheduling section of the form.
     */
    @FXML
    public void clear() {
        firstNameField.clear();
        lastNameField.clear();
        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        dobPicker.setStyle(null);
        dobPicker.setValue(null);
        appointmentDatePicker.setValue(null);
        dateErrorLabel.setText("");
        dateErrorLabel.setVisible(false);
        ObservableList<String> emptyList = FXCollections.observableArrayList("none");
        timeslotComboBox.getSelectionModel().clearSelection();
        timeslotComboBox.setVisible(false);
        timeslotComboBox.setItems(emptyList);
        timeslotComboBox.setPromptText("");
        providerComboBox.getSelectionModel().clearSelection();
        providerComboBox.setVisible(false);
        providerComboBox.setItems(emptyList);
        providerComboBox.setPromptText("");
        firstNameErrorLabel.setText("");
        firstNameErrorLabel.setVisible(false);
        lastNameErrorLabel.setText("");
        lastNameErrorLabel.setVisible(false);
        appointmentDateErrorLabel.setText("");
        appointmentDateErrorLabel.setVisible(false);

        appointmentDatePicker.setStyle(null);
        toggleGroup.selectToggle(null);

        scheduleButton.setDisable(true);
        cancelButton.setDisable(true);
    }

    /**
     * Updates the state of the reschedule button based on input validation.
     */
//    private void updateRButtonState() {
//        boolean ifFirstNameEmpty = rPatientFirstName.getText().trim().isEmpty();
//        boolean ifLastNameEmpty = rPatientLastName.getText().trim().isEmpty();
//        boolean ifBirthdayEmpty = (rPatientBirthday.getValue() == null);
//        boolean ifOriginalTimeslotEmpty = (currentTimeslot.getValue() == null || currentTimeslot.getValue().isEmpty());
//        boolean ifNewTimeslotEmpty = (newTimeslot.getValue() == null || newTimeslot.getValue().isEmpty());
//        boolean shouldDisable = ifFirstNameEmpty || ifLastNameEmpty || ifBirthdayEmpty || !rBirthdayValid || !rAppointmentValid;
//
//        if (shouldDisable) {
//            currentTimeslot.setPromptText("Current Timeslot");
//            currentTimeslot.setItems(createTimeslotList());
//            currentTimeslot.setVisible(true);
//        }
//        shouldDisable = ifFirstNameEmpty || ifLastNameEmpty || ifBirthdayEmpty || !rBirthdayValid || !rAppointmentValid
//                || ifOriginalTimeslotEmpty || ifNewTimeslotEmpty;
//        reschedule.setDisable(shouldDisable);
//    }
    private void updateRButtonState() {
        boolean ifFirstNameEmpty = rPatientFirstName.getText().trim().isEmpty();
        boolean ifLastNameEmpty = rPatientLastName.getText().trim().isEmpty();
        boolean ifBirthdayEmpty = (rPatientBirthday.getValue() == null);
        boolean ifOriginalTimeslotEmpty = (currentTimeslot.getValue() == null || currentTimeslot.getValue().isEmpty());
        boolean ifNewTimeslotEmpty = (newTimeslot.getValue() == null || newTimeslot.getValue().isEmpty());

        // Consolidate all conditions into a single `shouldDisable` variable
        boolean shouldDisable = ifFirstNameEmpty || ifLastNameEmpty || ifBirthdayEmpty
                || !rBirthdayValid || !rAppointmentValid
                || ifOriginalTimeslotEmpty || ifNewTimeslotEmpty;

        // Update `reschedule` button state based on `shouldDisable`
        reschedule.setDisable(shouldDisable);

        // Only update currentTimeslot if it is currently empty (optional, to avoid redundant calls)
        if (shouldDisable && currentTimeslot.getItems().isEmpty()) {

            currentTimeslot.setPromptText("Current Timeslot");
            currentTimeslot.setItems(createTimeslotList());
            currentTimeslot.setVisible(true);
        }
    }

    /**
     * Clears all fields in the rescheduling section of the form.
     */
    @FXML
    public void rClear() {
        rPatientFirstName.clear();
        rPatientLastName.clear();
        rPatientBirthday.setValue(null);
        originalAppointmentDate.setValue(null);
        ObservableList<String> emptyList = FXCollections.observableArrayList("none");
        newTimeslot.getSelectionModel().clearSelection();
        newTimeslot.setVisible(false);
        newTimeslot.setItems(emptyList);
        newTimeslot.setPromptText(" ");
        currentTimeslot.getSelectionModel().clearSelection();
        currentTimeslot.setVisible(false);
        currentTimeslot.setItems(emptyList);
        currentTimeslot.setPromptText(" ");
        rDateErrorLabel.setText("");
        rDateErrorLabel.setVisible(false);
        rFirstNameErrorLabel.setText("");
        rFirstNameErrorLabel.setVisible(false);
        rLastNameErrorLabel.setText("");
        rLastNameErrorLabel.setVisible(false);
        originalAppointmentDateErrorLabel.setText("");
        originalAppointmentDateErrorLabel.setVisible(false);
        rPatientFirstName.setStyle(null);
        rPatientLastName.setStyle(null);
        rPatientBirthday.setStyle(null);
        originalAppointmentDate.setStyle(null);
        reschedule.setDisable(true);
    }

    /**
     * Sorts the appointment list by date and updates the displayed list.
     */
    @FXML
    public void sortByAppointment() {
        Sort.sortByDate(appointmentList);
        showAppointments(appointmentList, appointmentListView);
    }

    /**
     * Sorts the appointment list by patient and updates the displayed list.
     */
    @FXML
    public void sortByPatient() {
        Sort.sortByPatient(appointmentList);
        showAppointments(appointmentList, appointmentListView);
    }

    /**
     * Sorts the appointment list by location and updates the displayed list.
     */
    @FXML
    public void sortByLocation() {
        Sort.sortByLocation(appointmentList);
        showAppointments(appointmentList, appointmentListView);
    }

    /**
     * Displays only office appointments sorted by location.
     */
    @FXML
    private void poCommand() {
        ObservableList<String> observableAppointments = FXCollections.observableArrayList();

        if (appointmentList.isEmpty()) {
            observableAppointments.add("Schedule calendar is empty.");
            appointmentListView.setItems(observableAppointments);
            return;
        }

        Sort.sortByLocation(appointmentList);
        for (Appointment appointment : appointmentList) {
            if (!(appointment instanceof Imaging)) {
                observableAppointments.add(appointment.toString());
            }
        }
        appointmentListView.setItems(observableAppointments);
    }

    /**
     * Displays only imaging appointments sorted by location.
     */
    @FXML
    private void piCommand() {
        ObservableList<String> observableAppointments = FXCollections.observableArrayList();

        if (appointmentList.isEmpty()) {
            observableAppointments.add("Schedule calendar is empty.");
            appointmentListView.setItems(observableAppointments);
            return;
        }

        Sort.sortByLocation(appointmentList);
        for (Appointment appointment : appointmentList) {
            if (appointment instanceof Imaging) {
                observableAppointments.add(appointment.toString());
            }
        }

        appointmentListView.setItems(observableAppointments);
    }

    /**
     * Displays patient billing statements sorted by patient.
     */
    @FXML
    private void viewPatientBillingStatements() {
        ObservableList<String> observableAppointments = FXCollections.observableArrayList();
        if (appointmentList.isEmpty()) {
            observableAppointments.add("Schedule calendar is empty.");
            statementListView.setItems(observableAppointments);
            return;
        }
        Sort.sortByPatient(appointmentList);
        List<Patient> patientList = createPatientList(appointmentList);
        for (Patient patient : patientList) {
            observableAppointments.add("(" + (patientList.indexOf(patient) + 1) + ") " + patient + " [due: $" + patient.charge() + ".00]");
        }

        statementListView.setItems(observableAppointments);
    }

    /**
     * Displays provider credits based on scheduled appointments.
     */
    @FXML
    public void printProviderCredits() {
        ObservableList<String> observableAppointments = FXCollections.observableArrayList();
        if (this.appointmentList.isEmpty()) {
            observableAppointments.add("Schedule calendar is empty.");
            statementListView.setItems(observableAppointments);
            return;
        }
        Sort.provider(this.providerList);

        for (Provider provider : this.providerList) {
            int credit = 0;
            for (Appointment appointment : this.appointmentList) {
                if (appointment.getProvider().equals(provider)) {
                    credit += provider.rate();
                }
            }
            observableAppointments.add("(" + (providerList.indexOf(provider) + 1) + ") " + provider.getProfile() + " [credit amount: $" + credit + ".00]");
        }
        statementListView.setItems(observableAppointments);
    }

    /**
     * Checks if the patient's date of birth is valid.
     * @param date           the patient's date of birth
     * @param datepicker     the DatePicker control for the date
     * @param dateErrorLabel the label to display error messages
     * @return true if the date is valid, false otherwise
     */
    public static boolean checkBirthdayValidity(Date date, DatePicker datepicker, Label dateErrorLabel) {
        String errorMessage;
        if (!date.isNotToday() || date.isAfterToday()) {
            errorMessage = "Patient dob: " + date + " is today or a date after today.";
            setDateFieldInvalid(datepicker, dateErrorLabel, errorMessage);
            return false;
        }
        if (!date.isValid()) {
            errorMessage = "Patient dob: " + date + " is not a valid calendar date.";
            setDateFieldInvalid(datepicker, dateErrorLabel, errorMessage);
            return false;
        }
        setDateFieldValid(datepicker, dateErrorLabel, "Date of Birth");
        return true;
    }

    /**
     * Checks if the appointment date is valid.
     * @param date           the appointment date
     * @param datepicker     the DatePicker control for the date
     * @param dateErrorLabel the label to display error messages
     * @return true if the appointment date is valid, false otherwise
     */
    public static boolean checkAppointmentDateValidity(Date date, DatePicker datepicker, Label dateErrorLabel) {
        String errorMessage;

        if (!date.isValid()) {
            errorMessage = "Appointment Date: " + date + " is not a valid calendar date.";
        } else if (!date.isNotToday() || !date.isAfterToday()) {
            errorMessage = "Appointment Date: " + date + " is today or a date before today.";
        } else if (!date.isNotWeekend()) {
            errorMessage = "Appointment date: " + date + " is Saturday or Sunday.";
        } else if (!date.withinSixMonths()) {
            errorMessage = "Appointment date: " + date + " is not within six months.";
        } else {
            setDateFieldValid(datepicker, dateErrorLabel, "Appointment Date");
            return true;
        }
        setDateFieldInvalid(datepicker, dateErrorLabel, errorMessage);
        return false;
    }

    /**
     * Sets a DatePicker field as invalid and displays an error message.
     * @param datepicker   the DatePicker control to mark as invalid
     * @param errorLabel   the label to display the error message
     * @param errorMessage the error message to display
     */
    public static void setDateFieldInvalid(DatePicker datepicker, Label errorLabel, String errorMessage) {
        datepicker.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
    }

    /**
     * Sets a DatePicker field as valid.
     * @param datepicker the DatePicker control to mark as valid
     * @param errorLabel the label to hide the error message
     * @param promptText the prompt text to set in the DatePicker
     */
    public static void setDateFieldValid(DatePicker datepicker, Label errorLabel, String promptText) {
        datepicker.setStyle(null);
        errorLabel.setText(null);
        errorLabel.setVisible(false);
        datepicker.setPromptText(promptText);
    }

    /**
     * Formats a TextField to indicate if it's empty or valid.
     * @param textfield  the TextField control to format
     * @param errorLabel the label to display an error if empty
     * @param isEmpty    whether the field is empty
     */
    public static void textFieldFormat(TextField textfield, Label errorLabel, boolean isEmpty) {
        if (isEmpty) {
            textfield.setStyle("-fx-border-color: red;");
            errorLabel.setText("Field cannot be left empty");
            errorLabel.setVisible(true);
        } else {
            textfield.setStyle(null);
            errorLabel.setVisible(false);
        }
    }

    /**
     * Creates a list of available timeslots.
     * @return an ObservableList of timeslot strings
     */
    public static ObservableList<String> createTimeslotList() {
        ObservableList<String> timeslots = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            timeslots.add(Timeslot.getTimeslotByNumber(i).toString());
        }
        return timeslots;
    }

    /**
     * Creates a list of provider names from the provider list.
     * @param providerList the list of providers
     * @return an ObservableList of provider strings
     */
    public static ObservableList<String> createProviderSettings(List<Provider> providerList) {
        ObservableList<String> providers = FXCollections.observableArrayList();
        loadProviderList(providerList);
        for (Provider provider : providerList) {
            if (provider instanceof Doctor) {
                String string = provider.getProfile().getfname() + " " + provider.getProfile().getlname() + "("
                        + ((Doctor) provider).getNpi() + ")";
                providers.add(string);
            }
        }
        return providers;
    }

    /**
     * Parses provider information from command line and adds it to the provider list.
     * @param commandLine  the provider information string
     * @param providerList the list to add providers to
     */
    private static void createProviderList(String commandLine, List<Provider> providerList) {
        StringTokenizer tokenizer = new StringTokenizer(commandLine, " ");
        String type = tokenizer.nextToken();
        String firstName = tokenizer.nextToken();
        String lastName = tokenizer.nextToken();
        Date dob = stringToDate(tokenizer.nextToken());

        Profile profile = new Profile(firstName, lastName, dob);
        Location location = stringToLocation(tokenizer.nextToken());

        if (type.equals("D")) {
            Specialty specialty = stringToSpecialty(tokenizer.nextToken());
            String id = tokenizer.nextToken();
            Doctor doctor = new Doctor(profile, location, specialty, id);
            providerList.add(doctor);
        } else if (type.equals("T")) {
            int ratePerVisit = Integer.parseInt(tokenizer.nextToken());
            Technician technician = new Technician(profile, location, ratePerVisit);
            providerList.add(technician);
        }
    }

    /**
     * Loads providers from a file into the provider list.
     * @param providerList the list to add providers to
     */
    private static void loadProviderList(List<Provider> providerList) {
        File file = new File("src/main/java/scheduler/providers.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String providers = scanner.nextLine();
                createProviderList(providers, providerList);
            }
            Technician first = createCircularList(providerList);
            Sort.provider(providerList);
        } catch (Exception e) {
            // Handle exceptions

        }
    }

    /**
     * Converts a date string to a Date object.
     * @param commandLineDate the date string in M/D/YYYY format
     * @return the parsed Date object
     */
    private static Date stringToDate(String commandLineDate) {
        StringTokenizer dateTokenizer = new StringTokenizer(commandLineDate, "/");
        int month = Integer.parseInt(dateTokenizer.nextToken());
        int day = Integer.parseInt(dateTokenizer.nextToken());
        int year = Integer.parseInt(dateTokenizer.nextToken());
        return new Date(month, day, year);
    }

    /**
     * Converts a location string to a Location object.
     * @param commandLineLocation the location string
     * @return the corresponding Location object
     */
    private static Location stringToLocation(String commandLineLocation) {
        return Location.valueOfLocation(commandLineLocation.toUpperCase());
    }

    /**
     * Converts a specialty string to a Specialty object.
     *
     * @param commandLineSpecialty the specialty string
     * @return the corresponding Specialty object
     */
    private static Specialty stringToSpecialty(String commandLineSpecialty) {
        return Specialty.valueOfSpecialty(commandLineSpecialty.toUpperCase());
    }

    /**
     * Creates a circular linked list of technicians in the provider list.
     * @param providerList the list of providers
     * @return the first Technician in the circular list
     */
    private static Technician createCircularList(List<Provider> providerList) {
        Technician current = null;
        Technician first = null;

        for (int i = providerList.size() - 1; i >= 0; i--) {
            if (providerList.get(i) instanceof Technician) {
                if (current == null) {
                    current = (Technician) providerList.get(i);
                    first = current;
                    current.setHead(first);
                    continue;
                }
                current.setNext((Technician) providerList.get(i));
                current.setHead(first);
                current = (Technician) providerList.get(i);
            }
        }
        if (current != null) {
            current.setNext(first);
        }
        return first;
    }

    /**
     * Sets up a toggle group for the appointment type radio buttons.
     * @param officeAppointment   the office visit radio button
     * @param imagingAppointment  the imaging service radio button
     * @return the created ToggleGroup for appointment type selection
     */
    public static ToggleGroup toggleAppointmentType(RadioButton officeAppointment, RadioButton imagingAppointment) {
        ToggleGroup toggleGroup = new ToggleGroup();
        officeAppointment.setToggleGroup(toggleGroup);
        imagingAppointment.setToggleGroup(toggleGroup);
        return toggleGroup;
    }

    /**
     * Checks if a provider with the given NPI is available at the specified
     * timeslot and date.
     * @param npi             the provider's NPI number
     * @param timeslot        the timeslot to check
     * @param date            the date to check
     * @param appointmentList the list of existing appointments
     * @return true if the provider is unavailable, false if available
     */
    public static boolean providerAvailability(String npi, Timeslot timeslot, Date date, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getProvider() instanceof Doctor doctor && doctor.getNpi().equals(npi)
                    && appointment.getDate().equals(date) && appointment.getTimeslot().equals(timeslot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a technician with the given NPI is available at the specified
     * timeslot and date.
     * @param timeslot        the timeslot to check
     * @param date            the date to check
     * @param appointmentList the list of existing appointments
     * @return true if the provider is unavailable, false if available
     */
    public static boolean technicianAvailability(Timeslot timeslot, Date date, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getProvider() instanceof Technician technician
                    && appointment.getDate().equals(date)
                    && appointment.getTimeslot().equals(timeslot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an appointment already exists for a given profile, date, and
     * timeslot.
     * @param profile         the profile to check
     * @param date            the date to check
     * @param timeslot        the timeslot to check
     * @param appointmentList the list of existing appointments
     * @return true if an appointment exists, false otherwise
     */
    public static boolean checkAppointmentExists(Profile profile, Date date, Timeslot timeslot, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getPatient().getProfile().equals(profile) && appointment.getDate().equals(date)
                    && appointment.getTimeslot().equals(timeslot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extracts the NPI number from a formatted provider string.
     * @param formattedString the formatted string containing the NPI
     * @return the extracted NPI as a String
     */
    public static String extractNpi(String formattedString) {
        int startIndex = formattedString.indexOf('(') + 1;
        int endIndex = formattedString.indexOf(')');
        return formattedString.substring(startIndex, endIndex);
    }

    /**
     * Finds a doctor in the provider list with the specified NPI.
     * @param npi           the NPI to search for
     * @param providerList  the list of providers
     * @return the Doctor with the matching NPI, or null if not found
     */
    public static Doctor checkNpi(String npi, List<Provider> providerList) {
        for (Provider provider : providerList) {
            if (provider instanceof Doctor doctor && doctor.getNpi().equals(npi)) {
                return doctor;
            }
        }
        return null;
    }

    /**
     * Finds an available technician for an imaging appointment.
     * @param timeslot         the timeslot requested
     * @param appointmentDate  the appointment date
     * @param patient          the patient for the appointment
     * @param room             the radiology room
     * @param appointmentList  the list of existing appointments
     * @param providerList     the list of providers
     * @param outputText       the TextArea for displaying messages
     * @return an Imaging appointment if technician and room are available, or null if not
     */
    public static Imaging findTechnicianAvailability(Timeslot timeslot, Date appointmentDate, Person patient, Radiology room,
                                                     List<Appointment> appointmentList, List<Provider> providerList, TextArea outputText) {
        Technician technicianList = null;
        int counter = 0;
        int size = 0;
        for (Provider provider : providerList) {
            if (provider instanceof Technician technician) {
                technicianList = technician.getHead();
                size = technicianList.getSize();
                break;
            }
        }
        while (technicianList != null && counter < size) {
            boolean isAvailable = true;
            for (Appointment appointment : appointmentList) {
                if (appointment.getProvider().equals(technicianList)
                        && appointment.getTimeslot().equals(timeslot)
                        && appointment.getDate().equals(appointmentDate)) {
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable && checkRoomAvailability(room, new Appointment(appointmentDate, timeslot, patient, technicianList), timeslot, appointmentDate, appointmentList)) {
                setHead(technicianList.getNext(), providerList);
                return new Imaging(appointmentDate, timeslot, patient, technicianList, room);
            }
            technicianList = technicianList.getNext();
            counter++;
        }
        outputText.setText("Cannot find an available technician at all locations for " + room + " at " + timeslot);
        return null;
    }

    /**
     * Checks if the specified radiology room is available.
     * @param room             the radiology room to check
     * @param appointment      the appointment being scheduled
     * @param timeslot         the timeslot for the appointment
     * @param appointmentDate  the date for the appointment
     * @param appointmentList  the list of existing appointments
     * @return true if the room is available, false otherwise
     */
    private static boolean checkRoomAvailability(Radiology room, Appointment appointment, Timeslot timeslot, Date appointmentDate, List<Appointment> appointmentList) {
        boolean roomAvailable = true;
        for (Appointment allAppointments : appointmentList) {
            Provider provider = (Provider) allAppointments.getProvider();
            Provider currProvider = (Provider) appointment.getProvider();
            if (appointmentDate.equals(allAppointments.getDate()) && timeslot.equals(allAppointments.getTimeslot()) && currProvider.getLocation().equals(provider.getLocation()))
                if (allAppointments instanceof Imaging imagingApt) {
                    if (imagingApt.getRoom().equals(room)) {
                        roomAvailable = false;
                        break;
                    }
                }

        }

        return roomAvailable;
    }

    /**
     * Sets the head technician in the circular linked list.
     * @param technician   the Technician to set as head
     * @param providerList the list of providers
     */
    private static void setHead(Technician technician, List<Provider> providerList) {
        for (Provider provider : providerList) {
            if (provider instanceof Technician technician1) {
                technician1.setHead(technician);
            }
        }
    }

    /**
     * Finds an appointment in the list matching the date, timeslot, and patient.
     * @param date            the appointment date
     * @param timeslot        the timeslot of the appointment
     * @param patient         the patient for the appointment
     * @param appointmentList the list of existing appointments
     * @return the found Appointment, or null if not found
     */
    public static Appointment foundAppointment(Date date, Timeslot timeslot, Person patient, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            if (patient.equals(appointment.getPatient()) && appointment.getTimeslot().equals(timeslot) && appointment.getDate().equals(date)) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * Displays the list of appointments in the ListView.
     * @param appointmentList     the list of appointments
     * @param appointmentListView the ListView to display appointments
     */
    public static void showAppointments(List<Appointment> appointmentList, ListView<String> appointmentListView) {
        ObservableList<String> observableAppointments = FXCollections.observableArrayList();

        if (appointmentList.isEmpty()) {
            observableAppointments.add("Schedule calendar is empty.");
            appointmentListView.setItems(observableAppointments);
            return;
        }

        for (Appointment appointment : appointmentList) {
            observableAppointments.add(appointment.toString());
        }
        appointmentListView.setItems(observableAppointments);
    }

    /**
     * Creates a list of unique patients from the appointment list.
     * @param appointmentList the list of appointments
     * @return a list of unique patients
     */
    public static List<Patient> createPatientList(List<Appointment> appointmentList) {
        List<Patient> patientList = new List<>();
        for (Appointment appointment : appointmentList) {
            Profile appointmentProfile = appointment.getPatient().getProfile();
            Patient existingPatient = null;
            for (Patient patient : patientList) {
                if (patient.getProfile().equals(appointmentProfile)) {
                    existingPatient = patient;
                    break;
                }
            }
            if (existingPatient != null) {
                existingPatient.addVisit(appointment);
            } else {
                Patient newPatient = new Patient(appointmentProfile);
                newPatient.addVisit(appointment);
                patientList.add(newPatient);
            }
        }
        return patientList;
    }
}