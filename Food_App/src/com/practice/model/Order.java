package com.practice.model;

import java.text.SimpleDateFormat;
import java.util.*;

import com.practice.model.delivery.DeliveryPartner;

public class Order {
    private int id;
    private Date date;
    private List<LineItem> items;
    private DeliveryPartner deliveryPartner; 
    private String paymentMode;
    private double discount;
    private double totalPaid;

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
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }
    public double calculateOrderPrice() {
        double total = 0.0;
        for (LineItem item : items) {
            total += item.calculateLineItemCost();
        }
        return total;
    }
    public String generateBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("\n========== INVOICE ==========\n");
        bill.append("Order ID: ").append(this.id).append("\n");
        bill.append("Date    : ").append(this.date).append("\n");

        bill.append("\nItems:\n");
        bill.append(String.format("%-20s %-10s %-10s %-10s\n", "Item", "Qty", "Price", "Total"));

        double totalAmount = 0.0;
        for (LineItem item : items) {
            String name = item.getFoodItem().getName();
            int qty = item.getQuantity();
            double price = item.getFoodItem().getDiscountedPrice();
            double total = item.calculateLineItemCost();

            bill.append(String.format("%-20s %-10d ₹%-9.2f ₹%-9.2f\n", name, qty, price, total));
            totalAmount += total;
        }

        // Optional discount for large orders
        double discount = 0.0;
        if (totalAmount > 500) {
            discount = totalAmount * 0.10;
            bill.append("\nDiscount (10% on orders > ₹500): -₹").append(String.format("%.2f", discount)).append("\n");
        }

        bill.append("------------------------------\n");
        bill.append("Total Payable: ₹").append(String.format("%.2f", totalAmount - discount)).append("\n");
        bill.append("==============================\n");

        return bill.toString();
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
