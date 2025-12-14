package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.models.SubscriptionTemplate;
import com.emsi.subtracker.services.SubscriptionService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur du formulaire d'ajout d'abonnement (Wizard 3 étapes).
 */
public class AddSubscriptionController implements Initializable {

    // --- Containers des étapes ---
    @FXML
    private VBox stepContainerCategories;
    @FXML
    private VBox stepContainerServices;
    @FXML
    private VBox stepContainerDetails;

    // --- Contenu dynamique ---
    @FXML
    private FlowPane flowCategories;
    @FXML
    private FlowPane flowServices;

    // --- Formulaire Final ---
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrix;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private ComboBox<String> cmbFrequence;
    @FXML
    private ComboBox<String> cmbCategorie;

    private final SubscriptionService service = new SubscriptionService();
    private List<SubscriptionTemplate> allTemplates = new ArrayList<>();
    private String currentSelectedCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init formulaire de base
        cmbFrequence.getItems().addAll("Mensuel", "Annuel");
        cmbFrequence.getSelectionModel().selectFirst();
        cmbCategorie.getItems().addAll("Divertissement", "Travail", "Sport", "Musique", "Transport", "Utilitaires",
                "Alimentation", "Autre");
        dateDebut.setValue(LocalDate.now());

        // Charger les données CSV
        loadSubscriptionData();

        // Afficher l'étape 1
        showCategories();
    }

    private void loadSubscriptionData() {
        try (InputStream is = getClass().getResourceAsStream("/data/subscriptions.csv")) {
            if (is == null) {
                System.err.println("Fichier subscriptions.csv introuvable !");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                boolean headerSkipped = false;
                while ((line = reader.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String name = parts[0].trim();
                        String category = parts[1].trim();
                        double price = 0.0;
                        try {
                            price = Double.parseDouble(parts[2].trim());
                        } catch (NumberFormatException ignored) {
                        }
                        String color = (parts.length > 3) ? parts[3].trim() : "#3498db";
                        String logo = (parts.length > 4) ? parts[4].trim() : "";

                        allTemplates.add(new SubscriptionTemplate(name, category, price, color, logo));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Navigation Wizard ---

    private void showCategories() {
        stepContainerCategories.setVisible(true);
        stepContainerServices.setVisible(false);
        stepContainerDetails.setVisible(false);

        flowCategories.getChildren().clear();

        Set<String> categories = allTemplates.stream()
                .map(SubscriptionTemplate::getCategory)
                .collect(Collectors.toSet());
        categories.add("Autre"); // Toujours présent

        for (String cat : categories) {
            Button btn = createTileButton(cat, "#34495e");
            btn.setOnAction(e -> onCategorySelected(cat));
            flowCategories.getChildren().add(btn);
        }
    }

    private void onCategorySelected(String category) {
        this.currentSelectedCategory = category;
        showServices(category);
    }

    private void showServices(String category) {
        stepContainerCategories.setVisible(false);
        stepContainerServices.setVisible(true);
        stepContainerDetails.setVisible(false);

        flowServices.getChildren().clear();

        // Filtrer les services
        List<SubscriptionTemplate> filtered = allTemplates.stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        // Bouton "Autre / Nouveau"
        Button btnCustom = createTileButton("+ Autre...", "#7f8c8d");
        btnCustom.setOnAction(e -> showForm(new SubscriptionTemplate("", category, 0.0, "#95a5a6", "")));
        flowServices.getChildren().add(btnCustom);

        for (SubscriptionTemplate t : filtered) {
            Button btn = createTileButton(t.getName(), t.getHexColor());
            btn.setOnAction(e -> showForm(t));
            flowServices.getChildren().add(btn);
        }
    }

    private void showForm(SubscriptionTemplate template) {
        stepContainerCategories.setVisible(false);
        stepContainerServices.setVisible(false);
        stepContainerDetails.setVisible(true);

        txtNom.setText(template.getName());
        if (template.getDefaultPrice() > 0) {
            txtPrix.setText(String.valueOf(template.getDefaultPrice()));
        } else {
            txtPrix.setText("");
        }

        // Sélectionner la catégorie correcte ou l'ajouter si manquante
        if (!cmbCategorie.getItems().contains(template.getCategory())) {
            cmbCategorie.getItems().add(template.getCategory());
        }
        cmbCategorie.getSelectionModel().select(template.getCategory());
    }

    @FXML
    protected void onBackToCategories() {
        showCategories();
    }

    @FXML
    protected void onBackToServices() {
        if (currentSelectedCategory != null) {
            showServices(currentSelectedCategory);
        } else {
            showCategories();
        }
    }

    // --- UI Helpers ---

    private Button createTileButton(String text, String hexColor) {
        Button btn = new Button(text);
        btn.setPrefSize(140, 80);
        btn.setWrapText(true);
        // Apply the CSS class
        btn.getStyleClass().add("tile-button");
        // Only set the dynamic background color inline
        btn.setStyle("-fx-background-color: " + hexColor + ";");
        return btn;
    }

    // --- Logique Métier (Sauvegarde) ---

    @FXML
    protected void onBtnEnregistrerClick() {
        if (validerChamps()) {
            try {
                String nom = txtNom.getText();
                double prix = Double.parseDouble(txtPrix.getText().replace(",", "."));
                LocalDate date = dateDebut.getValue();
                String frequence = cmbFrequence.getValue();
                String categorie = cmbCategorie.getValue();

                int id = (int) (System.currentTimeMillis() % 100000);

                Abonnement nouvelAbonnement = new Abonnement(id, nom, prix, date, frequence, categorie);
                service.add(nouvelAbonnement);

                retourAuDashboard();

            } catch (NumberFormatException e) {
                afficherErreur("Le prix doit être un nombre valide.");
            }
        }
    }

    @FXML
    protected void onBtnAnnulerClick() {
        retourAuDashboard();
    }

    private void retourAuDashboard() {
        Stage currentStage = (Stage) stepContainerCategories.getScene().getWindow();
        currentStage.close();
    }

    private boolean validerChamps() {
        if (txtNom.getText() == null || txtNom.getText().trim().isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return false;
        }
        if (txtPrix.getText() == null || txtPrix.getText().trim().isEmpty()) {
            afficherErreur("Le prix est obligatoire.");
            return false;
        }
        if (dateDebut.getValue() == null) {
            afficherErreur("La date est obligatoire.");
            return false;
        }
        return true;
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }
}
