module Scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.naming;

    opens scheduler;
    opens data to javafx.base;
}