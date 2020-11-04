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

public class Data {


    public static List<User> userList = new ArrayList<>();
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static ObservableList<Country> countryList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static ObservableList<Division> divisionList = FXCollections.observableArrayList();
    public static ObservableList<Contact> contactList = FXCollections.observableArrayList();

    private static Connection conn;

    /**
     * Tries to establish a connection with the database and throws an exception if it fails
     *
     * @return true if successful and false if unsuccessful
     */
    public static boolean open() {
        MysqlDataSource datasource = getMySQLDataSource();
        try {
            conn = datasource.getConnection();
            getAllCustomers();
            getAllUsers();
            getAllCountries();
            getAllAppointments();
            getAllContacts();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed loading database: " + e.getMessage());
            return false;
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
    public static void getAllAppointments() {

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
            System.out.println("Creating appointmentList failed: " + e.getMessage());
        }
    }

    /**
     * Extracts all customers from the database and stores them into a static ObservableArrayList
     */
    public static void getAllCustomers() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + Customer.TABLE)) {

            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(Customer.INDEX_ID));
                customer.setName(results.getString(Customer.INDEX_NAME));
                customer.setAddress(results.getString(Customer.INDEX_ADDRESS));
                customer.setPostalCode(results.getString(Customer.INDEX_POSTAL_CODE));
                customer.setPhone(results.getString(Customer.INDEX_PHONE));
                customer.setDivisionId(results.getInt(Customer.INDEX_DIVISION_ID));

                customerList.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("Creating customerList failed: " + e.getMessage());
        }
    }

    /**
     * Extracts all the countries from the database and stores them into a static ObservableArrayList
     */
    public static void getAllCountries() {
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
    public static void getAllContacts() {
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
    public static void getAllUsers() {

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
            System.out.println("Creating userList failed: " + e.getMessage());
        }
    }

    /**
     * Clears the current divisionList and then extracts all the divisions from the database for a specified country
     * and stores them into a static ObservableArrayList
     *
     * @param country The country to be searched with
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
        try (Statement statement = conn.createStatement();
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
     * Extracts a single contact name from the contact table in the database
     *
     * @param id The contact ED to be searched with
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

    public static boolean newCustomer(Customer customer) {
        try (Statement statement = conn.createStatement()) {
            String currentUser = Main.currentUser.getUserName();

            statement.execute("INSERT INTO " + Customer.TABLE + " (" + Customer.ID + ", " + Customer.NAME +
                    ", " + Customer.ADDRESS + ", " + Customer.POSTAL_CODE + ", " + Customer.PHONE + ", " +
                    Customer.CREATED_BY + ", " + Customer.LAST_UPDATED_BY + ", " + Customer.DIVISION_ID +
                    ") VALUES(" + customer.getId() + ", '" + customer.getName() + "', '" + customer.getAddress() + "', '" +
                    customer.getPostalCode() + "', '" + customer.getPhone() + "', '" + currentUser + "', '" + currentUser +
                    "', " + customer.getDivisionId() + ")");

            customerList.clear();
            getAllCustomers();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to create new customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Edits an existing customer in the database with new values. Clears the customerList and reloads it from the database
     *
     * @param customer The new customer information to be updated
     * @return True if successful and false if not
     */
    public static boolean editCustomer(Customer customer) {
        try (Statement statement = conn.createStatement()) {
            statement.execute("UPDATE " + Customer.TABLE + " SET " + Customer.NAME + "='" + customer.getName() +
                    "', " + Customer.ADDRESS + "='" + customer.getAddress() + "', " + Customer.POSTAL_CODE +
                    "='" + customer.getPostalCode() + "', " + Customer.PHONE + "='" + customer.getPhone() + "', " +
                    Customer.LAST_UPDATE + "='" + LocalDateTime.now(ZoneOffset.UTC) + "', " + Customer.LAST_UPDATED_BY +
                    "='" + Main.currentUser.getUserName() + "', " + Customer.DIVISION_ID + "="
                    + customer.getDivisionId() + " WHERE " + Customer.ID + "=" + customer.getId());

            customerList.clear();
            getAllCustomers();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to edit customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an existing customer from the database. Clears the customerList and reloads it from the database
     *
     * @param customer The customer to be deleted
     * @return True if successful and false if not
     */
    public static boolean deleteCustomer(Customer customer) {
        try (Statement statement = conn.createStatement()) {
            statement.execute("DELETE FROM " + Customer.TABLE + " WHERE " + Customer.ID + "=" + customer.getId());

            customerList.clear();
            getAllCustomers();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to delete customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if any customer is related to an appointment
     *
     * @param customerID The customer ID to check against all appointments
     * @return True if there are no matches and false if there are matches or there is an error
     */
    public static boolean safeToDelete(int customerID) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + Appointment.TABLE + " appts INNER JOIN " +
                     Customer.TABLE + " custs ON appts." + Customer.ID + " = custs." + Customer.ID +
                     " WHERE custs." + Customer.ID + "=" + customerID)) {
            results.next();
            if (results.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to find match: " + e.getMessage());
        }
        return false;
    }

    public static ResultSet getAppointmentsForCustomer(int customerID) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT " + Appointment.ID + ", " + Appointment.START + ", " +
                     Appointment.END + " FROM " + Appointment.TABLE + " appts INNER JOIN " + Customer.TABLE +
                     " custs ON appts." + Appointment.CUSTOMER_ID + " = custs." + Customer.ID + " WHERE custs." +
                     Customer.ID + "=" + customerID)) {

            return results;
        } catch (SQLException e) {
            System.out.println("Failed to find appointments for customer: " + e.getMessage());
            return null;
        }
    }
}