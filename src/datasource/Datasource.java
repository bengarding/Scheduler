package datasource;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
            getAllUsers();
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
        FileInputStream fis = null;
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
     * Pulls all appointments from the database
     *
     * @return ObservableList of all appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_APPOINTMENTS)) {

            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

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
            return appointmentList;

        } catch (SQLException e) {
            System.out.println("Creating appointmentList failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Pulls all customers from the database
     *
     * @return ObservableList of all customers
     */
    public static ObservableList<Customer> getAllCustomers() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_CUSTOMERS)) {

            ObservableList<Customer> customerList = FXCollections.observableArrayList();

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
            return customerList;

        } catch (SQLException e) {
            System.out.println("Creating customerList failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Pulls all users from the database and stores them in a static ArrayList
     */
    public static void getAllUsers() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_USERS)) {

//            StringBuilder sb = new StringBuilder("INSERT INTO users (Create_Date) VALUES ('");
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            LocalDateTime localDateTime = LocalDateTime.now();
//            sb.append(timestamp).append("');");
//            System.out.println(sb.toString());
////            statement.execute(sb.toString());

//            Locale locale = Locale.getDefault();
//            System.out.println(locale.toString());
//            System.out.println(locale.getLanguage());
//            System.out.println(Locale.ENGLISH.toString());
//            if (locale.getLanguage().equals(Locale.ENGLISH)){
//                System.out.println("english");
//            }

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
     * Extracts the division name from the first_level_division table in the database
     *
     * @param id the division_id to search with
     * @return String of the division name
     */
    public static String getDivisionName(int id) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT Division FROM " + TABLE_DIVISIONS + " WHERE Division_ID=" + id)) {
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract division name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts the country name from the country table in the database
     *
     * @param id the division_id to search with
     * @return String of the country name
     */
    public static String getCountryName(int id) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT Country FROM " + TABLE_COUNTRIES + " country INNER JOIN " +
                     TABLE_DIVISIONS + " division ON country.Country_ID = division.Country_ID WHERE division.Division_ID=" + id)) {
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract country name: " + e.getMessage());
            return null;
        }
    }

    public static String getContactName(int id) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT Contact_Name FROM contacts WHERE Contact_ID=" + id)) {
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            System.out.println("Failed to extract contact name: " + e.getMessage());
            return null;
        }
    }
}