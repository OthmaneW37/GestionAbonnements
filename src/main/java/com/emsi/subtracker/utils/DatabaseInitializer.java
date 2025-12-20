package com.emsi.subtracker.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String SERVER_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=master;encrypt=true;trustServerCertificate=true;";
    private static final String DB_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=subtracker_db;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "password";

    public static void initialize() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 1. Connect to 'master' to create the database if it doesn't exist
            try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
                    Statement stmt = conn.createStatement()) {

                String createDbQuery = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'subtracker_db') " +
                        "BEGIN CREATE DATABASE subtracker_db; END";
                stmt.executeUpdate(createDbQuery);
                System.out.println("Database 'subtracker_db' checked/created.");
            }

            // 2. Connect to 'subtracker_db' to create tables
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                    Statement stmt = conn.createStatement()) {

                // Table Users
                String createUsers = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U') " +
                        "BEGIN " +
                        "CREATE TABLE users (" +
                        "id INT PRIMARY KEY IDENTITY(1,1), " +
                        "username NVARCHAR(50) NOT NULL UNIQUE, " +
                        "password NVARCHAR(255) NOT NULL" +
                        "); " +
                        "INSERT INTO users (username, password) VALUES ('admin', 'admin123'); " +
                        "END";
                stmt.executeUpdate(createUsers);

                // Table Subscriptions
                String createSubs = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='subscriptions' AND xtype='U') "
                        +
                        "BEGIN " +
                        "CREATE TABLE subscriptions (" +
                        "id INT PRIMARY KEY IDENTITY(1,1), " +
                        "name NVARCHAR(100) NOT NULL, " +
                        "price DECIMAL(10, 2) NOT NULL, " +
                        "date_start DATE NOT NULL, " +
                        "category NVARCHAR(50) NOT NULL, " +
                        "frequency NVARCHAR(20) NOT NULL, " +
                        "user_id INT, " +
                        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                        "); " +
                        "END";
                stmt.executeUpdate(createSubs);

                System.out.println("Tables checked/created.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fatal Error during Database Initialization: " + e.getMessage());
        }
    }
}
