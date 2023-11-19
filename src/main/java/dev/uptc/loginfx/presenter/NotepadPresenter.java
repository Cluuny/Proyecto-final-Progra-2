package dev.uptc.loginfx.presenter;

import dev.uptc.loginfx.model.NotepadModel;
import dev.uptc.loginfx.views.NotepadView;
import javafx.stage.FileChooser;  // Importaci�n de la clase FileChooser de JavaFX para elegir archivos
import javafx.stage.Stage;  // Importaci�n de la clase Stage de JavaFX para manejar ventanas
import java.io.File;  // Importaci�n de la clase File para representar archivos
import java.io.IOException;  // Importaci�n de la clase IOException para el manejo de excepciones de E/S

public class NotepadPresenter {
    private final NotepadModel model;
    private final NotepadView view;
    private final Stage stage;

    public NotepadPresenter(NotepadModel model, NotepadView view) {
        this.model = model;
        this.view = view;
        this.stage = new Stage();
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                model.loadTextFromFile(file);
                view.setText(model.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                model.setText(view.getText());
                model.saveTextToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
