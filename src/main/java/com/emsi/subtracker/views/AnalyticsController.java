package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Contrôleur de l'écran Analytics.
 */
public class AnalyticsController implements Initializable {

    @FXML
    private PieChart pieChartCategories;

    @FXML
    private BarChart<String, Number> barChartCosts;

    @FXML
    private javafx.scene.control.TextField searchField;

    private final SubscriptionService service = new SubscriptionService();
    private List<Abonnement> allSubscriptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allSubscriptions = service.getAll();

        loadPieChartData(allSubscriptions);
        loadBarChartData(allSubscriptions);

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                filterCharts(newVal);
            });
        }

        // Apply Theme
        javafx.application.Platform.runLater(() -> {
            if (pieChartCategories.getScene() != null) {
                com.emsi.subtracker.utils.ThemeManager.applyTheme(pieChartCategories.getScene());
            }
        });
    }

    private void filterCharts(String query) {
        if (allSubscriptions == null)
            return;

        String lowerQuery = (query != null) ? query.toLowerCase() : "";
        List<Abonnement> filtered = allSubscriptions.stream()
                .filter(sub -> sub.getNom().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());

        loadPieChartData(filtered);
        loadBarChartData(filtered);
    }

    private void loadPieChartData() {
        loadPieChartData(this.allSubscriptions);
    } // overload for refresh

    private void loadBarChartData() {
        loadBarChartData(this.allSubscriptions);
    } // overload for refresh

    private void loadPieChartData(List<Abonnement> list) {
        // Group by category and sum counts (or costs?)
        // Let's visualize Cost Distribution by Category
        Map<String, Double> costByCategory = list.stream()
                .collect(Collectors.groupingBy(
                        Abonnement::getCategorie,
                        Collectors.summingDouble(a -> {
                            // Normalize to monthly cost
                            return "Annuel".equalsIgnoreCase(a.getFrequence()) ? a.getPrix() / 12.0 : a.getPrix();
                        })));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        costByCategory.forEach((cat, cost) -> {
            pieData.add(new PieChart.Data(cat, cost));
        });

        pieChartCategories.setData(pieData);
    }

    private void loadBarChartData(List<Abonnement> list) {
        barChartCosts.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Coût Mensuel");

        for (Abonnement sub : list) {
            double monthlyCost = "Annuel".equalsIgnoreCase(sub.getFrequence()) ? sub.getPrix() / 12.0 : sub.getPrix();
            series.getData().add(new XYChart.Data<>(sub.getNom(), monthlyCost));
        }

        barChartCosts.getData().add(series);
    }

    @FXML
    protected void goToDashboard() {
        try {
            Stage currentStage = (Stage) pieChartCategories.getScene().getWindow();
            SceneManager.switchScene(currentStage, "dashboard.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSettingsClick() {
        try {
            Stage currentStage = (Stage) pieChartCategories.getScene().getWindow();
            SceneManager.switchScene(currentStage, "settings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
