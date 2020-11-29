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

/**
 * The ContactReportController class is the controller for contactReport.fxml
 *
 * @author Ben Garding
 */
public class ContactReportController {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private ComboBox<String> contactBox;
    @FXML
    private Button closeButton;

    /**
     * The contactBox is loaded with the names of all the contacts. The first contact in the list is selected and the
     * appointments that are associated with that contact are loaded into appointmentTable.
     */
    public void initialize() {
        ObservableList<String> contactList = FXCollections.observableArrayList();
        for (Contact contact : Data.contactList) {
            contactList.add(contact.getName());
        }
        contactBox.setItems(contactList);
        contactBox.setValue(contactList.get(0));

        appointmentTable.setItems(Data.getAppointmentsForContact(Data.getContactId(contactBox.getValue())));
    }

    /**
     * When a new contact is selected in the contactBox, appointmentTable is updated with the associated appointments
     * for the selected contact
     */
    @FXML
    private void updateTable() {
        appointmentTable.setItems(Data.getAppointmentsForContact(Data.getContactId(contactBox.getValue())));
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
