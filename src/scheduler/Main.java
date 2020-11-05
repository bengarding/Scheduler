package scheduler;

import data.Data;
import data.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static ResourceBundle language = ResourceBundle.getBundle("res/lang", Locale.getDefault());
    public static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle(language.getString("scheduler"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        ZoneId.getAvailableZoneIds().stream().filter(c -> c.contains("UTC")).sorted().forEach(System.out::println);
//        LocalDate parisDate = LocalDate.of(2020, 10, 10);
//        LocalTime estStartTime = LocalTime.of(13,0);
//        ZoneId parisZoneId = ZoneId.of("Europe/Paris");
//        ZonedDateTime parisZDT = ZonedDateTime.of(parisDate, estStartTime, parisZoneId);
//        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
//
//        Instant parisToUTCInstant = parisZDT.toInstant();
//        ZonedDateTime parisToLocalZDT = parisZDT.withZoneSameLocal(localZoneId);
//        ZonedDateTime UTCToLocalZDT = parisToUTCInstant.atZone(localZoneId);
//
//        System.out.println("local: " + ZonedDateTime.now());
//        System.out.println("paris: " + parisZDT);
//        System.out.println("paris->utc: " + parisToUTCInstant);
//        System.out.println("utc->local: " + UTCToLocalZDT);
//        System.out.println("utc->local date only: " + UTCToLocalZDT.toLocalDate());

//        String date = String.valueOf(UTCToLocalZDT.toLocalDate());
//        System.out.println(String.valueOf(UTCToLocalZDT.toLocalDate()) + " " + String.valueOf(UTCToLocalZDT.toLocalTime())); // for sql

        Data.open();
        launch(args);
    }

    /**
     * Shows an alert
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


