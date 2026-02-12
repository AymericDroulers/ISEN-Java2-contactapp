module isen.contactapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens isen.contactapp to javafx.fxml;
    exports isen.contactapp;
}