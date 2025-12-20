package com.emsi.subtracker.controllers;

import com.emsi.subtracker.models.Subscription;
import com.emsi.subtracker.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML
    private Label totalLabel;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barChart;

    private User currentUser;
    private List<Subscription> subscriptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init
    }

    public void initData(ObservableList<Subscription> subs, User user) {
        this.subscriptions = subs;
        this.currentUser = user;
        calculateStats();
    }

    private void calculateStats() {
        if (subscriptions == null)
            return;

        double totalMonthlyDetails = 0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Subscription sub : subscriptions) {
            double monthlyPrice = sub.getPrice();
            if ("Yearly".equalsIgnoreCase(sub.getFrequency())) {
                monthlyPrice = sub.getPrice() / 12.0;
            }

            totalMonthlyDetails += monthlyPrice;
            categoryTotals.put(sub.getCategory(), categoryTotals.getOrDefault(sub.getCategory(), 0.0) + monthlyPrice);
        }

        // Update Total Label
        totalLabel.setText(String.format("%.2f MAD", totalMonthlyDetails));

        // Update PieChart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        pieChart.setData(pieData);

        // Update BarChart (Mocking yearly forecast based on monthly avg)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Predicted Expenses");

        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        for (String m : months) {
            series.getData().add(new XYChart.Data<>(m, totalMonthlyDetails));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    @FXML
    private void handleGoToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
