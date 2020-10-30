package scheduler;

import datasource.Appointment;
import datasource.Customer;
import datasource.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class Controller {

    @FXML
    TableView<Appointment> appointmentTable;

    @FXML
    TableView<Customer> customerTable;

    public void initialize() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(Datasource.getAllAppointments());
        ObservableList<Customer> customers = FXCollections.observableArrayList(Datasource.getAllCustomers());
        appointmentTable.setItems(appointments);
        customerTable.setItems(customers);

    }
}
