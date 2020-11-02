package data;

import javafx.beans.property.SimpleStringProperty;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private String country;

    private SimpleStringProperty idProp;
    private SimpleStringProperty nameProp;
    private SimpleStringProperty phoneProp;
    private SimpleStringProperty addressProp;
    private SimpleStringProperty postalCodeProp;
    private SimpleStringProperty divisionProp;
    private SimpleStringProperty countryProp;

    public static final String COLUMN_ID = "Customer_ID";
    public static final String COLUMN_NAME = "Customer_Name";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_POSTAL_CODE = "Postal_Code";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_CREATE_DATE = "Create_Date";
    public static final String COLUMN_CREATED_BY = "Created_By";
    public static final String COLUMN_LAST_UPDATE = "Last_Update";
    public static final String COLUMN_LAST_UPDATED_BY = "Last_Updated_By";
    public static final String COLUMN_DIVISION_ID = "Division_ID";

    public static final int INDEX_ID = 1;
    public static final int INDEX_NAME = 2;
    public static final int INDEX_ADDRESS = 3;
    public static final int INDEX_POSTAL_CODE = 4;
    public static final int INDEX_PHONE = 5;
    public static final int INDEX_CREATE_DATE = 6;
    public static final int INDEX_CREATED_BY = 7;
    public static final int INDEX_LAST_UPDATE = 8;
    public static final int INDEX_LAST_UPDATED_BY = 9;
    public static final int INDEX_DIVISION_ID = 10;

    public Customer(int id, String name, String address, String postalCode, String phone, int divisionId, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.country = country;
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
}