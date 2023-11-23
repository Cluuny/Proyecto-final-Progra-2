package dev.uptc.loginfx.presenter;

import dev.uptc.loginfx.model.User;
import dev.uptc.loginfx.views.LoggedWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class LoggedPresenter extends Application {

    private final User user;

    public LoggedPresenter(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage stage) throws Exception {
        new LoggedWindow(this.user).start(new Stage());
    }
}
