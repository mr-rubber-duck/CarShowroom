package org.example.carshowroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.carshowroom.DAO.UserDAO;

import java.io.IOException;

public class LoginController {


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckbox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink registerLink;


    @FXML
    public void initialize() {
        // Add enter key listener to password field
        passwordField.setOnAction(event -> handleLogin(event));

        // Add enter key listener to username field
        usernameField.setOnAction(event -> passwordField.requestFocus());
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Hide error label initially
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        // Authentication logic (replace with actual database check)
        if (authenticateUser(username, password)) {
            // Save remember me preference if needed
            if (rememberMeCheckbox.isSelected()) {
                // Save username to preferences/file
                System.out.println("Remember me selected");
            }

            // Navigate to main application window
            navigateToMainWindow();
        } else {
            showError("Invalid username or password");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    private boolean authenticateUser(String username, String password) {
        UserDAO userDAO=new UserDAO();
        return userDAO.checkLogin(username, password);

    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        // TODO: Implement forgot password functionality
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Please contact the system administrator to reset your password.");
        alert.showAndWait();
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        // TODO: Navigate to registration page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerLink.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to load registration page");
        }
    }

    private void navigateToMainWindow() {
        try {
            // TODO: Update with your actual main window FXML path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Car Showroom - Dashboard");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to load main window");
        }
    }
}