package com.emsi.subtracker.views;

import com.emsi.subtracker.models.User;
import com.emsi.subtracker.services.UserService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    private final UserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    @FXML
    private void handleLogin() {
        System.out.println("Login button clicked.");
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        User user = userService.authenticate(username, password);
        if (user != null) {
            System.out.println("Authentication successful for user: " + user.getUsername());
            // Login successful
            try {
                // Pass user to dashboard if needed, for now just navigate
                com.emsi.subtracker.utils.UserSession.getInstance().setUser(user);

                System.out.println("Switching to dashboard.fxml...");
                SceneManager.switchScene((Stage) loginButton.getScene().getWindow(), "dashboard.fxml");
            } catch (IOException e) {
                System.err.println("FAILED to load dashboard: " + e.getMessage());
                e.printStackTrace();
                errorLabel.setText("Erreur lors du chargement du dashboard.");
            }
        } else {
            System.out.println("Authentication failed for: " + username);
            errorLabel.setText("Identifiants incorrects.");
        }
    }

    @FXML
    private TextField passwordTextField;
    @FXML
    private javafx.scene.control.CheckBox showPasswordCheckBox;

    @FXML
    public void initialize() {
        // Sync text fields
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    private void onShowPassword() {
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
        }
    }

    @FXML
    private void goToRegister() {
        try {
            SceneManager.switchScene((Stage) loginButton.getScene().getWindow(), "register.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur de navigation.");
        }
    }
}
