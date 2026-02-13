module isen.contactapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens isen.contactapp.view to javafx.fxml;
    exports isen.contactapp;
}