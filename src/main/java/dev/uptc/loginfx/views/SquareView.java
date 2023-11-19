package dev.uptc.loginfx.views;

import com.fasterxml.jackson.databind.JsonNode;
import dev.uptc.loginfx.presenter.SquarePresenter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.scene.control.Alert.AlertType;

public class SquareView extends Application {
    TextField inputField;
    Label resultLabel;
    Button calculateButton;
    private ObservableList<String> resultsData;

    @Override
    public void start(Stage primaryStage) {
        SquarePresenter presenter;
        ListView<String> resultList;
        Button addToResultButton;
        Image bannerImage = new Image(System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "main" + File.separator + "resources" +
                File.separator + "dev" + File.separator + "uptc" +
                File.separator + "loginfx" + File.separator + "banner.png");
        ImageView bannerImageView = new ImageView(bannerImage);
        bannerImageView.setFitWidth(300);
        bannerImageView.setFitHeight(150);

        Label titleLabel = new Label("Calculadora de Cuadrados");
        titleLabel.getStyleClass().add("title-label");

        inputField = new TextField();
        resultLabel = new Label();
        calculateButton = new Button("Calcular");
        addToResultButton = new Button("Agregar a la Lista");
        Button removeFromResultButton = new Button("Eliminar de la Lista");
        resultList = new ListView<>();
        resultsData = FXCollections.observableArrayList();
        resultList.getStyleClass().add("result-list-item");

        presenter = new SquarePresenter(this);

        calculateButton.setOnAction(event -> {
            presenter.calculateSquare();
            addToResultButton.setDisable(false);
        });

        addToResultButton.setOnAction(event -> {
            String result = resultLabel.getText();
            if (isResultValid(result)) {
                resultsData.add(result);
                saveResultsToFileTxt();
                saveResultsToFileJson();
            } else {
                showSelectionWarning();
            }
        });

        removeFromResultButton.setOnAction(event -> {
            String selectedItem = resultList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                resultsData.remove(selectedItem);
                saveResultsToFileTxt();
                saveResultsToFileJson();
            }
        });

        resultList.setItems(resultsData);
        loadResultsFromFileTxt();
        loadResultsFromFileJson();

        VBox root = new VBox(10);
        root.getStyleClass().add("main-container");
        root.getChildren().addAll(bannerImageView, titleLabel, inputField, calculateButton, resultLabel, addToResultButton, removeFromResultButton, resultList);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(new File(System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "main" + File.separator + "resources" +
                File.separator + "dev" + File.separator + "uptc" +
                File.separator + "loginfx" + File.separator + "squareStyles.css").toURI().toString());
        primaryStage.setTitle("Calculadora de Cuadrados");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private boolean isResultValid(String result) {
        return !result.contains("Ingresa un número valido antes de calcular el cuadrado.");
    }

    private void showSelectionWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText("No se puede agregar un mensaje de error a la lista.");
        alert.showAndWait();
    }

    public double getInputNumber() {
        try {
            return Double.parseDouble(inputField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setResult(double result) {
        double inputNumber = getInputNumber();
        if (inputNumber != 0) {
            resultLabel.setText("El cuadrado de " + inputNumber + " es " + result);
        } else {
            resultLabel.setText("Ingresa un número valido antes de calcular el cuadrado.");
        }
    }

    private void saveResultsToFileTxt() {
        try {
            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + File.separator + "src" +
                    File.separator + "main" + File.separator + "resources" +
                    File.separator + "dev" + File.separator + "uptc" +
                    File.separator + "loginfx" + File.separator + "squarecalculator" + File.separator + "results.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String result : resultsData) {
                bufferedWriter.write(result);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadResultsFromFileTxt() {
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + "src" +
                    File.separator + "main" + File.separator + "resources" +
                    File.separator + "dev" + File.separator + "uptc" +
                    File.separator + "loginfx" + File.separator + "squarecalculator" + File.separator + "results.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultsData.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveResultsToFileJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode resultsNode = rootNode.putArray("results");
            for (String result : resultsData) {
                resultsNode.add(result);
            }
            objectMapper.writeValue(new File(System.getProperty("user.dir") + File.separator + "src" +
                    File.separator + "main" + File.separator + "resources" +
                    File.separator + "dev" + File.separator + "uptc" +
                    File.separator + "loginfx" + File.separator + "squarecalculator" + File.separator + "results.json"), rootNode);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadResultsFromFileJson() {
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + "src" +
                    File.separator + "main" + File.separator + "resources" +
                    File.separator + "dev" + File.separator + "uptc" +
                    File.separator + "loginfx" + File.separator + "squarecalculator" + File.separator + "results.json");
            if (!file.exists()) {
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.readValue(file, ObjectNode.class);
            ArrayNode resultsNode = (ArrayNode) rootNode.get("results");
            if (resultsNode != null) {
                resultsData.clear();
                for (JsonNode resultNode : resultsNode) {
                    resultsData.add(resultNode.asText());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
