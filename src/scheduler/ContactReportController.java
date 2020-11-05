package scheduler;

import data.Appointment;
import data.Contact;
import data.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ContactReportController {

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private ComboBox<String> contactBox;
    @FXML
    private Button closeButton;

    public void initialize() {
        ObservableList<String> contactList = FXCollections.observableArrayList();
        for (Contact contact : Data.contactList) {
            contactList.add(contact.getName());
        }
        contactBox.setItems(contactList);
        contactBox.setValue(contactList.get(0));

        appointmentTable.setItems(Data.getAppointmentsForContact(Data.getContactID(contactBox.getValue())));
    }

    @FXML
    private void updateTable() {
        appointmentTable.setItems(Data.getAppointmentsForContact(Data.getContactID(contactBox.getValue())));
    }

    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
