package dev.uptc.loginfx.views;

import dev.uptc.loginfx.model.NotepadModel;
import dev.uptc.loginfx.presenter.NotepadPresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NotepadView extends Application {

    private TextArea textArea;
    private NotepadPresenter presenter;

    @Override
    public void start(Stage primaryStage) {
        textArea = new TextArea();
        MenuBar menuBar = createMenuBar();
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textArea);

        presenter = new NotepadPresenter(new NotepadModel(), this);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("Bloc de Notas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Archivo");
        MenuItem openMenuItem = new MenuItem("Abrir");
        MenuItem saveMenuItem = new MenuItem("Guardar");
        MenuItem exitMenuItem = new MenuItem("Salir");
        openMenuItem.setOnAction(event -> presenter.openFile());
        saveMenuItem.setOnAction(event -> presenter.saveFile());
        exitMenuItem.setOnAction(event -> System.exit(0));
        fileMenu.getItems().addAll(openMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);

        Menu editMenu = new Menu("Editar");
        MenuItem undoMenuItem = new MenuItem("Deshacer");
        undoMenuItem.setOnAction(event -> textArea.undo());
        editMenu.getItems().add(undoMenuItem);

        Menu helpMenu = new Menu("Ayuda");
        MenuItem aboutMenuItem = new MenuItem("Acerca de");
        aboutMenuItem.setOnAction(event -> showAboutDialog());
        helpMenu.getItems().add(aboutMenuItem);

        Menu optionsMenu = new Menu("Opciones");
        CheckMenuItem uppercaseMenuItem = new CheckMenuItem("Mayúsculas");
        uppercaseMenuItem.setOnAction(event -> toggleUppercase());
        optionsMenu.getItems().add(uppercaseMenuItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu, helpMenu);

        return menuBar;
    }

    private void toggleUppercase() {
        if (textArea.getSelectedText().isEmpty()) {
            String text = textArea.getText();
            textArea.setText(text.toUpperCase());
        } else {
            String selectedText = textArea.getSelectedText();
            textArea.replaceSelection(selectedText.toUpperCase());
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Bloc de Notas UPTC");
        alert.setContentText("Versión 1.0\nUniversidad Pedagógica y Tecnológica de Colombia\nDesarrollado por:\n - Vicente Matallana\n - Gabriel Cely\n - William González");
        alert.showAndWait();
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}
