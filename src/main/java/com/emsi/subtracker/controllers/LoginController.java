package com.emsi.subtracker.controllers;

import com.emsi.subtracker.dao.UserDAO;
import com.emsi.subtracker.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init logic if needed
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        try {
            User user = userDAO.checkLogin(username, password);

            if (user != null) {
                // Success -> Go to Dashboard
                loadDashboard(user);
            } else {
                showError("Invalid Credentials!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Connection failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Enter username/password to register.");
            return;
        }

        try {
            boolean success = userDAO.register(username, password);
            if (success) {
                showAlert("Success", "Account created! You can now login.");
            } else {
                showError("Registration failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Registration Error", "Failed: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    private void loadDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();

            // Pass user to dashboard if needed
            DashboardController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subscription Tracker - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading dashboard.");
        }
    }
}
