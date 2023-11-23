package dev.uptc.loginfx.presenter;

import dev.uptc.loginfx.exceptions.BadCredentialsException;
import dev.uptc.loginfx.model.LoginModel;
import dev.uptc.loginfx.model.User;
import dev.uptc.loginfx.views.LoggedWindow;
import dev.uptc.loginfx.views.LoginWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;

public class LoginPresenter extends Application {
    private final LoginWindow loginWindow;
    private final LoginModel loginModel;
    private Stage stage;

    public LoginPresenter() {
        this.loginWindow = new LoginWindow();
        this.loginModel = new LoginModel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Platform.runLater(() ->
                loginWindow.start(new Stage()));
    }

    public String loadBanner() {
        return loginModel.getBanner();
    }

    public File loadCssPath() {
        return loginModel.getStylePath();
    }

    public void handleLogin(String userName, String password) {
        try {
            User user = loginModel.handleLogin(userName, password);
            if (user != null) {
                this.stage.close();
                try {
                    this.openMenu(user);
                }catch (Exception e){
                    loginWindow.showAlert(e.getMessage());
                }
            } else {
                throw new BadCredentialsException();
            }
        } catch (BadCredentialsException e) {
            loginWindow.showAlert(e.getMessage());
        }

    }

    private void openMenu(User user) throws Exception{
        if (this.loginModel.getSupportedRoles().contains(user.getRole())) {
            LoggedPresenter loggedPresenter = new LoggedPresenter(user);
            loggedPresenter.start(this.stage);
        }
    }
}
