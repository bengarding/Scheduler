package datasource;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Datasource {

    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_DIVISIONS = "first_level_divisions";
    public static final String TABLE_COUNTRIES = "countries";

    public static List<User> userList = new ArrayList<>();
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static ObservableList<Country> countryList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static ObservableList<Division> divisionList = FXCollections.observableArrayList();

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
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_APPOINTMENTS)) {

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
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_CUSTOMERS)) {

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
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_COUNTRIES)) {

            while (results.next()) {
                Country country = new Country();
                country.setId(results.getInt(Country.COUNTRY_ID));
                country.setName(results.getString(Country.COUNTRY_NAME));

                countryList.add(country);
            }
        } catch (SQLException e) {
            System.out.println("Failed to extract countries: " + e.getMessage());
        }
    }

    /**
     * Extracts all users from the database and stores them into a static ArrayList
     */
    public static void getAllUsers() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_USERS)) {

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
     * Extracts a single division name from the first_level_division table in the database
     *
     * @param id The division ID to search with
     * @return The division name
     */
    public static String getDivisionName(int id) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT " + Division.DIVISION_NAME + " FROM " + TABLE_DIVISIONS +
                     " WHERE " + Division.DIVISION_ID + "=" + id)) {
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
             ResultSet result = statement.executeQuery("SELECT " + Division.DIVISION_ID + " FROM " + TABLE_DIVISIONS +
                     " WHERE " + Division.DIVISION_NAME + "='" + name + "'")) {
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
             ResultSet result = statement.executeQuery("SELECT " + Country.COUNTRY_NAME + " FROM " + TABLE_COUNTRIES +
                     " country INNER JOIN " + TABLE_DIVISIONS + " division ON country." + Country.COUNTRY_ID +
                     " = division." + Country.COUNTRY_ID + " WHERE division." + Division.DIVISION_ID + "=" + id)) {

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
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT " + Contact.CONTACT_NAME + " FROM " +
                     TABLE_CONTACTS + " WHERE " + Contact.CONTACT_ID + "=" + id)) {

            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract contact name: " + e.getMessage());
            return null;
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
             ResultSet results = statement.executeQuery("SELECT *  FROM " + TABLE_DIVISIONS +
                     " WHERE " + Country.COUNTRY_ID + "=(SELECT " + Country.COUNTRY_ID + " FROM " + TABLE_COUNTRIES +
                     " WHERE " + Country.COUNTRY_NAME + "='" + country + "')")) {
            divisionList.clear();
            while (results.next()) {
                Division division = new Division();
                division.setId(results.getInt(Division.DIVISION_ID));
                division.setName(results.getString(Division.DIVISION_NAME));

                divisionList.add(division);
            }

        } catch (SQLException e) {
            System.out.println("Failed to extract divisions: " + e.getMessage());
        }
    }

    public static boolean newCustomer(Customer customer) {
        try (Statement statement = conn.createStatement()) {
            String currentUser = Main.currentUser.getUserName();

            statement.execute("INSERT INTO " + TABLE_CUSTOMERS + " (" + Customer.COLUMN_ID + ", " + Customer.COLUMN_NAME +
                    ", " + Customer.COLUMN_ADDRESS + ", " + Customer.COLUMN_POSTAL_CODE + ", " + Customer.COLUMN_PHONE + ", " +
                    Customer.COLUMN_CREATED_BY + ", " + Customer.COLUMN_LAST_UPDATED_BY + ", " + Customer.COLUMN_DIVISION_ID +
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

}