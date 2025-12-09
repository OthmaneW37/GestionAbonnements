package com.emsi.subtracker.util;

import com.emsi.subtracker.config.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private final HikariDataSource dataSource;

    private DatabaseConnection() {
        try {
            Class.forName(DatabaseConfig.getDriver());

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DatabaseConfig.getUrl());
            config.setUsername(DatabaseConfig.getUsername());
            config.setPassword(DatabaseConfig.getPassword());

            // Optional Hikari settings
            config.setConnectionTimeout(DatabaseConfig.getLongProperty("hikari.connectionTimeout", 30000));
            config.setIdleTimeout(DatabaseConfig.getLongProperty("hikari.idleTimeout", 600000));
            config.setMaxLifetime(DatabaseConfig.getLongProperty("hikari.maxLifetime", 1800000));
            config.setMinimumIdle(DatabaseConfig.getIntProperty("hikari.minimumIdle", 5));
            config.setMaximumPoolSize(DatabaseConfig.getIntProperty("hikari.maximumPoolSize", 10));
            config.setPoolName(DatabaseConfig.getProperty("hikari.poolName", "SubTrackerPool"));

            dataSource = new HikariDataSource(config);
            System.out.println("✓ Database connection pool initialized successfully");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("✓ Database connection pool closed");
        }
    }
}
