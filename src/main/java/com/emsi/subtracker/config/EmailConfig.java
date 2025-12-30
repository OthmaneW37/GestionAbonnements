package com.emsi.subtracker.config;

/**
 * Configuration SMTP pour Mailtrap (Email Testing).
 * 
 * Mailtrap est parfait pour le développement - tous les emails sont capturés
 * dans une inbox de test, aucun email n'est envoyé en production.
 * 
 * Credentials déjà configurés pour le sandbox Mailtrap.
 */
public class EmailConfig {

    // Configuration SMTP Gmail
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "587";
    public static final boolean SMTP_AUTH = true;
    public static final boolean SMTP_TLS_ENABLE = true;
    public static final boolean SMTP_SSL_ENABLE = false;

    // Credentials Mailtrap (déjà configurés)
    public static final String EMAIL_USERNAME = "othmanemoussawi@gmail.com";
    public static final String EMAIL_PASSWORD = "fncm pvkt ljpj hfhj";

    // Adresse d'envoi (doit correspondre à EMAIL_USERNAME pour Gmail)
    public static final String EMAIL_FROM = "othmanemoussawi@gmail.com";
    public static final String EMAIL_FROM_NAME = "SubTracker App";

    /**
     * Vérifie si la configuration est valide.
     */
    public static boolean isConfigured() {
        return true; // Toujours configuré avec Mailtrap
    }
}
