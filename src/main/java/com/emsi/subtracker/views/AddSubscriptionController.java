package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Contrôleur du formulaire d'ajout d'abonnement.
 */
public class AddSubscriptionController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des ComboBox
        cmbFrequence.getItems().addAll("Mensuel", "Annuel");
        cmbFrequence.getSelectionModel().selectFirst();

        cmbCategorie.getItems().addAll("Divertissement", "Travail", "Sport", "Musique", "Autre");
        cmbCategorie.getSelectionModel().select("Autre");

        // Date par défaut = aujourd'hui
        dateDebut.setValue(LocalDate.now());

        // Platform.runLater to ensure Scene is not null
        javafx.application.Platform.runLater(() -> {
            if (txtNom.getScene() != null) {
                com.emsi.subtracker.utils.ThemeManager.applyTheme(txtNom.getScene());
            }
        });
    }

    private Abonnement currentAbonnement;

    @FXML
    protected void onBtnEnregistrerClick() {
        if (validerChamps()) {
            try {
                // 1. Récupération des valeurs
                String nom = txtNom.getText();
                double prix = Double.parseDouble(txtPrix.getText());
                LocalDate date = dateDebut.getValue();
                String frequence = cmbFrequence.getValue();
                String categorie = cmbCategorie.getValue();

                if (currentAbonnement != null) {
                    // Update existing
                    currentAbonnement.setNom(nom);
                    currentAbonnement.setPrix(prix);
                    currentAbonnement.setDateDebut(date);
                    currentAbonnement.setFrequence(frequence);
                    currentAbonnement.setCategorie(categorie);
                    service.update(currentAbonnement);
                } else {
                    // Create new
                    // Génération d'un ID (timestamp simple pour l'exemple)
                    int id = (int) (System.currentTimeMillis() % 100000);
                    Abonnement nouvelAbonnement = new Abonnement(id, nom, prix, date, frequence, categorie);
                    service.add(nouvelAbonnement);
                }

                // 3. Retour au Dashboard
                retourAuDashboard();

            } catch (NumberFormatException e) {
                afficherErreur("Le prix doit être un nombre valide.");
            }
        }
    }

    public void setAbonnement(Abonnement abonnement) {
        this.currentAbonnement = abonnement;
        if (abonnement != null) {
            txtNom.setText(abonnement.getNom());
            txtPrix.setText(String.valueOf(abonnement.getPrix()));
            dateDebut.setValue(abonnement.getDateDebut());
            cmbFrequence.setValue(abonnement.getFrequence());
            cmbCategorie.setValue(abonnement.getCategorie());
        }
    }

    @FXML
    protected void onBtnAnnulerClick() {
        retourAuDashboard();
    }

    private void retourAuDashboard() {
        Stage currentStage = (Stage) txtNom.getScene().getWindow();
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
