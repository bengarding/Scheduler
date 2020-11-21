package dweller;

import data.*;
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

    @FXML
    private RadioButton homeownerRadio;
    @FXML
    private RadioButton apartmentManagerRadio;
    @FXML
    private Label yearBuiltLabel;
    @FXML
    private Label roomsLabel;
    @FXML
    private Label windowsLabel;
    @FXML
    private Label doorsLabel;
    @FXML
    private Label unitsLabel;
    @FXML
    private Label buildingsLabel;
    @FXML
    private Label yearBuiltError;
    @FXML
    private Label roomsError;
    @FXML
    private Label windowsError;
    @FXML
    private Label doorsError;
    @FXML
    private Label unitsError;
    @FXML
    private Label buildingsError;
    @FXML
    private TextField yearBuiltField;
    @FXML
    private TextField roomsField;
    @FXML
    private TextField windowsField;
    @FXML
    private TextField doorsField;
    @FXML
    private TextField unitsField;
    @FXML
    private TextField buildingsField;
    @FXML
    private ToggleGroup customerType;


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
        radioSelected();

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

        yearBuiltField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                yearBuiltError.setVisible(yearBuiltField.getText().isEmpty());
            }
        });

        yearBuiltField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                yearBuiltField.setText(s);
            }
        });

        roomsField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                roomsError.setVisible(roomsField.getText().isEmpty());
            }
        });

        roomsField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                roomsField.setText(s);
            }
        });

        windowsField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                windowsError.setVisible(windowsField.getText().isEmpty());
            }
        });

        windowsField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                windowsField.setText(s);
            }
        });

        doorsField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                doorsError.setVisible(doorsField.getText().isEmpty());
            }
        });

        doorsField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                doorsField.setText(s);
            }
        });

        unitsField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                unitsError.setVisible(unitsField.getText().isEmpty());
            }
        });

        unitsField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                unitsField.setText(s);
            }
        });

        buildingsField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                buildingsError.setVisible(buildingsField.getText().isEmpty());
            }
        });

        buildingsField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                buildingsField.setText(s);
            }
        });
    }

    /**
     * Shows the appropriate fields depending on which radio button is selected
     */
    @FXML
    private void radioSelected() {
        if (homeownerRadio.isSelected()) {
            yearBuiltLabel.setVisible(true);
            yearBuiltField.setVisible(true);
            roomsLabel.setVisible(true);
            roomsField.setVisible(true);
            windowsLabel.setVisible(true);
            windowsField.setVisible(true);
            doorsLabel.setVisible(true);
            doorsField.setVisible(true);
            unitsLabel.setVisible(false);
            unitsField.setVisible(false);
            unitsField.setText("");
            unitsError.setVisible(false);
            buildingsLabel.setVisible(false);
            buildingsField.setVisible(false);
            buildingsField.setText("");
            buildingsError.setVisible(false);
        } else {
            yearBuiltLabel.setVisible(false);
            yearBuiltField.setVisible(false);
            yearBuiltField.setText("");
            yearBuiltError.setVisible(false);
            roomsLabel.setVisible(false);
            roomsField.setVisible(false);
            roomsField.setText("");
            roomsError.setVisible(false);
            windowsLabel.setVisible(false);
            windowsField.setVisible(false);
            windowsField.setText("");
            windowsError.setVisible(false);
            doorsLabel.setVisible(false);
            doorsField.setVisible(false);
            doorsField.setText("");
            doorsError.setVisible(false);
            unitsLabel.setVisible(true);
            unitsField.setVisible(true);
            buildingsLabel.setVisible(true);
            buildingsField.setVisible(true);
        }
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
     * If all controls are valid, saves a new homeowner or apartment to the database. If the customerEdit boolean is set to true,
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
        if (homeownerRadio.isSelected()) {
            if (yearBuiltField.getText().isEmpty()) {
                yearBuiltError.setVisible(true);
                isValid = false;
            }
            if (windowsField.getText().isEmpty()) {
                windowsError.setVisible(true);
                isValid = false;
            }
            if (roomsField.getText().isEmpty()) {
                roomsError.setVisible(true);
                isValid = false;
            }
            if (doorsField.getText().isEmpty()) {
                doorsError.setVisible(true);
                isValid = false;
            }
        } else {
            if (unitsField.getText().isEmpty()) {
                unitsError.setVisible(true);
                isValid = false;
            }
            if (buildingsField.getText().isEmpty()) {
                buildingsError.setVisible(true);
                isValid = false;
            }
        }

        int divisionID = Data.getDivisionId(divisionBox.getValue());
        if (divisionID == 0) {
            Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error connecting to the database");
            return;
        }
        if (!isValid) {
            Main.showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in each field");
            return;
        }

        if (homeownerRadio.isSelected()) {
            Homeowner homeowner = new Homeowner(Integer.parseInt(idField.getText()), nameField.getText(), addressField.getText(),
                    postalCodeField.getText(), phoneField.getText(), divisionID, countryBox.getValue(), Customer.HOMEOWNER,
                    Integer.parseInt(yearBuiltField.getText()), Integer.parseInt(windowsField.getText()),
                    Integer.parseInt(doorsField.getText()), Integer.parseInt(roomsField.getText()));

            if (customerEdit && Data.editHomeowner(homeowner)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Homeowner Edited", "Homeowner successfully updated");
                return;
            } else if (customerEdit) {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
                return;
            }
            if (Data.newHomeowner(homeowner)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Homeowner Added", "Homeowner successfully added to the database");
            } else {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
            }
        } else {
            ApartmentManager apartmentManager = new ApartmentManager(Integer.parseInt(idField.getText()), nameField.getText(), addressField.getText(),
                    postalCodeField.getText(), phoneField.getText(), divisionID, countryBox.getValue(), Customer.APARTMENT_MANAGER,
                    Integer.parseInt(unitsField.getText()), Integer.parseInt(buildingsField.getText()));

            if (customerEdit && Data.editApartmentManager(apartmentManager)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Apartment Manager Edited", "Apartment manager successfully updated");
                return;
            } else if (customerEdit) {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
                return;
            }
            if (Data.newApartmentManager(apartmentManager)) {
                close();
                Main.showAlert(Alert.AlertType.INFORMATION, "Apartment Manager Added", "Apartment manager successfully added to the database");
            } else {
                Main.showAlert(Alert.AlertType.WARNING, "Database Error", "There was an error saving to the database");
            }
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

        if (customer.getType().equals(Customer.HOMEOWNER)) {
            yearBuiltField.setText(String.valueOf(((Homeowner) customer).getWindows()));
            windowsField.setText(String.valueOf(((Homeowner) customer).getWindows()));
            doorsField.setText(String.valueOf(((Homeowner) customer).getDoors()));
            roomsField.setText(String.valueOf(((Homeowner) customer).getRooms()));
            apartmentManagerRadio.setDisable(true);
        } else {
            apartmentManagerRadio.setSelected(true);
            radioSelected();
            homeownerRadio.setDisable(true);
            unitsField.setText(String.valueOf(((ApartmentManager) customer).getUnits()));
            buildingsField.setText(String.valueOf(((ApartmentManager) customer).getBuildings()));
        }

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
