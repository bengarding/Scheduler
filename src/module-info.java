module Dweller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires sqlite.jdbc;

    opens dweller;
    opens data to javafx.base;
    opens fxml to javafx.base;
}