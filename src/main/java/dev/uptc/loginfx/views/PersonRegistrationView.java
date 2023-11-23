package dev.uptc.loginfx.views;

import dev.uptc.loginfx.model.Person;
import dev.uptc.loginfx.model.PersonListCell;
import dev.uptc.loginfx.presenter.PersonRegistrationPresenter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PersonRegistrationView {

    private ComboBox<String> countryComboBox;
    private PersonRegistrationPresenter presenter;
    private ListView<Person> personListView = new ListView<>();

    public PersonRegistrationView(ComboBox<String> countryComboBox, PersonRegistrationPresenter personRegistrationPresenter) {
        this.countryComboBox = countryComboBox;
        this.presenter = personRegistrationPresenter;
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Person Registration App");

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> presenter.registerPerson(idField, firstNameField, lastNameField, this.countryComboBox.getValue()));

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            presenter.editPerson(this.personListView);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> presenter.deletePerson(this.personListView));

        personListView.setCellFactory(param -> new PersonListCell());

        personListView.setItems(presenter.getPersonList());

        HBox inputBox = new HBox(idField, firstNameField, lastNameField, countryComboBox, registerButton, editButton, deleteButton);

        inputBox.setSpacing(
                10);
        VBox vbox = new VBox(inputBox, personListView);

        vbox.setSpacing(
                10);
        vbox.setPadding(
                new Insets(10));

        Scene scene = new Scene(vbox, 1000, 400);

        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
