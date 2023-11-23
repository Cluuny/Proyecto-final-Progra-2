package dev.uptc.loginfx.views;

import dev.uptc.loginfx.model.User;
import dev.uptc.loginfx.presenter.PersonRegistrationPresenter;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class LoggedWindow extends Application {
    private final User user;
    private final Map<String, Application> toolList;

    public LoggedWindow(User user) {
        this.user = user;
        this.toolList = new HashMap<>();
        this.toolList.put("Calculadora de cuadrados", new SquareView());
        this.toolList.put("Calculadora de Fechas Futuras", new DateCalculatorView());
        this.toolList.put("Notepad", new NotepadView());
        this.toolList.put("CRUD Holding", new CrudHoldingApp());
        this.toolList.put("Registro de Personas", new PersonRegistrationPresenter());
    }

    @Override
    public void start(Stage primaryStage) {
        // Crear un texto para el título
        Label titleText = new Label("Bienvenido " + this.user.getRole());
        titleText.setFont(new Font(20));

        // Crear una imagen con tamaño reducido
        Image image = new Image(String.valueOf(new File(System.getProperty("user.dir") + File.separator + "\\src\\main\\resources\\dev\\uptc\\loginfx\\banner.png"))); // Reemplaza con la ruta correcta de tu imagen
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(100);

        List<String> keySet = new ArrayList<>(this.toolList.keySet());
        Button button1 = new Button(keySet.get(0));
        Button button2 = new Button(keySet.get(1));
        Button button3 = new Button(keySet.get(2));
        Button button4 = new Button(keySet.get(3));
        Button button5 = new Button(keySet.get(4));

        button1.setOnAction(event -> {
            try {
                Application application = this.toolList.get(keySet.get(0));
                application.start(new Stage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        button2.setOnAction(event -> {
            try {
                Application application = this.toolList.get(keySet.get(1));
                application.start(new Stage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        button3.setOnAction(event -> {
            try {
                Application application = this.toolList.get(keySet.get(2));
                application.start(new Stage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        button4.setOnAction(event -> {
            try {
                Application application = this.toolList.get(keySet.get(3));
                application.start(new Stage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        button5.setOnAction(event -> {
            try {
                Application application = this.toolList.get(keySet.get(4));
                application.start(new Stage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        // Crear una lista vertical de botones
        VBox buttonList = new VBox(10); // Espacio de 10 píxeles entre botones
        buttonList.getChildren().addAll(button1, button2, button3, button4, button5);
        List<Node> buttons = new ArrayList<>(buttonList.getChildren());
        Collections.shuffle(buttons);
        buttonList.getChildren().clear();
        buttonList.getChildren().addAll(buttons);
        buttonList.setAlignment(Pos.CENTER);

        // Crear un contenedor HBox para organizar la imagen y la lista horizontalmente
        HBox imageAndButtonBox = new HBox(20); // Espacio de 20 píxeles entre la imagen y la lista
        imageAndButtonBox.getChildren().addAll(buttonList);
        imageAndButtonBox.setAlignment(Pos.CENTER);

        // Crear un contenedor VBox para organizar el título y la combinación de imagen y botones verticalmente
        VBox root = new VBox(20); // Espacio de 20 píxeles entre el título y la combinación de imagen y botones
        root.getChildren().addAll(titleText, imageView, buttonList);
        root.setAlignment(Pos.CENTER);

        // Crear la escena y establecerla en la ventana principal
        Scene scene = new Scene(root, 500, 450);
        // Cargar la hoja de estilo CSS
        String cssPath = new File(System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "main" + File.separator + "resources" +
                File.separator + "dev" + File.separator + "uptc" +
                File.separator + "loginfx" + File.separator + "logged.css").toURI().toString();

        scene.getStylesheets().add(cssPath);
        primaryStage.setScene(scene);

        // Establecer el título de la ventana principal
        primaryStage.setTitle("Panel de " + this.user.getRole());

        // Mostrar la ventana principal
        primaryStage.show();
    }
}
