package com.practice.model;

import java.text.SimpleDateFormat;
import java.util.*;

public class Order {
    private int id;
    private Date date;
    private List<LineItem> items;
    private DeliveryPartner deliveryPartner; // Optional - if you're assigning a partner

    public Order() {
        this.items = new ArrayList<>();
        this.date = new Date();
    }

    public Order(int id, Date date) {
        this.id = id;
        this.date = date;
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public void addLineItem(LineItem item) {
        this.items.add(item);
    }

    public double calculateOrderPrice() {
        double total = 0.0;
        for (LineItem item : items) {
            total += item.calculateLineItemCost();
        }
        return total;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Order Invoice ---\n");
        sb.append("Order ID: ").append(id).append("\n");
        sb.append("Date: ").append(sdf.format(date)).append("\n\n");

        for (LineItem item : items) {
            FoodItem food = item.getFoodItem();
            sb.append(food.getName()).append(" - ₹")
              .append(food.getPrice()).append(" - ")
              .append(food.getDiscountedPrice()).append("% off => ₹")
              .append(String.format("%.2f", food.getDiscountedPrice()))
              .append(" x ").append(item.getQuantity())
              .append(" = ₹").append(String.format("%.2f", item.calculateLineItemCost()))
              .append("\n");
        }

        sb.append("\nTotal Amount Payable: ₹")
          .append(String.format("%.2f", calculateOrderPrice()))
          .append("\n");

        if (deliveryPartner != null) {
            sb.append("Delivery Partner: ").append(deliveryPartner.getName()).append("\n");
        }

        return sb.toString();
    }
}
