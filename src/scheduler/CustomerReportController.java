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

public class CustomerReportController {

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private ComboBox<String> customerBox;
    @FXML
    private Button closeButton;

    public void initialize() {
        ObservableList<String> customerList = FXCollections.observableArrayList();
        for (Customer customer : Data.customerList) {
            customerList.add(customer.getName());
        }
        customerBox.setItems(customerList);
        customerBox.setValue(customerList.get(0));

        appointmentTable.setItems(Data.getAppointmentsForCustomer(Data.getCustomerID(customerBox.getValue())));
    }

    @FXML
    private void updateTable() {
        appointmentTable.setItems(Data.getAppointmentsForCustomer(Data.getCustomerID(customerBox.getValue())));
    }

    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
