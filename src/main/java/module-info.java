module isen.contactapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens isen.contactapp to javafx.fxml;
    exports isen.contactapp;
}