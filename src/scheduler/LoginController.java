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
import java.util.Locale;

public class LoginController {

    @FXML
    private Label username;
    @FXML
    private Label password;
    @FXML
    private Label error;
    @FXML
    private Label loc;
    @FXML
    private Button submitButton;
    @FXML
    private Button exitButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    /**
     * Initialized the login window. If the Locale is set to French, everything is translated into French.
     */
    public void initialize() {
        username.setText(Main.language.getString("username"));
        password.setText(Main.language.getString("password"));
        error.setText(Main.language.getString("error"));
        loc.setText(Main.language.getString("location"));
        submitButton.setText(Main.language.getString("submit"));
        exitButton.setText(Main.language.getString("exit"));
        loc.setText(loc.getText().concat(Locale.getDefault().getDisplayCountry()));
    }

    /**
     * When the submit button is pressed, the entered username and password are checked against the userList in the
     * Datasource class. If a match is found, the user data is saved in the Main class and the main window is opened.
     * Otherwise, an error message is displayed
     */
    @FXML
    private void submit() {

        for (User user : Data.userList) {
            if (user.getUserName().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText())) {
                Main.currentUser = user;
                Data.logActivity(usernameField.getText(), passwordField.getText(), true);

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("main.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Scheduler");
                    stage.setScene(scene);
                    stage.show();

                    Stage oldStage = (Stage) submitButton.getScene().getWindow();
                    oldStage.close();

                    boolean upcomingAppointments = false;
                    ArrayList<Appointment> appointments = Data.getAppointmentsForUser(user.getId());
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
                    return;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (user.getUserName().equals(usernameField.getText())) {
                error.setVisible(true);
                Data.logActivity(usernameField.getText(), passwordField.getText(), false);
                String message = Main.language.getString("incorrect_password_for") + " " + usernameField.getText();
                Main.showAlert(Alert.AlertType.INFORMATION, Main.language.getString("incorrect_password"), message);
                return;
            }

        }
        String message = Main.language.getString("username") + " '" + usernameField.getText() + "' " +
                Main.language.getString("not_found") + ".";
        Main.showAlert(Alert.AlertType.INFORMATION, Main.language.getString("user_not_found"), message);
        Data.logActivity(usernameField.getText(), passwordField.getText(), false);
    }

    /**
     * Exits the application
     */
    @FXML
    private void exit() {
        Platform.exit();
    }


}
