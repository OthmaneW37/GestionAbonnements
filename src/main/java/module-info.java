module com.emsi.subtracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires com.microsoft.sqlserver.jdbc;

<<<<<<< HEAD
    // Jakarta Mail for SMTP email notifications (Mailtrap)
    requires jakarta.mail;
    requires jakarta.activation;

    opens com.emsi.subtracker to javafx.fxml;
    opens com.emsi.subtracker.views to javafx.fxml;
=======
    opens com.emsi.subtracker.controllers to javafx.fxml;
>>>>>>> 981914bfcf7f22d4c8c16c2ebb471e388aa49dbd
    opens com.emsi.subtracker.models to javafx.base;

    exports com.emsi.subtracker;
    exports com.emsi.subtracker.controllers;
    exports com.emsi.subtracker.models;
    exports com.emsi.subtracker.utils;
    exports com.emsi.subtracker.services;
}
