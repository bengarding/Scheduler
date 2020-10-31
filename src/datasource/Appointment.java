package datasource;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;

    private SimpleStringProperty idProp;
    private SimpleStringProperty titleProp;
    private SimpleStringProperty descriptionProp;
    private SimpleStringProperty locationProp;
    private SimpleStringProperty typeProp;
    private SimpleStringProperty startProp;
    private SimpleStringProperty endProp;
    private SimpleStringProperty customerIdProp;
    private SimpleStringProperty contactProp;

    public static final String COLUMN_ID = "Appointment_ID";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_LOCATION = "Location";
    public static final String COLUMN_TYPE = "Type";
    public static final String COLUMN_START = "Start";
    public static final String COLUMN_END = "End";
    public static final String COLUMN_CREATE_DATE = "Create_Date";
    public static final String COLUMN_CREATED_BY = "Created_By";
    public static final String COLUMN_LAST_UPDATE = "Last_Update";
    public static final String COLUMN_LAST_UPDATED_BY = "Last_Updated_By";
    public static final String COLUMN_CUSTOMER_ID = "Customer_ID";
    public static final String COLUMN_USER_ID = "User_ID";
    public static final String COLUMN_CONTACT_ID = "Contact_ID";

    public static final int INDEX_ID = 1;
    public static final int INDEX_TITLE = 2;
    public static final int INDEX_DESCRIPTION = 3;
    public static final int INDEX_LOCATION = 4;
    public static final int INDEX_TYPE = 5;
    public static final int INDEX_START = 6;
    public static final int INDEX_END = 7;
    public static final int INDEX_CREATE_DATE = 8;
    public static final int INDEX_CREATED_BY = 9;
    public static final int INDEX_LAST_UPDATE = 10;
    public static final int INDEX_LAST_UPDATED_BY = 11;
    public static final int INDEX_CUSTOMER_ID = 12;
    public static final int INDEX_USER_ID = 13;
    public static final int INDEX_CONTACT_ID = 14;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.idProp = new SimpleStringProperty(String.valueOf(id));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleProp = new SimpleStringProperty(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.descriptionProp = new SimpleStringProperty(description);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        this.locationProp = new SimpleStringProperty(location);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeProp = new SimpleStringProperty(type);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start.toLocalDateTime();
        this.startProp = new SimpleStringProperty(new SimpleDateFormat("MM-dd-yy   HH:mm").format(start));
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end.toLocalDateTime();
        this.endProp = new SimpleStringProperty(new SimpleDateFormat("MM-dd-yy   HH:mm").format(end));
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        this.customerIdProp = new SimpleStringProperty(String.valueOf(customerId));
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
        this.contactProp = new SimpleStringProperty(Datasource.getContactName(contactId));
    }

    public String getIdProp() {
        return idProp.get();
    }

    public SimpleStringProperty idPropProperty() {
        return idProp;
    }

    public String getTitleProp() {
        return titleProp.get();
    }

    public SimpleStringProperty titlePropProperty() {
        return titleProp;
    }

    public String getDescriptionProp() {
        return descriptionProp.get();
    }

    public SimpleStringProperty descriptionPropProperty() {
        return descriptionProp;
    }

    public String getLocationProp() {
        return locationProp.get();
    }

    public SimpleStringProperty locationPropProperty() {
        return locationProp;
    }

    public String getTypeProp() {
        return typeProp.get();
    }

    public SimpleStringProperty typePropProperty() {
        return typeProp;
    }

    public String getStartProp() {
        return startProp.get();
    }

    public SimpleStringProperty startPropProperty() {
        return startProp;
    }

    public String getEndProp() {
        return endProp.get();
    }

    public SimpleStringProperty endPropProperty() {
        return endProp;
    }

    public String getCustomerIdProp() {
        return customerIdProp.get();
    }

    public SimpleStringProperty customerIdPropProperty() {
        return customerIdProp;
    }

    public String getContactProp() {
        return contactProp.get();
    }

    public SimpleStringProperty contactPropProperty() {
        return contactProp;
    }
}
