package dev.uptc.loginfx.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.uptc.loginfx.model.Country;
import dev.uptc.loginfx.model.Person;
import dev.uptc.loginfx.views.PersonRegistrationView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonRegistrationPresenter extends Application {
    private final ComboBox<String> countryComboBox = new ComboBox<>();
    private final ObservableList<Person> personList = FXCollections.observableArrayList();
    private static final String COUNTRIES_API_URL = "https://restcountries.com/v3.1/all";
    private final Set<String> existingIds = new HashSet<>(); // Conjunto de IDs existentes
    private ListView<Person> personListView;
    private PersonRegistrationView view;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/javafxdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private Connection dbConnection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Conectarse a la base de datos
        try {
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores de conexi?n
        }

        fetchCountries();
        this.view = new PersonRegistrationView(this.countryComboBox, this);
        this.personListView = new ListView<>();
        this.view.start(primaryStage);

        // Cargar datos de la base de datos en la lista observable al inicio
        loadDataFromDatabase();
    }

    public void registerPerson(TextField idField, TextField firstNameField, TextField lastNameField, String country) {
        String id = idField.getText();
        String firstName = firstNameField.getText().toUpperCase(); // Convertir a may?sculas
        String lastName = lastNameField.getText().toUpperCase();   // Convertir a may?sculas

        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || country == null) {
            view.showAlert("Incomplete Information", "Please fill in all the fields.");
            return;
        }

        // Verificar si el ID ya existe
        if (existingIds.contains(id)) {
            view.showAlert("Duplicate ID", "This ID is already in use. Please choose a different ID.");
            return;
        }

        Person person = new Person(id, firstName, lastName, country);
        personList.add(person);
        existingIds.add(id); // Agregar el ID al conjunto de IDs existentes
        saveData(person);
        idField.clear();
        firstNameField.clear();
        lastNameField.clear();
        view.showAlert("Person Registered", "Person information saved successfully.");
        personListView.setItems(personList); // Actualizar la lista observable
    }

    public void editPerson(ListView<Person> personListView) {
        Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            view.showAlert("No Person Selected", "Please select a person to edit.");
            return;
        }

        // Create a dialog for editing the selected person
        Dialog<Person> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Edit Person");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create and configure the grid for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField(selectedPerson.getId());
        idField.setDisable(true); // Disable editing of the ID field
        TextField firstNameField = new TextField(selectedPerson.getFirstName());
        TextField lastNameField = new TextField(selectedPerson.getLastName());
        ComboBox<String> dialogComboBox = new ComboBox<>(this.countryComboBox.getItems());
        dialogComboBox.getSelectionModel().select(selectedPerson.getCountry());

        countryComboBox.setValue(selectedPerson.getCountry());
        countryComboBox.requestFocus();

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Last Name:"), 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(new Label("Country:"), 0, 3);
        grid.add(dialogComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a person when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                selectedPerson.setFirstName(firstNameField.getText().toUpperCase()); // Convert to uppercase
                selectedPerson.setLastName(lastNameField.getText().toUpperCase());   // Convert to uppercase
                selectedPerson.setCountry(dialogComboBox.getValue().toUpperCase());
                updateData(selectedPerson); // Update the data in the database
                view.showAlert("Person Updated", "Person information updated successfully.");
                personListView.refresh(); // Update the main list view
            }
            return selectedPerson;
        });

        dialog.showAndWait();
    }

    public void deletePerson(ListView<Person> personListView) {
        Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            view.showAlert("No Person Selected", "Please select a person to delete.");
            return;
        }

        // Eliminar la persona de la base de datos
        deleteData(selectedPerson.getId());

        // Eliminar la persona de la lista observable
        personList.remove(selectedPerson);
        existingIds.remove(selectedPerson.getId());

        view.showAlert("Person Deleted", "Selected person has been deleted.");
        personListView.setItems(personList); // Actualizar la lista observable
    }

    private void fetchCountries() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(COUNTRIES_API_URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            Type listType = new TypeToken<List<Country>>() {
            }.getType();
            List<Country> countries = new Gson().fromJson(responseBody, listType);

            ObservableList<String> countryNames = FXCollections.observableArrayList();
            for (Country country : countries) {
                countryNames.add(country.getName().getCommon());
            }

            countryNames.sort(Comparator.naturalOrder()); // Ordenar pa?ses alfab?ticamente
            this.countryComboBox.setItems(countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData(Person person) {
        String sql = "INSERT INTO personas (id, first_name, last_name, country) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = this.dbConnection.prepareStatement(sql)) {
            statement.setString(1, person.getId());
            statement.setString(2, person.getFirstName());
            statement.setString(3, person.getLastName());
            statement.setString(4, person.getCountry());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores al guardar datos
        }
    }

    private void updateData(Person person) {
        String sql = "UPDATE personas SET first_name = ?, last_name = ?, country = ? WHERE id = ?";

        try (PreparedStatement statement = dbConnection.prepareStatement(sql)) {
            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getCountry());
            statement.setString(4, person.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle data update errors
        }
    }

    private void deleteData(String id) {
        String sql = "DELETE FROM personas WHERE id = ?";

        try (PreparedStatement statement = dbConnection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores al eliminar datos
        }
    }

    private void loadDataFromDatabase() {
        personList.clear();
        String sql = "SELECT * FROM personas";

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String country = resultSet.getString("country");

                Person person = new Person(id, firstName, lastName, country);
                personList.add(person);
                existingIds.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores al cargar datos

        }
    }

    public ObservableList<Person> getPersonList() {
        return personList;
    }
}
