package scheduler;

import data.Country;
import data.Customer;
import data.Data;
import data.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    @FXML
    private Button saveButton;

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
     * Closes the window without saving
     */
    @FXML
    private void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in each field");
            alert.showAndWait();
            return;
        }

        Customer customer = new Customer(Integer.parseInt(idField.getText()), nameField.getText(), addressField.getText(),
                postalCodeField.getText(), phoneField.getText(), divisionID, countryBox.getValue());

        for (Customer cust : Data.customerList) {
            if (cust.getId() == customer.getId() && Data.editCustomer(customer)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Customer Edited", "Customer successfully updated");
                return;
            } else if (cust.getId() == customer.getId()) {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
                return;
            }
        }

        if (Data.newCustomer(customer)) {
            close();
            Main.showAlert(Alert.AlertType.INFORMATION, "Customer Added", "Customer successfully added to the database");
        } else {
            Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
        }
    }

    /**
     * Loads customer data into customer.fxml when a customer is to be edited
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
