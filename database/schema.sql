-- Create Database (Run this separately if needed, or ensure DB exists)
-- CREATE DATABASE subtracker_db;
-- GO

-- USE subtracker_db;
-- GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[abonnements]') AND type in (N'U'))
BEGIN
    CREATE TABLE abonnements (
        id INT PRIMARY KEY IDENTITY(1,1),
        nom NVARCHAR(100) NOT NULL,
        prix DECIMAL(10, 2) NOT NULL,
        date_debut DATE NOT NULL,
        frequence NVARCHAR(20) NOT NULL,
        categorie NVARCHAR(50)
    );
END
GO

-- Sample Data (Optional)
-- INSERT INTO abonnements (nom, prix, date_debut, frequence, categorie) VALUES 
-- ('Netflix', 15.99, '2024-01-01', 'Mensuel', 'Divertissement');
