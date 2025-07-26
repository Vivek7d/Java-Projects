package com.practice.model.print;

import java.util.List;
import com.practice.model.LineItem;

public class CartPrinter {

    public static void printCart(List<LineItem> cart) {
        if (cart == null || cart.isEmpty()) {
            System.out.println("🛒 Your cart is empty.");
            return;
        }

        System.out.println("\n======================= Your Cart =======================\n");
        System.out.printf("%-5s %-20s %-10s %-15s %-15s%n", "ID", "Food Item", "Quantity", "Unit Price (₹)", "Total (₹)");
        System.out.println("---------------------------------------------------------------------");

        double cartSubtotal = 0.0;
        for (LineItem item : cart) {
            int id = item.getFoodItem().getId();
            String foodName = item.getFoodItem().getName();
            int qty = item.getQuantity();
            double unitPrice = item.getFoodItem().getDiscountedPrice();
            double total = unitPrice * qty;
            cartSubtotal += total;

            System.out.printf("%-5d %-20s %-10d %-15.2f %-15.2f%n", id, foodName, qty, unitPrice, total);
        }

        System.out.println("---------------------------------------------------------------------");
        System.out.printf("%60s ₹%.2f%n", "Subtotal:", cartSubtotal);

        if (cartSubtotal > 500) {
            double discount = cartSubtotal * 0.10;
            double discountedTotal = cartSubtotal - discount;
            System.out.printf("%60s -₹%.2f%n", "Discount (10%):", discount);
            System.out.printf("%60s ₹%.2f%n", "Total After Discount:", discountedTotal);
        } else {
            System.out.printf("%60s ₹%.2f%n", "Total Cart Amount:", cartSubtotal);
        }

        System.out.println("==============================================================\n");
    }
}
