package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.datastorage.UserDao;
import de.hitec.nhplus.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

/**
 * The <code>LoginController</code> is responsible for handling the logic of the login view.
 * It authenticates users using the credentials entered and loads the main application window upon successful login.
 */
public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private Connection connection;
    private UserDao dao;

    /**
     * When <code>initialize()</code> gets called, the database connection is established and the {@link UserDao}
     * is initialized. This method is automatically invoked after the FXML file has been loaded and the FXML elements
     * have been injected.
     */
    @FXML
    public void initialize() {
        try {
            connection = ConnectionBuilder.getConnection();
            dao = new UserDao(connection);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method handles the login button event. It fetches the username and password from the corresponding
     * <code>TextField</code>s, validates the credentials using the {@link UserDao}, and loads the main scene
     * if authentication succeeds. If authentication fails, an error dialog is shown.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = dao.getUserByCredentials(username, password);
        if (user != null) {
            loadMainScene();
        } else {
            showError("Login fehlgeschlagen", "Benutzername oder Passwort ist falsch.");
        }
    }

    /**
     * Loads the main application scene by replacing the current stage's scene with the content defined in
     * <code>MainWindowView.fxml</code>.
     */
    private void loadMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/hitec/nhplus/MainWindowView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an error dialog with the given header and content messages.
     *
     * @param header  The header text of the error dialog.
     * @param content The content text of the error dialog.
     */
    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
