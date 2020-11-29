module Scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires mysql.connector.java;

    opens scheduler;
    opens data to javafx.base;
    opens fxml to javafx.base;
}