package data;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The Data class is in charge of connecting to the database and executing SQL statements. It also holds the values
 * for lists that are used throughout the application.
 *
 * @author Ben Garding
 */
public abstract class Data {

    public static List<User> userList = new ArrayList<>();
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static ObservableList<Country> countryList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static ObservableList<Division> divisionList = FXCollections.observableArrayList();
    public static ObservableList<Contact> contactList = FXCollections.observableArrayList();

    private static Connection conn;

    /**
     * Tries to establish a connection with the database and throws an exception if it fails
     */
    public static void open() {
        MysqlDataSource datasource = getMySQLDataSource();
        try {
            conn = datasource.getConnection();
            getAllContacts();
            getAllUsers();
            getAllCustomers();
            getAllCountries();
            getAllAppointments();
        } catch (SQLException e) {
            System.out.println("Failed loading database: " + e.getMessage());
        }
    }

    /**
     * Called by open() to establish database connection. Reads database URL and credentials from db.properties file
     *
     * @return MysqlDataSource object with URL and credentials stored in it
     */
    private static MysqlDataSource getMySQLDataSource() {
        Properties props = new Properties();
        FileInputStream fis;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream("src/res/db.properties");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }


    /**
     * Extracts all appointments from the database and stores them into a static ObservableArrayList
     */
    private static void getAllAppointments() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + Appointment.TABLE)) {

            while (results.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(results.getInt(Appointment.INDEX_ID));
                appointment.setTitle(results.getString(Appointment.INDEX_TITLE));
                appointment.setDescription(results.getString(Appointment.INDEX_DESCRIPTION));
                appointment.setLocation(results.getString(Appointment.INDEX_LOCATION));
                appointment.setType(results.getString(Appointment.INDEX_TYPE));
                appointment.setStart(results.getTimestamp(Appointment.INDEX_START));
                appointment.setEnd(results.getTimestamp(Appointment.INDEX_END));
                appointment.setCustomerId(results.getInt(Appointment.INDEX_CUSTOMER_ID));
                appointment.setUserId(results.getInt(Appointment.INDEX_USER_ID));
                appointment.setContactId(results.getInt(Appointment.INDEX_CONTACT_ID));

                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract appointments: " + e.getMessage());
        }
    }

    /**
     * Extracts all customers from the database and stores them into a static ObservableArrayList
     */
    private static void getAllCustomers() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + Customer.TABLE)) {

            while (results.next()) {
                if (results.getString(Customer.TYPE).equals(Customer.HOMEOWNER)) {
                    Homeowner homeowner = getHomeowner(results.getInt(Customer.INDEX_ID));

                    if (homeowner != null) {
                        homeowner.setId(results.getInt(Customer.INDEX_ID));
                        homeowner.setName(results.getString(Customer.INDEX_NAME));
                        homeowner.setAddress(results.getString(Customer.INDEX_ADDRESS));
                        homeowner.setPostalCode(results.getString(Customer.INDEX_POSTAL_CODE));
                        homeowner.setPhone(results.getString(Customer.INDEX_PHONE));
                        homeowner.setDivisionId(results.getInt(Customer.INDEX_DIVISION_ID));
                        homeowner.setType(Customer.HOMEOWNER);

                        customerList.add(homeowner);
                    }
                } else if (results.getString(Customer.TYPE).equals(ApartmentManager.APARTMENT_MANAGER)) {
                    ApartmentManager apartmentManager = getApartmentManager(results.getInt(Customer.INDEX_ID));

                    if (apartmentManager != null) {
                        apartmentManager.setId(results.getInt(Customer.INDEX_ID));
                        apartmentManager.setName(results.getString(Customer.INDEX_NAME));
                        apartmentManager.setAddress(results.getString(Customer.INDEX_ADDRESS));
                        apartmentManager.setPostalCode(results.getString(Customer.INDEX_POSTAL_CODE));
                        apartmentManager.setPhone(results.getString(Customer.INDEX_PHONE));
                        apartmentManager.setDivisionId(results.getInt(Customer.INDEX_DIVISION_ID));
                        apartmentManager.setType(Customer.APARTMENT_MANAGER);

                        customerList.add(apartmentManager);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract customers: " + e.getMessage());
        }
    }

    /**
     * Extracts all the countries from the database and stores them into a static ObservableArrayList
     */
    private static void getAllCountries() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + Country.TABLE)) {

            while (results.next()) {
                Country country = new Country();
                country.setId(results.getInt(Country.ID));
                country.setName(results.getString(Country.NAME));

                countryList.add(country);
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract countries: " + e.getMessage());
        }
    }

    /**
     * Extracts all the contacts from the database and stores them into a static ObservableArrayList
     */
    private static void getAllContacts() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + Contact.TABLE)) {

            while (results.next()) {
                Contact contact = new Contact();
                contact.setId(results.getInt(Contact.ID));
                contact.setName(results.getString(Contact.NAME));

                contactList.add(contact);
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract contacts: " + e.getMessage());
        }
    }

    /**
     * Extracts all users from the database and stores them into a static ArrayList
     */
    private static void getAllUsers() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + User.TABLE)) {

            while (results.next()) {
                User user = new User();
                user.setId(results.getInt(User.INDEX_ID));
                user.setUserName(results.getString(User.INDEX_NAME));
                user.setPassword(results.getString(User.INDEX_PASSWORD));

                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract users: " + e.getMessage());
        }
    }

    /**
     * Clears the current divisionList and then extracts all the divisions from the database for a specified country
     * and stores them into a static ObservableArrayList
     *
     * @param country The country name to be searched with
     */
    public static void getAllDivisions(String country) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT *  FROM " + Division.TABLE +
                     " WHERE " + Country.ID + "=(SELECT " + Country.ID + " FROM " + Country.TABLE +
                     " WHERE " + Country.NAME + "='" + country + "')")) {

            divisionList.clear();
            while (results.next()) {
                Division division = new Division();
                division.setId(results.getInt(Division.ID));
                division.setName(results.getString(Division.NAME));

                divisionList.add(division);
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract divisions: " + e.getMessage());
        }
    }

    /**
     * Extracts a single homeowner from the database
     *
     * @param customerId The customer ID of the homeowner
     * @return The homeowner
     */
    private static Homeowner getHomeowner(int customerId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + Homeowner.TABLE + " WHERE " + Homeowner.ID +
                     "=" + customerId)) {
            results.next();
            Homeowner homeowner = new Homeowner();
            homeowner.setDoors(results.getInt(Homeowner.DOORS));
            homeowner.setWindows(results.getInt(Homeowner.WINDOWS));
            homeowner.setRooms(results.getInt(Homeowner.ROOMS));
            homeowner.setYearBuilt(results.getInt(Homeowner.YEAR_BUILT));
            return homeowner;
        } catch (SQLException e) {
            System.out.println("Failed to extract homeowner: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts a single apartment manager from the database
     *
     * @param customerId The customer ID of the apartment manager
     * @return The apartment manager
     */
    private static ApartmentManager getApartmentManager(int customerId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + ApartmentManager.TABLE + " WHERE " + ApartmentManager.ID +
                     "=" + customerId)) {
            results.next();
            ApartmentManager apartmentManager = new ApartmentManager();
            apartmentManager.setBuildings(results.getInt(ApartmentManager.BUILDINGS));
            apartmentManager.setUnits(results.getInt(ApartmentManager.UNITS));

            return apartmentManager;
        } catch (SQLException e) {
            System.out.println("Failed to extract apartment manager: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts a single division name from the first_level_division table in the database
     *
     * @param id The division ID to search with
     * @return The division name
     */
    public static String getDivisionName(int id) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT " + Division.NAME + " FROM " + Division.TABLE +
                     " WHERE " + Division.ID + "=" + id)) {

            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract division name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts a single division id from the first_level_division table in the database
     *
     * @param name The division name to search with
     * @return The division ID
     */
    public static int getDivisionId(String name) {
        try (
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery("SELECT " + Division.ID + " FROM " + Division.TABLE +
                        " WHERE " + Division.NAME + "='" + name + "'")) {

            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract division id: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Extracts a single country name from the country table in the database
     *
     * @param id The division ID to search with
     * @return The country name
     */
    public static String getCountryName(int id) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT " + Country.NAME + " FROM " + Country.TABLE +
                     " country INNER JOIN " + Division.TABLE + " division ON country." + Country.ID +
                     " = division." + Country.ID + " WHERE division." + Division.ID + "=" + id)) {

            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract country name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts a single contact name from contactList
     *
     * @param id The contact ID to be searched with
     * @return The contact name
     */
    public static String getContactName(int id) {
        for (Contact contact : contactList) {
            if (contact.getId() == id) {
                return contact.getName();
            }
        }
        return null;
    }

    /**
     * Extracts a single contact ID from contactList
     *
     * @param name The contact name to be searched with
     * @return The contact ID
     */
    public static int getContactId(String name) {
        for (Contact contact : contactList) {
            if (contact.getName().equals(name)) {
                return contact.getId();
            }
        }
        return 0;
    }

    /**
     * Extracts a single customer ID from customerList
     *
     * @param name The contact name to be searched with
     * @return The contact ID
     */
    public static int getCustomerId(String name) {
        for (Customer customer : customerList) {
            if (customer.getName().equals(name)) {
                return customer.getId();
            }
        }
        return 0;
    }

    /**
     * Adds a new customer to the database.
     *
     * @param customer The new customer to be added
     * @return True if successful and false if not
     */
    private static boolean newCustomer(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            String currentUser = Main.currentUser.getUserName();

            statement.execute("INSERT INTO " + Customer.TABLE + " (" + Customer.ID + ", " + Customer.NAME +
                    ", " + Customer.ADDRESS + ", " + Customer.POSTAL_CODE + ", " + Customer.PHONE + ", " +
                    Customer.CREATED_BY + ", " + Customer.LAST_UPDATED_BY + ", " + Customer.DIVISION_ID + ", " + Customer.TYPE +
                    ") VALUES(" + customer.getId() + ", '" + customer.getName() + "', '" + customer.getAddress() + "', '" +
                    customer.getPostalCode() + "', '" + customer.getPhone() + "', '" + currentUser + "', '" + currentUser +
                    "', " + customer.getDivisionId() + ", '" + customer.getType() + "')");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to create new customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * First, a new customer is added to the database and if that is successful, then a new homeowner is added.
     * Clears the customerList and reloads it from the database.
     *
     * @param customer The homeowner to be added
     * @return True if successful and false if not
     */
    public static boolean newHomeowner(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            String currentUser = Main.currentUser.getUserName();

            if (newCustomer(customer)) {
                statement.execute("INSERT INTO " + Homeowner.TABLE + " (" + Homeowner.ID + ", " + Homeowner.YEAR_BUILT + ", " +
                        Homeowner.WINDOWS + ", " + Homeowner.DOORS + ", " + Homeowner.ROOMS + ", " + Homeowner.CREATED_BY + ", " +
                        Homeowner.LAST_UPDATED_BY + ") VALUES (" + customer.getId() + ", " + ((Homeowner) customer).getYearBuilt() +
                        ", " + ((Homeowner) customer).getWindows() + ", " + ((Homeowner) customer).getDoors() + ", " +
                        ((Homeowner) customer).getRooms() + ", '" + currentUser + "', '" + currentUser + "')");

                customerList.clear();
                getAllCustomers();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to create new homeowner: " + e.getMessage());
            return false;
        }
    }

    /**
     * First, a new customer is added to the database and if that is successful, then a new apartment is added.
     * Clears the customerList and reloads it from the database.
     *
     * @param customer The apartment to be added
     * @return True if successful and false if not
     */
    public static boolean newApartmentManager(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            String currentUser = Main.currentUser.getUserName();

            if (newCustomer(customer)) {
                statement.execute("INSERT INTO " + ApartmentManager.TABLE + " (" + ApartmentManager.ID + ", " + ApartmentManager.UNITS + ", " +
                        ApartmentManager.BUILDINGS + ", " + ApartmentManager.CREATED_BY + ", " + ApartmentManager.LAST_UPDATED_BY + ") VALUES (" +
                        customer.getId() + ", " + ((ApartmentManager) customer).getUnits() + ", " + ((ApartmentManager) customer).getBuildings() +
                        ", '" + currentUser + "', '" + currentUser + "')");

                customerList.clear();
                getAllCustomers();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to create new apartment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing customer in the database with new values.
     *
     * @param customer The edited customer information to be updated
     * @return True if successful and false if not
     */
    private static boolean editCustomer(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("UPDATE " + Customer.TABLE + " SET " + Customer.NAME + "='" + customer.getName() +
                    "', " + Customer.ADDRESS + "='" + customer.getAddress() + "', " + Customer.POSTAL_CODE +
                    "='" + customer.getPostalCode() + "', " + Customer.PHONE + "='" + customer.getPhone() + "', " +
                    Customer.LAST_UPDATE + "='" + LocalDateTime.now(ZoneOffset.UTC) + "', " + Customer.LAST_UPDATED_BY +
                    "='" + Main.currentUser.getUserName() + "', " + Customer.DIVISION_ID + "="
                    + customer.getDivisionId() + " WHERE " + Customer.ID + "=" + customer.getId());
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to edit customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing customer first, and if that is successful, then updates the homeowner.
     * Clears the customerList and reloads it from the database
     *
     * @param customer The homeowner to be updated
     * @return True if successful and false if not
     */
    public static boolean editHomeowner(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            if (editCustomer(customer)) {
                statement.execute("UPDATE " + Homeowner.TABLE + " SET " + Homeowner.YEAR_BUILT + "=" + ((Homeowner) customer).getYearBuilt() +
                        ", " + Homeowner.WINDOWS + "=" + ((Homeowner) customer).getWindows() + ", " + Homeowner.DOORS + "=" +
                        ((Homeowner) customer).getDoors() + ", " + Homeowner.ROOMS + "=" + ((Homeowner) customer).getRooms() + ", " +
                        Homeowner.LAST_UPDATE + "='" + LocalDateTime.now(ZoneOffset.UTC) + "', " + Homeowner.LAST_UPDATED_BY +
                        "='" + Main.currentUser.getUserName() + "' WHERE " + Homeowner.ID + "=" + customer.getId());

                customerList.clear();
                getAllCustomers();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to edit homeowner: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing customer first, and if that is successful, then updates the apartment.
     * Clears the customerList and reloads it from the database
     *
     * @param customer The apartment to be updated
     * @return True if successful and false if not
     */
    public static boolean editApartmentManager(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            if (editCustomer(customer)) {
                statement.execute("UPDATE " + ApartmentManager.TABLE + " SET " + ApartmentManager.UNITS + "=" + ((ApartmentManager) customer).getUnits() +
                        ", " + ApartmentManager.BUILDINGS + "=" + ((ApartmentManager) customer).getBuildings() + ", " + ApartmentManager.LAST_UPDATE +
                        "='" + LocalDateTime.now(ZoneOffset.UTC) + "', " + ApartmentManager.LAST_UPDATED_BY +
                        "='" + Main.currentUser.getUserName() + "' WHERE " + ApartmentManager.ID + "=" + customer.getId());

                customerList.clear();
                getAllCustomers();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to edit apartment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an existing customer from the database.
     *
     * @param customer The customer to be deleted
     * @return True if successful and false if not
     */
    private static boolean deleteCustomer(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("DELETE FROM " + Customer.TABLE + " WHERE " + Customer.ID + "=" + customer.getId());

            return true;
        } catch (SQLException e) {
            System.out.println("Failed to delete customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an existing homeowner from the database, then delete the associated customer
     * Clears the customerList and reloads it from the database
     *
     * @param customer The homeowner to be deleted
     * @return True is successful and false if not
     */
    public static boolean deleteHomeowner(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("DELETE FROM " + Homeowner.TABLE + " WHERE " + Homeowner.ID + "=" + customer.getId());
            deleteCustomer(customer);
            customerList.clear();
            getAllCustomers();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to delete homeowner: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an existing apartment manager from the database, then delete the associated customer
     * Clears the customerList and reloads it from the database
     *
     * @param customer The apartment manager to be deleted
     * @return True is successful and false if not
     */
    public static boolean deleteApartmentManager(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("DELETE FROM " + ApartmentManager.TABLE + " WHERE " + ApartmentManager.ID + "=" + customer.getId());
            deleteCustomer(customer);
            customerList.clear();
            getAllCustomers();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to delete apartment manager: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new appointment to the database. Clears the appointmentList and reloads it from the database
     *
     * @param appointment The new appointment to be added
     * @return True if successful and false if not
     */
    public static boolean newAppointment(Appointment appointment) {
        try {
            Statement statement = conn.createStatement();
            String currentUser = Main.currentUser.getUserName();

            statement.execute("INSERT INTO " + Appointment.TABLE + " (" + Appointment.ID + ", " + Appointment.TITLE +
                    ", " + Appointment.DESCRIPTION + ", " + Appointment.LOCATION + ", " + Appointment.TYPE + ", " +
                    Appointment.START + ", " + Appointment.END + ", " + Appointment.CREATED_BY + ", " + Appointment.LAST_UPDATED_BY +
                    ", " + Appointment.CUSTOMER_ID + ", " + Appointment.USER_ID + ", " + Appointment.CONTACT_ID + ") VALUES(" +
                    appointment.getId() + ", '" + appointment.getTitle() + "', '" + appointment.getDescription() + "', '" +
                    appointment.getLocation() + "', '" + appointment.getType() + "', '" + appointment.getStart() + "', '" +
                    appointment.getEnd() + "', '" + currentUser + "', '" + currentUser + "', " + appointment.getCustomerId() +
                    ", " + appointment.getUserId() + ", " + appointment.getContactId() + ")");

            appointmentList.clear();
            getAllAppointments();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to create new appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Edits an existing appointment in the database with new values. Clears the appointmentList and reloads it from the database
     *
     * @param appointment The edited appointment information to be updated
     * @return True if successful and false if not
     */
    public static boolean editAppointment(Appointment appointment) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("UPDATE " + Appointment.TABLE + " SET " + Appointment.TITLE + "='" + appointment.getTitle() +
                    "', " + Appointment.DESCRIPTION + "='" + appointment.getDescription() + "', " + Appointment.LOCATION +
                    "='" + appointment.getLocation() + "', " + Appointment.TYPE + "='" + appointment.getType() + "', " +
                    Appointment.START + "='" + appointment.getStart() + "', " + Appointment.END + "='" + appointment.getEnd() +
                    "', " + Appointment.LAST_UPDATE + "='" + LocalDateTime.now(ZoneOffset.UTC) + "', " + Appointment.LAST_UPDATED_BY +
                    "='" + Main.currentUser.getUserName() + "', " + Appointment.CUSTOMER_ID + "=" + appointment.getCustomerId() +
                    ", " + Appointment.CONTACT_ID + "=" + appointment.getContactId() + ", " + Appointment.USER_ID + "=" +
                    appointment.getUserId() + " WHERE " + Appointment.ID + "=" + appointment.getId());

            appointmentList.clear();
            getAllAppointments();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to edit appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an existing appointment from the database. Clears the appointmentList and reloads it from the database
     *
     * @param appointment The appointment to be deleted
     * @return True if successful and false if not
     */
    public static boolean deleteAppointment(Appointment appointment) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("DELETE FROM " + Appointment.TABLE + " WHERE " + Appointment.ID + "=" + appointment.getId());

            appointmentList.clear();
            getAllAppointments();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to delete appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if any customer is related to an appointment
     *
     * @param customerId The customer ID to check against all appointments
     * @return True if there are no matches and false if there are matches or there is an error
     */
    public static boolean safeToDelete(int customerId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + Appointment.TABLE + " appts INNER JOIN " +
                     Customer.TABLE + " custs ON appts." + Customer.ID + " = custs." + Customer.ID +
                     " WHERE custs." + Customer.ID + "=" + customerId)) {

            results.next();
            if (results.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to find match: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extracts all appointments for a specified customer
     *
     * @param customerId The customer ID to search with
     * @return ObservableArrayList of appointments
     */
    public static ObservableList<Appointment> getAppointmentsForCustomer(int customerId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT " + Appointment.ID + ", " + Appointment.TITLE + ", " +
                     Appointment.DESCRIPTION + ", appts." + Appointment.TYPE + ", " + Appointment.START + ", " + Appointment.END +
                     ", " + Appointment.CONTACT_ID + " FROM " + Appointment.TABLE + " appts INNER JOIN " + Customer.TABLE +
                     " custs ON appts." + Appointment.CUSTOMER_ID + " = custs." + Customer.ID + " WHERE custs." +
                     Customer.ID + "=" + customerId)) {

            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            while (results.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(results.getInt(Appointment.ID));
                appointment.setTitle(results.getString(Appointment.TITLE));
                appointment.setDescription(results.getString(Appointment.DESCRIPTION));
                appointment.setType(results.getString(Appointment.TYPE));
                appointment.setStart(results.getTimestamp(Appointment.START));
                appointment.setEnd(results.getTimestamp(Appointment.END));
                appointment.setContactId(results.getInt(Appointment.CONTACT_ID));

                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println("Failed to find appointments for customer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts all appointment times for a specified customer
     *
     * @param customerId The customer ID to search with
     * @return ArrayList of appointments that only has values for title, start, and end
     */
    public static ArrayList<Appointment> getAppointmentDatesForCustomer(int customerId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT " + Appointment.TITLE + ", " + Appointment.START + ", " +
                     Appointment.END + " FROM " + Appointment.TABLE + " appts INNER JOIN " + Customer.TABLE + " custs ON appts." +
                     Appointment.CUSTOMER_ID + " = custs." + Customer.ID + " WHERE custs." + Customer.ID + "=" + customerId)) {

            ArrayList<Appointment> appointments = new ArrayList<>();
            while (results.next()) {
                Appointment appointment = new Appointment();
                appointment.setTitle(results.getString(Appointment.TITLE));
                appointment.setStart(results.getTimestamp(Appointment.START));
                appointment.setEnd(results.getTimestamp(Appointment.END));
                appointments.add(appointment);
            }
            return appointments;

        } catch (SQLException e) {
            System.out.println("Failed to find appointment times for customer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts all appointment times for a specified user
     *
     * @param userId The user ID to search with
     * @return ArrayList of appointments that only has values for title and start
     */
    public static ArrayList<Appointment> getAppointmentDatesForUser(int userId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT " + Appointment.ID + ", " + Appointment.START + ", " +
                     Appointment.END + " FROM " + Appointment.TABLE + " appts INNER JOIN " + User.TABLE + " user ON appts." +
                     Appointment.USER_ID + " = user." + User.ID + " WHERE user." + User.ID + "=" + userId)) {

            ArrayList<Appointment> appointments = new ArrayList<>();
            while (results.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(results.getInt(Appointment.ID));
                appointment.setStart(results.getTimestamp(Appointment.START));
                appointment.setEnd(results.getTimestamp(Appointment.END));
                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println("Failed to find appointments for user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts all appointments for a specified contact
     *
     * @param contactId The contact ID to search with
     * @return ArrayList of appointments
     */
    public static ObservableList<Appointment> getAppointmentsForContact(int contactId) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT " + Appointment.ID + ", " + Appointment.TITLE + ", " +
                     Appointment.DESCRIPTION + ", " + Appointment.TYPE + ", " + Appointment.START + ", " + Appointment.END +
                     ", " + Appointment.CUSTOMER_ID + " FROM " + Appointment.TABLE + " appts INNER JOIN " + Contact.TABLE +
                     " contact ON appts." + Appointment.CONTACT_ID + " = contact." + Contact.ID + " WHERE contact." +
                     Contact.ID + "=" + contactId)) {

            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            while (results.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(results.getInt(Appointment.ID));
                appointment.setTitle(results.getString(Appointment.TITLE));
                appointment.setDescription(results.getString(Appointment.DESCRIPTION));
                appointment.setType(results.getString(Appointment.TYPE));
                appointment.setStart(results.getTimestamp(Appointment.START));
                appointment.setEnd(results.getTimestamp(Appointment.END));
                appointment.setCustomerId(results.getInt(Appointment.CUSTOMER_ID));

                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println("Failed to find appointments for contact: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts the name and count of each unique appointment type from the database
     *
     * @return ArrayList of results
     */
    public static ArrayList<Report> getTypeReport() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT DISTINCT " + Appointment.TYPE + ", COUNT(" + Appointment.TYPE +
                     ") AS Count FROM " + Appointment.TABLE + " GROUP BY " + Appointment.TYPE)) {

            ArrayList<Report> reports = new ArrayList<>();
            while (results.next()) {
                Report report = new Report();
                report.setType(results.getString(Appointment.TYPE));
                report.setCount(results.getInt("Count"));
                reports.add(report);
            }
            return reports;

        } catch (SQLException e) {
            System.out.println("Failed to extract type count: " + e.getMessage());
            return null;
        }
    }


}