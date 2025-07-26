package com.practice.model.print;

import com.practice.model.Customer;
import com.practice.model.LineItem;
import com.practice.model.Order;
import com.practice.model.delivery.DeliveryPartner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InvoiceGenerator {

    public static void printInvoice(Order order, Customer customer, String paymentMode, double discount, double total, DeliveryPartner assignedPartner) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                  INVOICE                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.printf("👤 Customer    : %-35s%n", customer.getName());
        System.out.printf("📧 Email       : %-35s%n", customer.getEmail());
        System.out.printf("📞 Phone       : %-35s%n", customer.getPhoneNumber());
        System.out.printf("🏠 Address     : %-35s%n", customer.getAddress());
        System.out.printf("🆔 Order ID    : %-35d%n", order.getId());
        System.out.printf("📅 Date        : %-35s%n", sdf.format(new Date()));
        System.out.printf("💳 Payment     : %-35s%n", paymentMode);
        System.out.println();

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                📦 ORDER DETAILS                  ║");
        System.out.println("╟──────────────┬────────┬──────────┬──────────────╢");
        System.out.printf("║ %-12s │ %-6s │ %-8s │ %-12s ║%n", "Item", "Qty", "₹Unit", "₹Total");
        System.out.println("╟──────────────┼────────┼──────────┼──────────────╢");

        List<LineItem> items = order.getItems();
        for (LineItem item : items) {
            String itemName = truncate(item.getFoodItem().getName(), 12);
            int quantity = item.getQuantity();
            double unitPrice = item.getFoodItem().getDiscountedPrice();
            double totalItemPrice = item.calculateLineItemCost();
            System.out.printf("║ %-12s │ %-6d │ %-8.2f │ %-12.2f ║%n", itemName, quantity, unitPrice, totalItemPrice);
        }

        System.out.println("╚══════════════╧════════╧══════════╧══════════════╝");

        // Discounts and total
        if (discount > 0) {
            System.out.printf("🎁 Discount Applied : -₹%.2f%n", discount);
        } else {
            System.out.println("🎁 Discount Applied : ₹0.00");
        }

        System.out.printf("💰 Total Paid       : ₹%.2f%n", total);
        System.out.println();

        // Delivery partner info
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║              🚚 DELIVERY DETAILS                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        if (assignedPartner != null) {
            System.out.printf("🚚 Delivered By     : %s%n", assignedPartner.getName());
            System.out.printf("📞 Contact No.      : %s%n", assignedPartner.getContactNumber());
        } else {
            System.out.println("❌ Delivery partner not assigned.");
        }

        System.out.println();
        System.out.println("🙏 Thank you for your order! We hope to serve you again. 🙏");
        System.out.println("══════════════════════════════════════════════════════");
    }

    public static void printInvoice(Order order, Customer customer) {
        printInvoice(
                order,
                customer,
                order.getPaymentMode(),
                order.getDiscount(),
                order.getTotalPaid(),
                order.getDeliveryPartner()
        );
    }

    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
}
