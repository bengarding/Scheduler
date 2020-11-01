package scheduler;

import datasource.Appointment;
import datasource.Customer;
import datasource.Datasource;
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

    private FilteredList<Appointment> appointmentsFiltered = new FilteredList<>(Objects.requireNonNull(Datasource.appointmentList));

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
        customerTable.setItems(Datasource.customerList);
        customerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
     * Hides the radio buttons and date picker if the customer tab is selected, and shows them if the appointments tab is selected.
     */
    @FXML
    private void tabSelected() {
        try {
            if (customerTab.isSelected()) {
                monthRadio.setVisible(false);
                weekRadio.setVisible(false);
                datePicker.setVisible(false);
            } else if (appointmentsTab.isSelected()) {
                monthRadio.setVisible(true);
                weekRadio.setVisible(true);
                datePicker.setVisible(true);
            }
        } catch (NullPointerException e) {
            // Not sure why it's throwing a NullPointerException. Everything works as intended
        }
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
     * Opens a new window
     *
     * @param fxml  The fxml file name to be opened. Concatenated with ".fxml"
     * @param title The title to set on the new window
     * @throws IOException
     */
    private void newWindow(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
