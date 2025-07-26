package com.practice.model;

import java.io.Serializable;
import java.util.*;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private List<Order> orders;
    private List<Order> orderHistory = new ArrayList<>();
    private List<FoodItem> wishlist = new ArrayList<>();
    public Customer() {
        this.orders = new ArrayList<>();
    }

    public Customer(int id, String name, String address, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.orders = new ArrayList<>();
    }
    public List<FoodItem> getWishlist() {
        return wishlist;
    }

    public void addToWishlist(FoodItem item) {
        wishlist.add(item);
    }

    public void removeFromWishlist(FoodItem item) {
        wishlist.remove(item);
    }
    // Getters & Setters
    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
