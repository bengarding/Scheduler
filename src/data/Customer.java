package data;

import javafx.beans.property.SimpleStringProperty;

/**
 * The Customer class creates a customer and stores static final variables used for SQL statements
 *
 * @author Ben Garding
 */
public abstract class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private String country;
    private String type;

    private SimpleStringProperty idProp;
    private SimpleStringProperty nameProp;
    private SimpleStringProperty phoneProp;
    private SimpleStringProperty addressProp;
    private SimpleStringProperty postalCodeProp;
    private SimpleStringProperty divisionProp;
    private SimpleStringProperty countryProp;
    private SimpleStringProperty typeProp;

    public static final String TABLE = "customers";
    public static final String ID = "Customer_ID";
    public static final String NAME = "Customer_Name";
    public static final String ADDRESS = "Address";
    public static final String POSTAL_CODE = "Postal_Code";
    public static final String PHONE = "Phone";
    public static final String CREATED_BY = "Created_By";
    public static final String LAST_UPDATE = "Last_Update";
    public static final String LAST_UPDATED_BY = "Last_Updated_By";
    public static final String DIVISION_ID = "Division_ID";
    public static final String TYPE = "Type";
    public static final String HOMEOWNER = "Homeowner";
    public static final String APARTMENT_MANAGER = "Apartment Manager";

    public static final int INDEX_ID = 1;
    public static final int INDEX_NAME = 2;
    public static final int INDEX_ADDRESS = 3;
    public static final int INDEX_POSTAL_CODE = 4;
    public static final int INDEX_PHONE = 5;
    public static final int INDEX_DIVISION_ID = 10;
    public static final int INDEX_TYPE = 11;

    public Customer(int id, String name, String address, String postalCode, String phone, int divisionId, String country, String type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.country = country;
        this.type = type;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.idProp = new SimpleStringProperty(String.valueOf(id));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProp = new SimpleStringProperty(name);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.addressProp = new SimpleStringProperty(address);
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        this.postalCodeProp = new SimpleStringProperty(postalCode);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.phoneProp = new SimpleStringProperty(phone);
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
        this.divisionProp = new SimpleStringProperty(Data.getDivisionName(divisionId));
        String country = Data.getCountryName(divisionId);
        this.country = country;
        this.countryProp = new SimpleStringProperty(country);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIdProp() {
        return idProp.get();
    }

    public SimpleStringProperty idPropProperty() {
        return idProp;
    }

    public String getNameProp() {
        return nameProp.get();
    }

    public SimpleStringProperty namePropProperty() {
        return nameProp;
    }

    public String getAddressProp() {
        return addressProp.get();
    }

    public SimpleStringProperty addressPropProperty() {
        return addressProp;
    }

    public String getPostalCodeProp() {
        return postalCodeProp.get();
    }

    public SimpleStringProperty postalCodePropProperty() {
        return postalCodeProp;
    }

    public String getPhoneProp() {
        return phoneProp.get();
    }

    public SimpleStringProperty phonePropProperty() {
        return phoneProp;
    }

    public String getDivisionProp() {
        return divisionProp.get();
    }

    public SimpleStringProperty divisionPropProperty() {
        return divisionProp;
    }

    public String getCountryProp() {
        return countryProp.get();
    }

    public SimpleStringProperty countryPropProperty() {
        return countryProp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeProp = new SimpleStringProperty(type);
    }

    public String getTypeProp() {
        return typeProp.get();
    }

    public SimpleStringProperty typePropProperty() {
        return typeProp;
    }

    public void setTypeProp(String typeProp) {
        this.typeProp.set(typeProp);
    }
}
