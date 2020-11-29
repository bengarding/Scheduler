package scheduler;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * The AppointmentController class is the controller for appointment.fxml
 *
 * @author Ben Garding
 */
public class AppointmentController {
    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField userIDField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> contactBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Label titleError;
    @FXML
    private Label startTimeError;
    @FXML
    private Label startTimeOutsideHoursError;
    @FXML
    private Label selectStartDateError;
    @FXML
    private Label startTimeBeforeEndError;
    @FXML
    private Label endTimeError;
    @FXML
    private Label endTimeOutsideHoursError;
    @FXML
    private Label selectEndDateError;
    @FXML
    private Label endTimeAfterStartError;
    @FXML
    private Label locationError;
    @FXML
    private Label typeError;
    @FXML
    private Label customerIDError;
    @FXML
    private Label customerIDExistsError;
    @FXML
    private Label userIDError;
    @FXML
    private Label UserIDExistsError;
    @FXML
    private Label startDateError;
    @FXML
    private Label startDateBeforeEndError;
    @FXML
    private Label endDateError;
    @FXML
    private Label endDateAfterStartError;
    @FXML
    private Label contactError;

    LocalDate startDatePicked;
    LocalDate endDatePicked;
    Instant startTimePicked;
    Instant endTimePicked;
    boolean customerIDExists;
    boolean userIDExists;
    boolean appointmentEdit = false;

