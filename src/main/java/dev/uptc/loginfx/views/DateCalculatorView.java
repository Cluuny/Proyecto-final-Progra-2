package dev.uptc.loginfx.views;  // Declaraci�n del paquete en el que se encuentra esta clase

import dev.uptc.loginfx.presenter.DateCalculatorPresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

public class DateCalculatorView extends Application {
    private DatePicker currentDatePicker;
    private TextField daysField;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        DateCalculatorPresenter presenter;
        ListView<String> resultList;
        Button removeFromResultButton;
        Button addToResultButton;
        Button calculateButton;  // M�todo start que configura la interfaz de usuario
        currentDatePicker = new DatePicker();  // Creaci�n de un DatePicker para seleccionar la fecha actual
        daysField = new TextField();  // Creaci�n de un TextField para ingresar la cantidad de d�as a agregar
        resultLabel = new Label();  // Creaci�n de una Label para mostrar el resultado
        calculateButton = new Button("Calcular Fecha Futura");  // Creaci�n de un bot�n para calcular la fecha futura
        addToResultButton = new Button("Agregar a la Lista");  // Creaci�n de un bot�n para agregar resultados a la lista
        removeFromResultButton = new Button("Eliminar de la Lista");  // Creaci�n de un bot�n para eliminar resultados de la lista
        resultList = new ListView<>();  // Creaci�n de una ListView para mostrar resultados

        presenter = new DateCalculatorPresenter(this);  // Creaci�n de una instancia de DateCalculatorPresenter y asociaci�n con la vista

        calculateButton.setOnAction(event -> presenter.calculateFutureDate());  // Configuraci�n de un controlador de eventos para el bot�n de c�lculo

        addToResultButton.setOnAction(event -> {  // Configuraci�n de un controlador de eventos para el bot�n de agregar a la lista
            String result = resultLabel.getText();  // Obtenci�n del resultado actual desde la etiqueta
            if (!result.isEmpty()) {  // Comprobaci�n de que el resultado no est� vac�o
                resultList.getItems().add(result);  // Agregar el resultado a la lista
            }
        });

        removeFromResultButton.setOnAction(event -> {  // Configuraci�n de un controlador de eventos para el bot�n de eliminar de la lista
            String selectedItem = resultList.getSelectionModel().getSelectedItem();  // Obtenci�n del elemento seleccionado en la lista
            if (selectedItem != null) {  // Comprobaci�n de que se haya seleccionado un elemento
                resultList.getItems().remove(selectedItem);  // Eliminar el elemento de la lista
            }
        });

        VBox root = new VBox(10);  // Creaci�n de un contenedor vertical
        root.getStyleClass().add("main-container");  // Aplicaci�n de una clase de estilo al contenedor
        root.getChildren().addAll(currentDatePicker, daysField, calculateButton, addToResultButton, removeFromResultButton, resultLabel, resultList);  // Agregar elementos al contenedor

        Scene scene = new Scene(root, 400, 300);  // Creaci�n de una escena con el contenedor
        scene.getStylesheets().add(new File(System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "main" + File.separator + "resources" +
                File.separator + "dev" + File.separator + "uptc" +
                File.separator + "loginfx" + File.separator + "datecalculator" + File.separator + "styles.css").toURI().toString());  // Aplicaci�n de un archivo de estilo externo
        primaryStage.setTitle("Calculadora de Fechas Futuras");  // Establecer el t�tulo de la ventana
        primaryStage.setScene(scene);  // Establecer la escena en la ventana
        primaryStage.show();  // Mostrar la ventana
    }

    public LocalDate getCurrentDate() {  // M�todo para obtener la fecha actual seleccionada
        return currentDatePicker.getValue();
    }

    public int getDaysToAdd() {
        try {
            return Integer.parseInt(daysField.getText());
        } catch (NumberFormatException e) {
            return 0; // Valor predeterminado en caso de error de conversi�n
        }
    }

    public void setResult(LocalDate result) {  // M�todo para establecer el resultado en la etiqueta
        resultLabel.setText("La fecha en " + getDaysToAdd() + " d�as ser�: " + result);
    }
}
