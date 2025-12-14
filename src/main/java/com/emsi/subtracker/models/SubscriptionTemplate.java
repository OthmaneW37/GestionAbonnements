package com.emsi.subtracker.models;

public class SubscriptionTemplate {
    private String name;
    private String category;
    private double defaultPrice;
    private String hexColor;
    private String logoUrl;

    public SubscriptionTemplate(String name, String category, double defaultPrice, String hexColor, String logoUrl) {
        this.name = name;
        this.category = category;
        this.defaultPrice = defaultPrice;
        this.hexColor = hexColor;
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public String getHexColor() {
        return hexColor;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
