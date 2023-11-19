package dev.uptc.loginfx.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.uptc.loginfx.exceptions.BadCredentialsException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginModel {
    private final List<String> supportedRoles = new ArrayList<>();
    private final String banner;
    private final File stylePath;
    private final File usersBD;

    public LoginModel() {
        this.supportedRoles.add("Administrador");
        this.supportedRoles.add("Docente");
        this.supportedRoles.add("Estudiante");
        this.supportedRoles.add("Invitado");
        this.banner = System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "main" + File.separator + "resources" +
                File.separator + "dev" + File.separator + "uptc" +
                File.separator + "loginfx" + File.separator + "banner.png";
        this.stylePath = new File(System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "main" + File.separator + "resources" +
                File.separator + "dev" + File.separator + "uptc" +
                File.separator + "loginfx" + File.separator + "styles.css");
        this.usersBD = new File(System.getProperty("user.dir") + File.separator + "\\src\\main\\resources\\dev\\uptc\\loginfx\\users.json");
    }

    public User handleLogin(String userName, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(this.usersBD);
            for (JsonNode userNode : rootNode.get("users")) {
                String storedUsername = userNode.get("username").asText();
                String storedPassword = userNode.get("password").asText();
                String storedRoll = userNode.get("roll").asText();

                if (storedUsername.equals(userName) && storedPassword.equals(password)) {
                    return new User(userName, storedRoll);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<String> getSupportedRoles() {
        return supportedRoles;
    }

    public String getBanner() {
        return banner;
    }

    public File getStylePath() {
        return stylePath;
    }
}
