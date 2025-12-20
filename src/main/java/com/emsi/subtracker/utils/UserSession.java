package com.emsi.subtracker.utils;

import com.emsi.subtracker.models.User;

public class UserSession {

    private static UserSession instance;

    private User user;
    private String currency = "DH"; // Default currency

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void cleanUserSession() {
        user = null; // or instance = null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "user=" + user +
                '}';
    }
}
