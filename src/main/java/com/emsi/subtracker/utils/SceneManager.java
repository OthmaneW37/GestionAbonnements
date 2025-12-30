package com.emsi.subtracker.utils;

import com.emsi.subtracker.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Utilitaire pour gérer la navigation entre les scènes.
 */
public class SceneManager {

    /**
     * Change la scène actuelle du Stage donné.
     * 
     * @param stage        La fenêtre principale.
     * @param fxmlFileName Le nom du fichier FXML (ex: "dashboard.fxml").
     */
    public static void switchScene(Stage stage, String fxmlFileName) throws IOException {
        try {
            // Charge le fichier FXML depuis le dossier resources/views/
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/" + fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());

            // Apply Global CSS
            scene.getStylesheets()
                    .add(Objects.requireNonNull(Main.class.getResource("/styles_v2.css")).toExternalForm());
            ThemeManager.applyTheme(scene);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue : " + fxmlFileName);
            e.printStackTrace();
        }
    }
}
