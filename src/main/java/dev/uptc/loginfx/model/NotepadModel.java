package dev.uptc.loginfx.model;

import java.io.*;

public class NotepadModel {
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void loadTextFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            text = builder.toString();
        }
    }

    public void saveTextToFile(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(text);
        }
    }
}
