package com.emsi.subtracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
<<<<<<< HEAD
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(Main.class.getResource("/styles_v2.css").toExternalForm());
        stage.setTitle("Subscription Tracker");
        stage.setScene(scene);
        stage.show();
=======
        // Init Database
        com.emsi.subtracker.utils.DatabaseInitializer.initialize();
>>>>>>> 981914bfcf7f22d4c8c16c2ebb471e388aa49dbd

        primaryStage = stage;
        setRoot("views/login");
        primaryStage.setTitle("Subscription Tracker");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/" + fxml + ".fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root); // New Scene for each switch to ensure clean state or reuse if optimized
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
