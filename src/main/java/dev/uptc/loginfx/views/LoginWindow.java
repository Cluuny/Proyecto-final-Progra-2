package dev.uptc.loginfx.views;

import dev.uptc.loginfx.presenter.LoginPresenter;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow extends Application {

    @Override
    public void start(Stage stage) {
        LoginPresenter presenter;
        presenter = new LoginPresenter();
        presenter.setStage(stage);
        stage.setTitle("Inicio de Sesión");

        // Elementos de la interfaz de usuario
        Label usernameLabel = new Label("Nombre de Usuario:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Contraseña:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Iniciar Sesión");
        loginButton.getStyleClass().add("hover-button");

        //Imagen de banner
        Image bannerImage = new Image(presenter.loadBanner());
        ImageView bannerImageView = new ImageView(bannerImage);
        bannerImageView.setFitWidth(300);
        bannerImageView.setFitHeight(150);

        usernameField.setId("username-field");
        passwordField.setId("password-field");
        loginButton.setId("login-button");

        VBox formLayout = new VBox(10);
        formLayout.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-padding: 20; -fx-spacing: 10;");
        formLayout.getChildren().addAll(bannerImageView, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        formLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formLayout, 700, 400);
        scene.getStylesheets().add(presenter.loadCssPath().toURI().toString());


        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                presenter.handleLogin(usernameField.getText(), passwordField.getText());
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                presenter.handleLogin(usernameField.getText(), passwordField.getText());
            }
        });

        loginButton.setOnAction(event ->
                presenter.handleLogin(usernameField.getText(), passwordField.getText()));

        stage.setScene(scene);
        stage.show();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText("Credenciales incorrectas");
        alert.setContentText("Verifique su nombre de usuario y contraseña.");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
