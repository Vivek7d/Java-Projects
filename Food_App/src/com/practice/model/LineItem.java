package com.practice.model;

public class LineItem {
    private int id;
    private int quantity;
    private FoodItem foodItem;

    public LineItem(int id, int quantity, FoodItem foodItem) {
        this.id = id;
        this.quantity = quantity;
        this.foodItem = foodItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public double calculateLineItemCost() {
        return quantity * foodItem.getDiscountedPrice();
    }

    @Override
    public String toString() {
        return "LineItem [ID=" + id +
               ", Quantity=" + quantity +
               ", FoodItem=" + foodItem.getName() +
               ", Unit Price=₹" + String.format("%.2f", foodItem.getDiscountedPrice()) +
               ", Total=₹" + String.format("%.2f", calculateLineItemCost()) + "]";
    }
}
