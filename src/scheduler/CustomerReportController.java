package scheduler;

import data.Appointment;
import data.Customer;
import data.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * The CustomerReportController class is the controller for customerReport.fxml
 *
 * @author Ben Garding
 */
public class CustomerReportController {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private ComboBox<String> customerBox;
    @FXML
    private Button closeButton;

    /**
     * The customerBox is loaded with the names of all the customers. The first customer in the list is selected and the
     * appointments that are associated with that customer are loaded into appointmentTable.
     */
    public void initialize() {
        ObservableList<String> customerList = FXCollections.observableArrayList();
        for (Customer customer : Data.customerList) {
            customerList.add(customer.getName());
        }
        customerBox.setItems(customerList);
        customerBox.setValue(customerList.get(0));

        appointmentTable.setItems(Data.getAppointmentsForCustomer(Data.getCustomerId(customerBox.getValue())));
    }

    /**
     * When a new customer is selected in the customerBox, appointmentTable is updated with the associated appointments
     * for the selected customer
     */
    @FXML
    private void updateTable() {
        appointmentTable.setItems(Data.getAppointmentsForCustomer(Data.getCustomerId(customerBox.getValue())));
    }

    /**
     * Closes the window
     */
    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
