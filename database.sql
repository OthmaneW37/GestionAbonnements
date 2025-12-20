-- Create Database (Run this if database doesn't exist)
-- CREATE DATABASE subtracker_db;
-- USE subtracker_db;

-- Table: Users
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE users (
        id INT PRIMARY KEY IDENTITY(1,1),
        username NVARCHAR(50) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL -- In a real app, hash this!
    );
    
    -- Insert Default User
    INSERT INTO users (username, password) VALUES ('admin', 'admin123');
END

-- Table: Subscriptions
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='subscriptions' AND xtype='U')
BEGIN
    CREATE TABLE subscriptions (
        id INT PRIMARY KEY IDENTITY(1,1),
        name NVARCHAR(100) NOT NULL,
        price DECIMAL(10, 2) NOT NULL,
        date_start DATE NOT NULL,
        category NVARCHAR(50) NOT NULL,
        frequency NVARCHAR(20) NOT NULL, -- Mensuel, Annuel
        user_id INT,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
END
