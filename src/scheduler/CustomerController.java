package scheduler;

import datasource.Country;
import datasource.Customer;
import datasource.Datasource;
import datasource.Division;
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
    private TextField cityField;
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
    private Label cityError;
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

        for (Country country : Datasource.countryList) {
            countryNames.add(country.getName());
        }
        countryBox.setItems(countryNames);
        idField.setText(String.valueOf(Datasource.customerList.get(Datasource.customerList.size() - 1).getId() + 1));

        countryBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                ObservableList<String> divisionNames = FXCollections.observableArrayList();
                Datasource.getAllDivisions(countryBox.getValue());
                for (Division division : Datasource.divisionList) {
                    divisionNames.add(division.getName());
                }
                divisionBox.setItems(divisionNames);
                divisionBox.autosize();
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

        cityField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                cityError.setVisible(cityField.getText().isEmpty());
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
        if (cityField.getText().isEmpty()) {
            cityError.setVisible(true);
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
        int divisionID = Datasource.getDivisionId(divisionBox.getValue());
        if (divisionID == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error connecting to the database");
            alert.showAndWait();
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

        if (!Datasource.newCustomer(customer)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error saving to the database");
            alert.showAndWait();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer Added");
        alert.setHeaderText(null);
        alert.setContentText("Customer successfully added to the database");
        alert.showAndWait();
        close();

    }


}