    private final LocalTime endTimePST = LocalTime.of(18, 1);
    private final LocalTime startTimePST = LocalTime.of(6, 59);
    private final ZoneId utcZone = ZoneId.of("UTC");
    private final ZoneId pstZone = ZoneId.of("US/Pacific");
    private final ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());


    /**
     * The contactBox is loaded with the names of all contacts. The idField is loaded with the next integer value after
     * the last appointment, and will used when creating a new appointment. idField is disabled so the user cannot
     * alter the value. Listeners are created on every input control except for descriptionArea. These listeners run
     * input validations.
     *
     */
    public void initialize() {
        userIDField.setText(String.valueOf(Main.currentUser.getId()));
        ObservableList<String> contactList = FXCollections.observableArrayList();
        for (Contact contact : Data.contactList) {
            contactList.add(contact.getName());
        }
        contactBox.setItems(contactList);
        idField.setText(String.valueOf(Data.appointmentList.get(Data.appointmentList.size() - 1).getId() + 1));

        titleField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                titleError.setVisible(titleField.getText().isEmpty());
            }
        });

        startDatePicker.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                endDatePicker.setValue(startDatePicker.getValue());
                endDatePicked = startDatePicker.getValue();
                AppointmentController.this.validateStartDate();
            }
        });

        endDatePicker.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                AppointmentController.this.validateEndDate();
            }
        });

        startTimeField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                AppointmentController.this.validateStartTime();
            }
        });

        endTimeField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                AppointmentController.this.validateEndTime();
            }
        });

        locationField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                locationError.setVisible(locationField.getText().isEmpty());
            }
        });

        typeField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                typeError.setVisible(typeField.getText().isEmpty());
            }
        });

        customerIDField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {

            if (aBoolean) {
                AppointmentController.this.validateCustomerID();
            }
        });

        customerIDField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                customerIDField.setText(s);
            }
        });

        userIDField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                AppointmentController.this.validateUserID();
            }
        });

        userIDField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                userIDField.setText(s);
            }
        });

        contactBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                contactError.setVisible(contactBox.getValue() == null);
            }
        });
    }

    /**
     * Performs validation checks for the start time entered. Validates for proper format and if it's within PST business hours
     *
     * @return True if valid and false if invalid
     */
    private boolean validateStartTime() {
        if (startDatePicker.getValue() == null) {
            selectStartDateError.setVisible(true);
            startTimeError.setVisible(false);
            startTimeBeforeEndError.setVisible(false);
            return false;
        } else if (startTimeField.getText().isEmpty() || !startTimeField.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            selectStartDateError.setVisible(false);
            startTimeError.setVisible(true);
            startTimeBeforeEndError.setVisible(false);
            return false;
        }
        startDateError.setVisible(false);

        Instant allowedStartTime = ZonedDateTime.of(startDatePicked, startTimePST, pstZone).toInstant();
        Instant allowedEndTime = ZonedDateTime.of(startDatePicked, endTimePST, pstZone).toInstant();

        if (startTimeField.getText().length() != 5) {
            startTimePicked = ZonedDateTime.of(startDatePicked, LocalTime.parse("0" + startTimeField.getText()), localZone).toInstant();
        } else {
            startTimePicked = ZonedDateTime.of(startDatePicked, LocalTime.parse(startTimeField.getText()), localZone).toInstant();
        }

        if (endTimePicked != null && startTimePicked.isAfter(endTimePicked)) {
            selectStartDateError.setVisible(false);
            startTimeError.setVisible(false);
            startTimeBeforeEndError.setVisible(true);
            return false;
        }

        selectStartDateError.setVisible(false);
        startTimeError.setVisible(false);
        startTimeBeforeEndError.setVisible(false);

        if (startTimePicked.isAfter(allowedStartTime) && startTimePicked.isBefore(allowedEndTime)) {
            startTimeOutsideHoursError.setVisible(false);
            return true;
        } else {
            startTimeOutsideHoursError.setVisible(true);
            outsideHours();
            return false;
        }
    }

    /**
     * Performs validation checks for the end time entered. Validates for proper format, if it's within PST business
     * hours, and if there are any conflicting appointments for the customer ID entered.
     *
     * @return True if valid and false if invalid
     */
    private boolean validateEndTime() {
        if (endDatePicker.getValue() == null) {
            selectEndDateError.setVisible(true);
            endTimeError.setVisible(false);
            return false;
        } else if (endTimeField.getText().isEmpty() || !endTimeField.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            selectEndDateError.setVisible(false);
            endTimeError.setVisible(true);
            return false;
        }
        endDateError.setVisible(false);

        Instant allowedStartTime = ZonedDateTime.of(endDatePicked, startTimePST, pstZone).toInstant();
        Instant allowedEndTime = ZonedDateTime.of(endDatePicked, endTimePST, pstZone).toInstant();

        if (endTimeField.getText().length() != 5) {
            endTimePicked = ZonedDateTime.of(endDatePicked, LocalTime.parse("0" + endTimeField.getText()), localZone).toInstant();
        } else {
            endTimePicked = ZonedDateTime.of(endDatePicked, LocalTime.parse(endTimeField.getText()), localZone).toInstant();
        }

        if (endTimePicked != null && startTimePicked.isAfter(endTimePicked)) {
            selectEndDateError.setVisible(false);
            endTimeError.setVisible(false);
            endTimeAfterStartError.setVisible(true);
            return false;
        }

        selectEndDateError.setVisible(false);
        endTimeError.setVisible(false);
        endTimeAfterStartError.setVisible(false);

        if (endTimePicked.isAfter(allowedStartTime) && endTimePicked.isBefore(allowedEndTime)) {
            endTimeOutsideHoursError.setVisible(false);
        } else {
            endTimeOutsideHoursError.setVisible(true);
            outsideHours();
            return false;
        }

        if (customerIDExists && startTimePicked != null && endTimePicked != null) {
            ArrayList<Appointment> appointments = Data.getAppointmentDatesForCustomer(Integer.parseInt(customerIDField.getText()));
            if (appointments != null) {
                for (Appointment appointment : appointments) {

                    Instant startAppointment = ZonedDateTime.of(appointment.getStart().minusMinutes(1), localZone).toInstant();
                    Instant endAppointment = ZonedDateTime.of(appointment.getEnd().plusMinutes(1), localZone).toInstant();

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M-d-u");

                    if ((startTimePicked.isAfter(startAppointment) && startTimePicked.isBefore(endAppointment)) ||
                            (endTimePicked.isAfter(startAppointment) && endTimePicked.isBefore(endAppointment)) ||
                            (startAppointment.isAfter(startTimePicked) && startAppointment.isBefore(endTimePicked)) ||
                            (endAppointment.isAfter(startTimePicked) && endAppointment.isBefore(endTimePicked))) {
                        String message = "The time chosen conflicts with the appointment: " + appointment.getTitle() +
                                ", which is scheduled from " + appointment.getStart().toLocalDate().format(dateFormatter) + " " +
                                appointment.getStart().toLocalTime() + " to " + appointment.getEnd().toLocalDate().format(dateFormatter) + " " +
                                appointment.getEnd().toLocalTime() + " " + ZonedDateTime.now().format(Main.zoneFormatter) + ". Please select another time for the new appointment";
                        Main.showAlert(Alert.AlertType.INFORMATION, "Schedule Conflict", message);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Displays a custom alert if the entered start or end times are outside of PST business hours. The acceptable
     * business hours are also shown in local time.
     */
    private void outsideHours() {
        String message = "You have selected a time that is outside of normal business hours: 7:00-18:00 PST. The business hours in your " +
                "time zone are " + ZonedDateTime.of(LocalDate.now(), startTimePST.plusMinutes(1), pstZone).toInstant().atZone(localZone).toLocalTime() +
                "-" + ZonedDateTime.of(LocalDate.now(), endTimePST.minusMinutes(1), pstZone).toInstant().atZone(localZone).toLocalTime() +
                " " + ZonedDateTime.now().format(Main.zoneFormatter);
        Main.showAlert(Alert.AlertType.INFORMATION, "Outside Business Hours", message);
    }

    /**
     * Validates the customer ID entered. If the customer ID exists, the customerIDExists boolean is set to true.
     * Otherwise, an error message is displayed and customerIDExists is set to false;
     *
     * @return True if valid and false if invalid
     */
    private boolean validateCustomerID() {
        if (customerIDField.getText().isEmpty()) {
            customerIDError.setVisible(true);
            return false;
        }
        customerIDError.setVisible(false);
        for (Customer customer : Data.customerList) {
            if (customer.getId() == Integer.parseInt(customerIDField.getText())) {
                customerIDExistsError.setVisible(false);
                customerIDExists = true;
                return true;
            }
        }
        customerIDExistsError.setVisible(true);
        customerIDExists = false;
        return false;
    }

    /**
     * Validates the user ID entered. If the user ID does not exist, an error message is displayed
     *
     * @return True if valid and false if invalid
     */
    private boolean validateUserID() {
        if (userIDField.getText().isEmpty()) {
            userIDError.setVisible(true);
            return false;
        }
        userIDError.setVisible(false);
        for (User user : Data.userList) {
            if (user.getId() == Integer.parseInt(userIDField.getText())) {
                UserIDExistsError.setVisible(false);
                userIDExists = true;
                return true;
            }
        }
        UserIDExistsError.setVisible(true);
        userIDExists = false;
        return false;
    }

    /**
     * Validates the start date selected. If the date selected is after the end date, an error message is displayed.
     *
     * @return True if valid and false if invalid
     */
    private boolean validateStartDate() {
        if (startDatePicker.getValue() == null) {
            startDateError.setVisible(true);
            return false;
        } else if (endDatePicker.getValue() != null && endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            startDateError.setVisible(false);
            startDateBeforeEndError.setVisible(true);
            return false;
        }
        startDateError.setVisible(false);
        startDateBeforeEndError.setVisible(false);
        startDatePicked = startDatePicker.getValue();
        return true;
    }

    /**
     * Validates the end date selected. If the date selected is before the start date, an error message is displayed.
     *
     * @return True if valid and false if invalid
     */
    private boolean validateEndDate() {
        if (endDatePicker.getValue() == null) {
            endDateError.setVisible(true);
            return false;
        } else if (startDatePicker.getValue() != null && endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            endDateError.setVisible(false);
            endDateAfterStartError.setVisible(true);
            return false;
        }
        endDateError.setVisible(false);
        endDateAfterStartError.setVisible(false);
        endDatePicked = endDatePicker.getValue();
        return true;
    }

    /**
     * Validates all of the controls
     *
     * @return True if all controls are valid and false if any are invalid
     */
    private boolean validateAll() {
        if (titleField.getText().isEmpty()) {
            titleError.setVisible(true);
            return false;
        } else if (locationField.getText().isEmpty()) {
            locationError.setVisible(true);
            return false;
        } else if (typeField.getText().isEmpty()) {
            typeError.setVisible(true);
            return false;
        } else if (!validateStartDate()) {
            return false;
        } else if (!validateStartTime()) {
            return false;
        } else if (!validateEndDate()) {
            return false;
        } else if (!validateEndTime()) {
            return false;
        } else if (!validateCustomerID()) {
            return false;
        } else if (!validateUserID()) {
            return false;
        } else if (contactBox.getValue() == null) {
            contactError.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * This method is called when the user selects an appointment to edit from main.fxml. All of the existing values are
     * loaded into the appropriate controls. The appointmentEdit boolean is set to true so that upon saving, the
     * appointment is edited instead of a new one being created.
     *
     * @param appointment The existing appointment to be edited
     */
    public void editAppointment(Appointment appointment) {
        idField.setText(appointment.getIdProp());
        titleField.setText(appointment.getTitle());
        typeField.setText(appointment.getType());
        locationField.setText(appointment.getLocation());
        startDatePicker.setValue(appointment.getStart().toLocalDate());
        startTimeField.setText(appointment.getStart().toLocalTime().toString());
        endDatePicker.setValue(appointment.getEnd().toLocalDate());
        endTimeField.setText(appointment.getEnd().toLocalTime().toString());
        customerIDField.setText(appointment.getCustomerIdProp());
        userIDField.setText(String.valueOf(appointment.getUserId()));
        contactBox.setValue(appointment.getContactProp());
        descriptionArea.setText(appointment.getDescription());

        startTimePicked = ZonedDateTime.of(appointment.getStart(), localZone).toInstant();
        endTimePicked = ZonedDateTime.of(appointment.getEnd(), localZone).toInstant();
        startDatePicked = startDatePicker.getValue();
        endDatePicked = endDatePicker.getValue();

        appointmentEdit = true;
    }

    /**
     * Closes the window
     */
    @FXML
    private void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * If all controls are valid, saves a new appointment to the database. If the appointmentEdit boolean is set to
     * true, then an existing appointment is updated rather than a new one being created
     */
    @FXML
    private void save() {
        if (validateAll()) {
            LocalDateTime start = LocalDateTime.ofInstant(startTimePicked, utcZone);
            LocalDateTime end = LocalDateTime.ofInstant(endTimePicked, utcZone);

            int contactID = 0;
            for (Contact contact : Data.contactList) {
                if (contact.getName().equals(contactBox.getValue())) {
                    contactID = contact.getId();
                    break;
                }
            }

            Appointment appointment = new Appointment(Integer.parseInt(idField.getText()), titleField.getText(),
                    descriptionArea.getText(), locationField.getText(), typeField.getText(), start, end,
                    Integer.parseInt(customerIDField.getText()), Integer.parseInt(userIDField.getText()), contactID);

            if (appointmentEdit && Data.editAppointment(appointment)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Appointment Edited", "Appointment successfully updated");
                return;
            } else if (appointmentEdit) {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
                return;
            }

            if (Data.newAppointment(appointment)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Appointment Added", "Appointment successfully added to the database");
            } else {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
            }
        }
    }
}
