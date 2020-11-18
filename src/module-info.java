module Dweller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.naming;

    opens dweller;
    opens data to javafx.base;
}