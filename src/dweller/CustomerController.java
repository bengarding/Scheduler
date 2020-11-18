package dweller;

import data.Country;
import data.Customer;
import data.Data;
import data.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * The CustomerController class is the controller for customer.fxml
 *
 * @author Ben Garding
 */
public class CustomerController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private ComboBox<String> countryBox;
    @FXML
    private ComboBox<String> divisionBox;
    @FXML
    private Label nameError;
    @FXML
    private Label phoneError;
    @FXML
    private Label addressError;
    @FXML
    private Label postalCodeError;
    @FXML
    private Label countryError;
    @FXML
    private Label divisionError;
    @FXML
    private Button cancelButton;

    boolean customerEdit = false;

    /**
     * The countryBox is loaded with the names of all countries. The idField is loaded with the next integer value after
     * the last customer, and will used when creating a new customer. idField is disabled so the user cannot alter the
     * value. Listeners are created on every input control. These listeners run input validations.
     */
    public void initialize() {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        for (Country country : Data.countryList) {
            countryNames.add(country.getName());
        }
        countryBox.setItems(countryNames);
        idField.setText(String.valueOf(Data.customerList.get(Data.customerList.size() - 1).getId() + 1));

        countryBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                setDivisionBox();
            }
            if (t1) {
                divisionBox.setValue("");
            }
        });

        nameField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                nameError.setVisible(nameField.getText().isEmpty());
            }
        });

        phoneField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                phoneError.setVisible(phoneField.getText().isEmpty());
            }
        });

        phoneField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("[0-9 -]*")) {
                phoneField.setText(s);
            }
        });

        addressField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                addressError.setVisible(addressField.getText().isEmpty());
            }
        });

        postalCodeField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                postalCodeError.setVisible(postalCodeField.getText().isEmpty());
            }
        });
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
     * If all controls are valid, saves a new customer to the database. If the customerEdit boolean is set to true,
     * then an existing customer is updated rather than a new one being created
     */
    @FXML
    private void save() {
        boolean isValid = true;
        if (nameField.getText().isEmpty()) {
            nameError.setVisible(true);
            isValid = false;
        }
        if (phoneField.getText().isEmpty()) {
            phoneError.setVisible(true);
            isValid = false;
        }
        if (addressField.getText().isEmpty()) {
            addressError.setVisible(true);
            isValid = false;
        }
        if (postalCodeField.getText().isEmpty()) {
            postalCodeError.setVisible(true);
            isValid = false;
        }
        if (countryBox.getValue() == null) {
            countryError.setVisible(true);
            isValid = false;
        }
        if (divisionBox.getValue() == null) {
            divisionError.setVisible(true);
            isValid = false;
        }
        int divisionID = Data.getDivisionId(divisionBox.getValue());
        if (divisionID == 0) {
            Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error connecting to the database");
            return;
        }
        if (!isValid) {
            Main.showAlert(Alert.AlertType.WARNING, "Mission Information", "Please fill in each field");
            return;
        }

        Customer customer = new Customer(Integer.parseInt(idField.getText()), nameField.getText(), addressField.getText(),
                postalCodeField.getText(), phoneField.getText(), divisionID, countryBox.getValue());

        if (customerEdit && Data.editCustomer(customer)) {
            close();
            Main.showAlert(Alert.AlertType.INFORMATION, "Customer Edited", "Customer successfully updated");
            return;
        } else if (customerEdit) {
            Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
            return;
        }

        if (Data.newCustomer(customer)) {
            close();
            Main.showAlert(Alert.AlertType.INFORMATION, "Customer Added", "Customer successfully added to the database");
        } else {
            Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
        }
    }

    /**
     * This method is called when the user selects a customer to edit from main.fxml. All of the existing values are
     * loaded into the appropriate controls. The customerEdit boolean is set to true so that upon saving, the
     * customer is edited instead of a new one being created.
     *
     * @param customer The customer to load the data from
     */
    public void editCustomer(Customer customer) {
        idField.setText(customer.getIdProp());
        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        countryBox.setValue(customer.getCountry());
        divisionBox.setValue(customer.getDivisionProp());

        customerEdit = true;

        setDivisionBox();
    }

    /**
     * Populated the divisionBox based on the country selected in the countryBox
     */
    private void setDivisionBox() {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        Data.getAllDivisions(countryBox.getValue());
        for (Division division : Data.divisionList) {
            divisionNames.add(division.getName());
        }
        divisionBox.setItems(divisionNames);
        divisionBox.autosize();
    }
}
