package com.practice.model.print;

import java.util.List;
import com.practice.model.LineItem;

public class CartPrinter {

    public static void printCart(List<LineItem> cart) {
        if (cart == null || cart.isEmpty()) {
            System.out.println("🛒 Your cart is empty.");
            return;
        }

        System.out.println("\n======================= 🛒 YOUR CART =======================\n");

        // Print table header
        printCartBorder();
        System.out.printf("| %-4s | %-20s | %-8s | %-14s | %-12s |%n",
                "ID", "Food Item", "Qty", "Unit Price (₹)", "Total (₹)");
        printCartBorder();

        double cartSubtotal = 0.0;

        for (LineItem item : cart) {
            int id = item.getFoodItem().getId();
            String foodName = item.getFoodItem().getName();
            int qty = item.getQuantity();
            double unitPrice = item.getFoodItem().getDiscountedPrice();
            double total = unitPrice * qty;
            cartSubtotal += total;

            System.out.printf("| %-4d | %-20s | %-8d | ₹%-13.2f | ₹%-11.2f |%n",
                    id, foodName, qty, unitPrice, total);
        }

        printCartBorder();

        // Print totals
        System.out.println();
        System.out.println("+---------------------------------------------------------------+");
        System.out.printf("| %-45s ₹%-10.2f |\n", "Subtotal:", cartSubtotal);

        if (cartSubtotal > 500) {
            double discount = cartSubtotal * 0.10;
            double totalAfterDiscount = cartSubtotal - discount;
            System.out.printf("| %-45s -₹%-9.2f |\n", "Discount (10%):", discount);
            System.out.printf("| %-45s ₹%-10.2f |\n", "Total After Discount:", totalAfterDiscount);
        } else {
            System.out.printf("| %-45s ₹%-10.2f |\n", "Total Cart Amount:", cartSubtotal);
        }

        System.out.println("+---------------------------------------------------------------+");
        System.out.println("\n✅ Please proceed to checkout!\n");
    }

    private static void printCartBorder() {
        System.out.println("+------+----------------------+----------+----------------+--------------+");
    }
}
