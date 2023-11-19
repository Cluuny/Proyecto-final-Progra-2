module dev.uptc.loginfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens dev.uptc.loginfx to javafx.fxml;
    exports dev.uptc.loginfx;
    exports dev.uptc.loginfx.views;
    exports dev.uptc.loginfx.presenter;
    exports dev.uptc.loginfx.model;
    opens dev.uptc.loginfx.views to javafx.fxml;
}