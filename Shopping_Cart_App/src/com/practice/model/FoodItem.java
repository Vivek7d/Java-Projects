package com.practice.model;

import java.io.Serializable;

public class FoodItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
    private String name;
    private double price;
    private double discount; // in percentage (e.g., 10 means 10% off)
    private String cuisineType; // Indian, Italian, Chinese
    private String category;    // Starter, Main Course, Dessert

    public FoodItem(int id, String name, double price, double discount, String cuisineType, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.cuisineType = cuisineType;
        this.category = category;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDiscountedPrice() {
        return price * (1 - discount / 100.0);
    }

    @Override
    public String toString() {
        return "FoodItem [ID=" + id +
               ", Name=" + name +
               ", Cuisine=" + cuisineType +
               ", Category=" + category +
               ", Price=₹" + price +
               ", Discount=" + discount + "%" +
               ", Final Price=₹" + String.format("%.2f", getDiscountedPrice()) + "]";
    }
}
