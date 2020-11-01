package scheduler;

import datasource.Datasource;
import datasource.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static ResourceBundle language = ResourceBundle.getBundle("res/lang", Locale.getDefault());
    public static User currentUser;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        if (Locale.getDefault().getLanguage().equals("fr")) {
            primaryStage.setTitle(language.getString("scheduler"));
        } else {
            primaryStage.setTitle("Scheduler");
        }
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Datasource.open();
        launch(args);
    }
}


