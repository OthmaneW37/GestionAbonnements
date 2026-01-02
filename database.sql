-- =============================================
-- Script d'initialisation de la base de données SubTracker
-- A exécuter sur SQL Server
-- =============================================

-- 1. Création de la Base de Données
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'subtracker_db')
BEGIN
    CREATE DATABASE subtracker_db;
END
GO

USE subtracker_db;
GO

-- 2. Table: Users
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE users (
        id INT PRIMARY KEY IDENTITY(1,1),
        username NVARCHAR(50) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        email NVARCHAR(255),
        account_type VARCHAR(20) DEFAULT 'individual', -- 'individual' or 'family'
        profile_picture VARCHAR(255) NULL
    );
    
    -- Insert Default User
    INSERT INTO users (username, password, email, account_type) 
    VALUES ('admin', 'admin123', 'admin@email.com', 'individual');
END
GO

-- 3. Table: Family Members
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='family_members' AND xtype='U')
BEGIN
    CREATE TABLE family_members (
        id INT PRIMARY KEY IDENTITY(1,1),
        user_id INT NOT NULL,
        name NVARCHAR(100) NOT NULL,
        username NVARCHAR(50) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        created_at DATETIME DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
END
GO

-- 4. Table: Abonnements
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='abonnements' AND xtype='U')
BEGIN
    CREATE TABLE abonnements (
        id INT PRIMARY KEY IDENTITY(1,1),
        nom NVARCHAR(100) NOT NULL,
        prix DECIMAL(10, 2) NOT NULL,
        date_debut DATE NOT NULL,
        frequence NVARCHAR(20) NOT NULL, -- Mensuel, Annuel
        categorie NVARCHAR(50) NOT NULL,
        user_id INT,
        assigned_to_member_id INT NULL,
        logo_url VARCHAR(500) NULL,
        color_hex VARCHAR(20) NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (assigned_to_member_id) REFERENCES family_members(id) -- Optional: ON DELETE SET NULL
    );
END
GO

print 'Base de données SubTracker initialisée avec succès !';

