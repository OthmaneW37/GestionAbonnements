module com.emsi.subtracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires com.microsoft.sqlserver.jdbc;

    opens com.emsi.subtracker to javafx.fxml;
    opens com.emsi.subtracker.views to javafx.fxml;
    opens com.emsi.subtracker.models to javafx.base;

    exports com.emsi.subtracker;
}
