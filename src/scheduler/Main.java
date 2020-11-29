package scheduler;

import data.Data;
import data.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static ResourceBundle language = ResourceBundle.getBundle("res/lang", Locale.getDefault());
    public static User currentUser;
    public static DateTimeFormatter zoneFormatter = DateTimeFormatter.ofPattern("zzz", Locale.getDefault());

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        primaryStage.setTitle(language.getString("scheduler"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Data.open();
        launch(args);

    }

    /**
     * Shows an alert with the passed content
     *
     * @param type        The type of alert to show
     * @param title       The title to display
     * @param contentText The content text to display
     */
    public static void showAlert(Alert.AlertType type, String title, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}


