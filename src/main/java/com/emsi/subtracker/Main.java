package com.emsi.subtracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        scene.getStylesheets().add(Main.class.getResource("/styles_v2.css").toExternalForm());
        stage.setTitle("Subscription Tracker");
        stage.getIcons().add(new javafx.scene.image.Image(Main.class.getResourceAsStream("/images/LogoST.png")));
        stage.setScene(scene);
        stage.show();

        // Démarrage du thread de fond (obligatoire pour le projet)
        startBackgroundService();
    }

    private void startBackgroundService() {
        Thread backgroundThread = new Thread(() -> {
            while (true) {
                try {
                    // Simulation d'une tâche de fond (ex: vérification des dates de paiement)
                    System.out.println("[Background Thread] Vérification des abonnements en arrière-plan...");
                    Thread.sleep(10000); // Pause de 10 secondes
                } catch (InterruptedException e) {
                    System.out.println("[Background Thread] Interrompu.");
                    break;
                }
            }
        });
        backgroundThread.setDaemon(true); // Permet l'arrêt du thread quand l'application ferme
        backgroundThread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
