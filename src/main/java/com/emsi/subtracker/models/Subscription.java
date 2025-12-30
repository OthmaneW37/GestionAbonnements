package com.emsi.subtracker.models;

import java.time.LocalDate;

public class Subscription {
    private int id;
    private String name;
    private double price;
    private LocalDate dateStart;
    private String category;
    private String frequency;
    private int userId;

    public Subscription(int id, String name, double price, LocalDate dateStart, String category, String frequency,
            int userId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dateStart = dateStart;
        this.category = category;
        this.frequency = frequency;
        this.userId = userId;
    }

    public Subscription(String name, double price, LocalDate dateStart, String category, String frequency, int userId) {
        this.name = name;
        this.price = price;
        this.dateStart = dateStart;
        this.category = category;
        this.frequency = frequency;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
