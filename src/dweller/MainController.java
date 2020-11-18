package dweller;

import data.Appointment;
import data.Customer;
import data.Data;
import data.Report;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The MainController class is the controller for main.fxml
 *
 * @author Ben Garding
 */
public class MainController {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private RadioButton monthRadio;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Tab customerTab;
    @FXML
    private MenuBar menuBar;

    private FilteredList<Appointment> appointmentsFiltered = new FilteredList<>(Objects.requireNonNull(Data.appointmentList));

    /**
     * The datePicker is set to today's date. radioSelected() is called to load appointments into appointmentTable.
     * customerTable is loaded with the customerList from the Data class. A listener is created to show datePicker and
     * the radio buttons when the appointment tab is selected and hide them when the customer tab is selected.
     */
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        radioSelected();
        customerTable.setItems(Data.customerList);
        customerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        customerTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                monthRadio.setVisible(false);
                weekRadio.setVisible(false);
                datePicker.setVisible(false);
            } else {
                monthRadio.setVisible(true);
                weekRadio.setVisible(true);
                datePicker.setVisible(true);
            }
        });
    }

    /**
     * Updates the appointment table based on the value of the date picker. Displays a month view if monthRadio is selected,
     * and week view if weekRadio is selected.
     *
     */
    @FXML
    private void radioSelected() {
        if (monthRadio.isSelected()) {
            appointmentsFiltered.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                        LocalDate monthStart = datePicker.getValue().minusDays(datePicker.getValue().getDayOfMonth() - 1);
                        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

                        return ti -> !monthStart.isAfter(ti.getStart().toLocalDate()) && !monthEnd.isBefore(ti.getStart().toLocalDate());
                    }, datePicker.valueProperty()
            ));
        } else {
            appointmentsFiltered.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                        DayOfWeek dayOfWeek = datePicker.getValue().getDayOfWeek();
                        int dayInt = 0;
                        switch (dayOfWeek) {
                            case MONDAY:
                                dayInt = 1;
                                break;
                            case TUESDAY:
                                dayInt = 2;
                                break;
                            case WEDNESDAY:
                                dayInt = 3;
                                break;
                            case THURSDAY:
                                dayInt = 4;
                                break;
                            case FRIDAY:
                                dayInt = 5;
                                break;
                            case SATURDAY:
                                dayInt = 6;
                                break;
                        }
                        LocalDate weekStart = datePicker.getValue().minusDays(dayInt);
                        LocalDate weekEnd = weekStart.plusDays(6);

                        return ti -> !weekStart.isAfter(ti.getStart().toLocalDate()) && !weekEnd.isBefore(ti.getStart().toLocalDate());
                    }, datePicker.valueProperty()
            ));
        }
        appointmentTable.setItems(appointmentsFiltered);
    }

    /**
     * Returns to the login window and closes the main window
     */
    @FXML
    private void logOff() {
        try {
            newWindow("login", "Scheduler");
            Stage oldStage = (Stage) menuBar.getScene().getWindow();
            oldStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application
     */
    @FXML
    private void exit() {
        Platform.exit();
    }

    /**
     * Calls newWindow() to open customer.fxml
     */
    @FXML
    private void newCustomer() {
        try {
            newWindow("customer", "New Customer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls newWindow() to open appointment.fxml
     */
    @FXML
    private void newAppointment() {
        try {
            newWindow("appointment", "New Appointment");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits a customer or appointment based on the tab that is selected. Takes the selected item and loads it into the
     * appropriate fxml file
     */
    @FXML
    private void edit() {
        if (customerTab.isSelected()) {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Main.showAlert(Alert.AlertType.INFORMATION, "No Customer Selected", "Please select a customer to edit");
            } else {
                try {
                    CustomerController controller = newWindow("customer", "Edit Customer").getController();
                    controller.editCustomer(selectedCustomer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Main.showAlert(Alert.AlertType.INFORMATION, "No Appointment Selected", "Please select an appointment to edit");
            } else {
                try {
                    AppointmentController controller = newWindow("appointment", "Edit Appointment").getController();
                    controller.editAppointment(selectedAppointment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Deletes a customer or appointment based on the tab that is selected. This method first checks if there are any customers
     * associated with an appointment before deletion, and displays an error if there are any associations
     */
    @FXML
    private void delete() {
        if (customerTab.isSelected()) {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Main.showAlert(Alert.AlertType.INFORMATION, "No Customer Selected", "Please select a " +
                        "customer to delete");
            } else if (!Data.safeToDelete(selectedCustomer.getId())) {
                Main.showAlert(Alert.AlertType.WARNING, "Deletion Error", "There was an error deleting " +
                        selectedCustomer.getName() + ". Please make sure that the customer is not associated with any " +
                        "appointments before deleting.");
            } else {
                if (Data.deleteCustomer(selectedCustomer)) {
                    Main.showAlert(Alert.AlertType.INFORMATION, "Customer Deleted", selectedCustomer.getName() +
                            " deleted from the database");
                } else {
                    Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error deleting " +
                            "from the database");
                }
            }
        } else {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Main.showAlert(Alert.AlertType.INFORMATION, "No Appointment Selected", "Please select an " +
                        "appointment to delete");
            } else {
                if (Data.deleteAppointment(selectedAppointment)) {
                    Main.showAlert(Alert.AlertType.INFORMATION, "Appointment Deleted", selectedAppointment.getTitle() +
                            " deleted from the database");
                } else {
                    Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error deleting " +
                            "from the database");
                }
            }
        }
    }

    /**
     * Extracts the count of each unique appointment type from the database and displays the results in an alert window
     */
    @FXML
    private void typeReport() {
        ArrayList<Report> types = Data.getTypeReport();
        StringBuilder sb = new StringBuilder("Total appointment count by type: \n");

        if (types != null) {
            int longestType = 0;
            int tabs;

            for (Report type : types) {
                if (type.getType().length() > longestType) {
                    longestType = type.getType().length();
                }
            }

            for (Report type : types) {
                sb.append("\n").append(type.getType()).append(":");

                if (longestType % ((type.getType().length() - 1)) == 0) {
                    tabs = longestType / type.getType().length();
                } else if (longestType == type.getType().length()) {
                    tabs = 0;
                } else {
                    tabs = longestType / (type.getType().length() - 1);
                }

                sb.append("\t".repeat(Math.max(0, tabs + 1)));
                sb.append(type.getCount());
            }
            Main.showAlert(Alert.AlertType.INFORMATION, "Type Count Report", sb.toString());
        }
    }

    /**
     * Extracts the count of each appointment by month from the database and displays the results in an alert window
     */
    @FXML
    private void monthReport() {
        ArrayList<Report> months = Data.getMonthReport();
        StringBuilder sb = new StringBuilder("Total appointment count by month: \n");

        if (months != null) {
            for (Report month : months) {
                String currentMonth;
                switch (month.getMonth()) {
                    case 1:
                        currentMonth = "January:\t\t";
                        break;
                    case 2:
                        currentMonth = "February:\t\t";
                        break;
                    case 3:
                        currentMonth = "March:\t\t";
                        break;
                    case 4:
                        currentMonth = "April:\t\t";
                        break;
                    case 5:
                        currentMonth = "May:\t\t\t";
                        break;
                    case 6:
                        currentMonth = "June:\t\t";
                        break;
                    case 7:
                        currentMonth = "July:\t\t\t";
                        break;
                    case 8:
                        currentMonth = "August:\t\t";
                        break;
                    case 9:
                        currentMonth = "September:\t";
                        break;
                    case 10:
                        currentMonth = "October:\t\t";
                        break;
                    case 11:
                        currentMonth = "November:\t";
                        break;
                    case 12:
                        currentMonth = "December:\t";
                        break;
                    default:
                        currentMonth = "";
                }
                sb.append("\n").append(currentMonth).append(month.getCount());
            }
            Main.showAlert(Alert.AlertType.INFORMATION, "Month Count Report", sb.toString());
        }
    }

    /**
     * Calls newWindow() to open contactReport.fxml
     */
    @FXML
    private void contactReport() {
        try {
            newWindow("contactReport", "Contact Appointment Report");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls newWindow() to open customerReport.fxml
     */
    @FXML
    private void customerReport() {
        try {
            newWindow("customerReport", "Customer Appointment Report");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a new window
     *
     * @param fxml  The fxml file name to be opened. Concatenated with ".fxml"
     * @param title The title to set on the new window
     * @return The FXMLLoader that was created
     * @throws IOException Loading the new scene from the FXMLLoader
     */
    private FXMLLoader newWindow(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

        return fxmlLoader;
    }
}
