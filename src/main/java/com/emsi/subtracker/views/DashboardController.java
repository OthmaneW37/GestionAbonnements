package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.models.User;
import com.emsi.subtracker.services.EmailService;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur de l'écran principal (Dashboard) - Version Cartes Visuelles.
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblTotalMensuel;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private ComboBox<String> sortFilter;

    private final SubscriptionService service = new SubscriptionService();
    private final EmailService emailService = EmailService.getInstance();
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private List<Abonnement> allSubscriptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("DashboardController initialization started...");

            // 1. Init Category Filter
            if (categoryFilter != null) {
                categoryFilter.getItems().addAll("Toutes", "Divertissement", "Travail", "Sport", "Musique", "Autre");
                categoryFilter.getSelectionModel().selectFirst();
                categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterSubscriptions());
            }

            // 2. Init Sort Filter
            if (sortFilter != null) {
                sortFilter.getItems().addAll("Par défaut", "Prix croissant", "Prix décroissant", "Date de début");
                sortFilter.getSelectionModel().selectFirst();
                sortFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterSubscriptions());
            }

            // 3. Init Search Listener
            if (searchField != null) {
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterSubscriptions();
                });
            } else {
                System.err.println("WARNING: searchField is null in DashboardController");
            }

            refreshDashboard();

            // ✅ INTÉGRATION EMAIL: Vérifier et envoyer les alertes de renouvellement J-3
            checkRenewalAlerts();

            System.out.println("DashboardController initialization completed.");
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR initializing DashboardController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateActiveWindows() {
        if (lblTotalMensuel != null && lblTotalMensuel.getScene() != null) {
            com.emsi.subtracker.utils.ThemeManager.applyTheme(lblTotalMensuel.getScene());
        }
    }

    private void refreshDashboard() {
        com.emsi.subtracker.models.User currentUser = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (currentUser != null && lblWelcome != null) {
            lblWelcome.setText("Bonjour, " + currentUser.getUsername());
        }

        // Fetch Data once
        this.allSubscriptions = service.getAll();

        // 1. Mise à jour du total
        double total = service.calculerTotalMensuel();

        // Currency Conversion
        String currency = com.emsi.subtracker.utils.UserSession.getInstance().getCurrency();
        double displayTotal = convertPrice(total, currency);
        String symbol = getCurrencySymbol(currency);

        lblTotalMensuel.setText(df.format(displayTotal) + " " + symbol);

        // 2. Refresh List
        filterSubscriptions();
    }

    private double convertPrice(double priceInDH, String targetCurrency) {
        switch (targetCurrency) {
            case "EUR":
                return priceInDH * 0.091; // Approx rate
            case "USD":
                return priceInDH * 0.099; // Approx rate
            default:
                return priceInDH;
        }
    }

    private String getCurrencySymbol(String currency) {
        switch (currency) {
            case "EUR":
                return "€";
            case "USD":
                return "$";
            default:
                return "DH";
        }
    }

    private void filterSubscriptions() {
        if (allSubscriptions == null)
            return;

        cardsContainer.getChildren().clear();

        String query = (searchField != null && searchField.getText() != null) ? searchField.getText().toLowerCase()
                : "";
        String category = (categoryFilter != null) ? categoryFilter.getValue() : "Toutes";
        String sortMode = (sortFilter != null) ? sortFilter.getValue() : "Par défaut";

        List<Abonnement> filtered = allSubscriptions.stream()
                .filter(sub -> {
                    // Filter by Search
                    boolean matchesSearch = query.isEmpty()
                            || (sub.getNom() != null && sub.getNom().toLowerCase().contains(query));

                    // Filter by Category
                    boolean matchesCategory = "Toutes".equals(category)
                            || (sub.getCategorie() != null && sub.getCategorie().equalsIgnoreCase(category));

                    return matchesSearch && matchesCategory;
                })
                .collect(java.util.stream.Collectors.toList());

        // Sort
        if ("Prix croissant".equals(sortMode)) {
            filtered.sort(java.util.Comparator.comparingDouble(Abonnement::getPrix));
        } else if ("Prix décroissant".equals(sortMode)) {
            filtered.sort(java.util.Comparator.comparingDouble(Abonnement::getPrix).reversed());
        } else if ("Date de début".equals(sortMode)) {
            filtered.sort(java.util.Comparator.comparing(Abonnement::getDateDebut));
        }

        // Display
        for (Abonnement sub : filtered) {
            VBox card = createSubscriptionCard(sub);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createSubscriptionCard(Abonnement sub) {
        VBox card = new VBox(10);
        card.getStyleClass().add("sub-card");

        // Determine Brand Color
        String name = sub.getNom() != null ? sub.getNom().toLowerCase() : "";
        if (name.contains("netflix")) {
            card.getStyleClass().add("brand-netflix");
        } else if (name.contains("spotify")) {
            card.getStyleClass().add("brand-spotify");
        } else if (name.contains("amazon") || name.contains("prime")) {
            card.getStyleClass().add("brand-amazon");
        } else if (name.contains("apple") || name.contains("icloud")) {
            card.getStyleClass().add("brand-apple");
        } else if (name.contains("adobe") || name.contains("creative")) {
            card.getStyleClass().add("brand-adobe");
        } else if (name.contains("basic") || name.contains("fit")) {
            card.getStyleClass().add("brand-basicfit");
        } else {
            card.getStyleClass().add("brand-default");
        }

        // Header: Title + Menu Button
        javafx.scene.layout.HBox header = new javafx.scene.layout.HBox();
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label lblName = new Label(sub.getNom());
        lblName.getStyleClass().add("sub-card-title");
        lblName.setWrapText(true);
        // Ensure title doesn't push menu out if too long
        lblName.setMaxWidth(160);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Three dots menu button (SVG)
        Button btnMenu = new Button();
        btnMenu.getStyleClass().add("action-icon-button");

        SVGPath icon = new SVGPath();
        // Material Design Vertical Dots
        icon.setContent(
                "M12 8c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm0 2c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm0 6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z");

        // Initial class
        icon.getStyleClass().add("icon-placeholder"); // Start with muted/placeholder color
        icon.setScaleX(0.8);
        icon.setScaleY(0.8);

        btnMenu.setGraphic(icon);

        // --- CONTEXT MENU ---
        ContextMenu contextMenu = new ContextMenu();

        // Edit Icon
        SVGPath editIconDisplay = new SVGPath();
        editIconDisplay.setContent(
                "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z");
        editIconDisplay.getStyleClass().add("icon-primary"); // Dynamic color
        editIconDisplay.setScaleX(0.7);
        editIconDisplay.setScaleY(0.7);

        MenuItem editItem = new MenuItem("Modifier");
        editItem.setGraphic(editIconDisplay);
        editItem.getStyleClass().add("menu-item-edit");
        editItem.setOnAction(e -> editSubscription(sub));

        // Delete Icon - Keep Red
        SVGPath deleteIconDisplay = new SVGPath();
        deleteIconDisplay.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        deleteIconDisplay.setFill(Color.web("#ff6666")); // Danger is always red
        deleteIconDisplay.setScaleX(0.7);
        deleteIconDisplay.setScaleY(0.7);

        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setGraphic(deleteIconDisplay);
        deleteItem.getStyleClass().add("menu-item-delete");
        deleteItem.setOnAction(e -> deleteSubscription(sub));

        contextMenu.getItems().addAll(editItem, deleteItem);

        // Handle click to show menu
        btnMenu.setOnMouseClicked(event -> {
            contextMenu.show(btnMenu, javafx.geometry.Side.BOTTOM, 0, 0);
            event.consume();
        });

        // Hover effect for icon color
        btnMenu.hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (icon.getStyleClass().contains("icon-placeholder"))
                    icon.getStyleClass().remove("icon-placeholder");
                if (!icon.getStyleClass().contains("icon-primary"))
                    icon.getStyleClass().add("icon-primary");
            } else {
                if (icon.getStyleClass().contains("icon-primary"))
                    icon.getStyleClass().remove("icon-primary");
                if (!icon.getStyleClass().contains("icon-placeholder"))
                    icon.getStyleClass().add("icon-placeholder");
            }
        });

        header.getChildren().addAll(lblName, spacer, btnMenu);

        // Price
        String currency = com.emsi.subtracker.utils.UserSession.getInstance().getCurrency();
        double displayPrice = convertPrice(sub.getPrix(), currency);
        String symbol = getCurrencySymbol(currency);

        Label lblPrice = new Label(df.format(displayPrice) + " " + symbol);
        lblPrice.getStyleClass().add("sub-card-price");

        // Footer Info (Category & Date)
        Label lblDate = new Label(
                "Echéance: " + (sub.getDateDebut() != null ? sub.getDateDebut().plusMonths(1).format(dtf) : "N/A"));
        lblDate.getStyleClass().add("sub-card-date");

        Label lblFreq = new Label(sub.getFrequence());
        lblFreq.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");

        // Add all to card
        card.getChildren().addAll(header, lblPrice, lblDate, lblFreq);

        // Interaction (Click placeholder)
        card.setOnMouseClicked(e -> {
            // Only trigger if not clicking the menu
            if (e.getTarget() != btnMenu && e.getTarget() != icon) {
                System.out.println("Selected: " + sub.getNom());
            }
        });

        return card;
    }

    private void editSubscription(Abonnement sub) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/add_subscription.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("/styles_v2.css").toExternalForm());
            com.emsi.subtracker.utils.ThemeManager.applyTheme(scene);

            AddSubscriptionController controller = fxmlLoader.getController();
            controller.setAbonnement(sub);

            Stage stage = new Stage();
            stage.setTitle("Modifier l'abonnement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            refreshDashboard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSubscription(Abonnement sub) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer " + sub.getNom() + " ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet abonnement ?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            service.remove(sub.getId());
            refreshDashboard();
        }
    }

    @FXML
    protected void onBtnAjouterClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/add_subscription.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Apply CSS to Modal
            scene.getStylesheets().add(getClass().getResource("/styles_v2.css").toExternalForm());
            com.emsi.subtracker.utils.ThemeManager.applyTheme(scene);

            Stage stage = new Stage();
            stage.setTitle("Nouvel Abonnement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            // Refresh après fermeture
            refreshDashboard();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onProfileClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/user_profile.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("/styles_v2.css").toExternalForm());
            com.emsi.subtracker.utils.ThemeManager.applyTheme(scene);

            Stage stage = new Stage();
            stage.setTitle("Mon Profil");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSettingsClick() {
        try {
            Stage currentStage = (Stage) lblTotalMensuel.getScene().getWindow();
            SceneManager.switchScene(currentStage, "settings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToAnalytics() {
        try {
            Stage currentStage = (Stage) lblTotalMensuel.getScene().getWindow();
            SceneManager.switchScene(currentStage, "analytics.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Vérifie les abonnements et envoie des alertes pour ceux qui arrivent à
     * échéance dans 3 jours.
     */
    private void checkRenewalAlerts() {
        User currentUser = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (currentUser != null && allSubscriptions != null) {
            emailService.checkAndSendAlerts(currentUser, allSubscriptions);
            System.out.println("🔔 Vérification des alertes de renouvellement effectuée.");
        }
    }
}
