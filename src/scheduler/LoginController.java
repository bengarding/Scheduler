package scheduler;

import data.Appointment;
import data.Data;
import data.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The LoginController class is the controller for login.fxml
 *
 * @author Ben Garding
 */
public class LoginController {
    @FXML
    private Label username;
    @FXML
    private Label password;
    @FXML
    private Label error;
    @FXML
    private Button submitButton;
    @FXML
    private Button exitButton;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    /**
     * If the Locale is set to French, everything is translated into French, including all error messages.
     */
    public void initialize() {
        username.setText(Main.language.getString("username"));
        password.setText(Main.language.getString("password"));
        error.setText(Main.language.getString("error"));
        submitButton.setText(Main.language.getString("submit"));
        exitButton.setText(Main.language.getString("exit"));
    }

    /**
     * When the submit button is pressed, the entered username and password are checked against the userList in the
     * Data class. If a match is found, the user data is saved in the Main class and main.fxml is opened.
     * Otherwise, an error message is displayed.
     */
    @FXML
    public boolean submit() {
        for (User user : Data.userList) {
            if (validate(user)) {
                Main.currentUser = user;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Scheduler");
                    stage.setScene(scene);
                    stage.show();

                    Stage oldStage = (Stage) submitButton.getScene().getWindow();
                    oldStage.close();

                    boolean upcomingAppointments = false;
                    ArrayList<Appointment> appointments = Data.getAppointmentDatesForUser(user.getId());
                    if (appointments != null) {
                        for (Appointment appointment : appointments) {
                            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15))) {
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M-d-u");
                                String message = Main.language.getString("upcoming_appointment_message") + ": \nID: " + appointment.getId() +
                                        "\n" + Main.language.getString("date") + ": " + appointment.getStart().toLocalDate().format(dateFormatter) +
                                        "\n" + Main.language.getString("time") + ": " + appointment.getStart().toLocalTime() +
                                        " - " + appointment.getEnd().toLocalTime();
                                Main.showAlert(Alert.AlertType.INFORMATION, Main.language.getString("upcoming_appointment"), message);
                                upcomingAppointments = true;
                                break;
                            }
                        }
                    }
                    if (!upcomingAppointments) {
                        Main.showAlert(Alert.AlertType.INFORMATION, Main.language.getString("no_upcoming_appointments"),
                                Main.language.getString("no_upcoming_appointments_message"));
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (user.getUserName().equals(usernameField.getText())) {
                error.setVisible(true);
                String message = Main.language.getString("incorrect_password_for") + " " + usernameField.getText();
                Main.showAlert(Alert.AlertType.INFORMATION, Main.language.getString("incorrect_password"), message);
                return false;
            }
        }
        String message = Main.language.getString("username") + " '" + usernameField.getText() + "' " +
                Main.language.getString("not_found") + ".";
        Main.showAlert(Alert.AlertType.INFORMATION, Main.language.getString("user_not_found"), message);
        return false;
    }

    public boolean validate(User user) {
        return user.getUserName().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText());
    }

    /**
     * Exits the application
     */
    @FXML
    private void exit() {
        Platform.exit();
    }

}
