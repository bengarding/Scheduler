package scheduler;

import datasource.Datasource;
import datasource.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
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
     * Initialized the login window. If the Locale is set to French, everything is translated to French.
     */
    public void initialize() {
        submitButton.setDefaultButton(true);
        exitButton.setCancelButton(true);

        if (Locale.getDefault().getLanguage().equals("fr")) {
            username.setText(Main.language.getString("username"));
            password.setText(Main.language.getString("password"));
            String translate = Main.language.getString("incorrect") + " " + Main.language.getString("username") + " " +
                    Main.language.getString("or") + " " + Main.language.getString("password");
            error.setText(translate);
            translate = Main.language.getString("location") + ": ";
            System.out.println(translate);
            loc.setText(translate);
            submitButton.setText(Main.language.getString("submit"));
            exitButton.setText(Main.language.getString("exit"));
        }
        loc.setText(loc.getText().concat(Locale.getDefault().getDisplayCountry()));

    }

    /**
     * When the submit button is pressed, the entered username and password are checked against the userList in the
     * Datasource class. If a match is found, the user data is saved in the Main class and the main window is opened.
     * Otherwise, an error message is displayed
     */
    @FXML
    private void submit() {
        for (User user : Datasource.userList) {
            if (user.getUserName().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText())) {
                Main.currentUser = user;

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

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                error.setVisible(true);
            }
        }
    }

    /**
     * Exits the application
     */
    @FXML
    private void exit() {
        Platform.exit();
    }


}
