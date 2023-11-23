module dev.uptc.loginfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires com.google.gson;
    requires okhttp3;


    opens dev.uptc.loginfx to javafx.fxml;
    opens dev.uptc.loginfx.model to com.google.gson;
    exports dev.uptc.loginfx.views;
    exports dev.uptc.loginfx.presenter;
    exports dev.uptc.loginfx.model;
    opens dev.uptc.loginfx.views to javafx.fxml;
}