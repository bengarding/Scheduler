package scheduler;

import data.Appointment;
import data.Customer;
import data.Data;
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
import java.util.Objects;

public class MainController {

    private FilteredList<Appointment> appointmentsFiltered = new FilteredList<>(Objects.requireNonNull(Data.appointmentList));

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
    private Tab appointmentsTab;
    @FXML
    private Tab customerTab;
    @FXML
    private MenuBar menuBar;

    public void initialize() {
        datePicker.setValue(LocalDate.of(2020, 6, 1));
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
     * Calls newWindow to open the customer window
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
     * Calls newWindow to open the appointment window
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
                    CustomerController controller = newWindow("appointment", "Edit Appointment").getController();
//                    controller.editAppointment(selectedAppointment);
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
                Main.showAlert(Alert.AlertType.INFORMATION, "No Customer Selected", "Please select a customer to delete");
            } else if (!Data.safeToDelete(selectedCustomer.getId())) {
                Main.showAlert(Alert.AlertType.WARNING, "Deletion Error", "There was an error deleting " + selectedCustomer.getName() +
                        ". Please make sure that the customer is not associated with any appointments before deleting.");
            } else {
                if (Data.deleteCustomer(selectedCustomer)) {
                    Main.showAlert(Alert.AlertType.INFORMATION, "Customer Deleted", selectedCustomer.getName() +
                            " deleted from the database");
                } else {
                    Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error deleting from the database");
                }
            }
        } else {

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
