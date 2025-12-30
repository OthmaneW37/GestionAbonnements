package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.models.User;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.UserSession;
import com.emsi.subtracker.utils.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblTotalSubscriptions;
    @FXML
    private Label lblTotalCost;
    @FXML
    private javafx.scene.control.ToggleButton themeToggle;

    private final SubscriptionService service = new SubscriptionService();
    private final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
        setupThemeToggle();
    }

    private void setupThemeToggle() {
        if (themeToggle != null) {
            boolean isDark = ThemeManager.isDarkTheme();
            themeToggle.setSelected(isDark);
            themeToggle.setText(isDark ? "ON" : "OFF");
            themeToggle.setStyle(isDark
                    ? "-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-background-radius: 20;"
                    : "-fx-background-color: #ccc; -fx-text-fill: black; -fx-background-radius: 20;");

            themeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                ThemeManager.setDarkTheme(newVal);
                themeToggle.setText(newVal ? "ON" : "OFF");
                themeToggle.setStyle(newVal
                        ? "-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-background-radius: 20;"
                        : "-fx-background-color: #ccc; -fx-text-fill: black; -fx-background-radius: 20;");
            });
        }
    }

    private void loadUserData() {
        User user = UserSession.getInstance().getUser();
        if (user != null) {
            lblUsername.setText(user.getUsername());
            lblEmail.setText(user.getEmail());

            // Fetch stats
            List<Abonnement> subs = service.getAll();
            lblTotalSubscriptions.setText(String.valueOf(subs.size()));

            double total = service.calculerTotalMensuel();
            lblTotalCost.setText(df.format(total) + " DH");
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) lblUsername.getScene().getWindow();
        stage.close();
    }
}
