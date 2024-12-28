Clinic Manager GUI:

A JavaFX-based graphical user interface (GUI) application for managing clinic appointments and imaging services. This project enhances previous iterations of the RU Clinic Scheduler by incorporating a modern GUI while maintaining robust scheduling functionalities.

Features:

	•	Appointment Management:
	•	Schedule, reschedule, and cancel appointments for office visits or imaging services.
	•	Ensure provider availability and avoid scheduling conflicts.
	•	Imaging Services:
	•	Book X-rays, ultrasounds, and CAT scans with automatic technician assignment using a circular list.
	•	Manage imaging rooms for each service type across multiple clinic locations.
	•	Dynamic Views:
	•	View appointments by location, provider, or patient.
	•	Display expected credits for providers based on completed appointments.
	•	Error Handling:
	•	Comprehensive validation to reject invalid data and prevent crashes.
	•	User-friendly error messages displayed on the GUI.

Technology Stack:

	•	JavaFX:
	•	Components: TextField, Button, RadioButton, TextArea, TableView, TabPane, GridPane.
	•	FXML: Defines the scene graph for the application.
	•	MVC Design Pattern:
	•	Model: Business logic and data models from previous project iterations.
	•	View: Defined in clinic-view.fxml.
	•	Controller: Event handling and GUI interactions in ClinicManagerController.java.
	•	Java:
	•	Reused core logic from earlier projects, ensuring modular and reusable code.

How It Works:

	•	GUI Workflow:
	•	Input appointment details using the form fields and buttons.
	•	Display, filter, and sort appointments in a structured TableView.
	•	Real-time updates to the GUI for added, rescheduled, or canceled appointments.
	•	Validation:
	•	The system enforces strict rules for data integrity, including valid dates, provider availability, and non-overlapping appointments.
	•	Dynamic Technician Assignment:
	•	Technicians are assigned to imaging appointments using a rotation system.

 Requirements:
 
	•	Java 17 or higher.
	•	JavaFX 17 or higher.

Future Enhancements:

	•	Add patient profiles with more detailed medical history.
	•	Implement login and role-based access for clinic staff and administrators.
	•	Integrate database support for persistent storage of appointments and provider data.

Contributors:

Keshav Dave, Danny Watson
