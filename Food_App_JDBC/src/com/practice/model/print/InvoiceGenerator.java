package com.practice.model.print;

import com.practice.model.Customer;
import com.practice.model.LineItem;
import com.practice.model.Order;
import com.practice.model.delivery.DeliveryPartner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceGenerator {

    private static final int WIDTH = 52;

    public static void printInvoice(Order order, Customer customer, String paymentMode, double discount, double total, DeliveryPartner partner) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        printDoubleLine('=', '=');
        printCentered("INVOICE");
        printDoubleLine('=', '=');

        printKeyValue("Customer", customer.getName());
        printKeyValue("Email", customer.getEmail());
        printKeyValue("Phone", customer.getPhoneNumber());
        printKeyValue("Address", customer.getAddress());
        printKeyValue("Order ID", String.valueOf(order.getId()));
        printKeyValue("Date", sdf.format(new Date()));
        printKeyValue("Payment Mode", paymentMode);
        printSingleLine('-', '-');

        printCentered("ORDER DETAILS");
        printDoubleLine('=', '=');

        int itemColWidth = 18;  // Item name max width
        int qtyColWidth = 3;
        int unitColWidth = 9;
        int totalColWidth = 9;

        System.out.printf("| %-"+itemColWidth+"s | %"+qtyColWidth+"s | %"+unitColWidth+"s | %"+totalColWidth+"s |%n",
                          "Item", "Qty", "Unit ₹", "Total ₹");
        printSingleLine('-', '-');

        for (LineItem item : order.getItems()) {
            String name = truncate(item.getFoodItem().getName(), itemColWidth);
            int qty = item.getQuantity();
            double unit = item.getFoodItem().getDiscountedPrice();
            double lineTotal = unit * qty;

            System.out.printf("| %-"+itemColWidth+"s | %"+qtyColWidth+"d | ₹%"+(unitColWidth-1)+".2f | ₹%"+(totalColWidth-1)+".2f |%n",
                              name, qty, unit, lineTotal);
        }

        printSingleLine('-', '-');
        printKeyValue("Discount Applied", "-₹" + String.format("%.2f", discount));
        printKeyValue("Total Paid", "₹" + String.format("%.2f", total));
        printSingleLine('-', '-');

        printCentered("DELIVERY DETAILS");
        printDoubleLine('=', '=');

        if (partner != null) {
            printKeyValue("Delivered By", partner.getName());
            printKeyValue("Contact Number", partner.getContactNumber());
        } else {
            printKeyValue("Delivery", "❌ Not Assigned");
        }

        printDoubleLine('=', '=');
        printCentered("Thank you for your order!");
        printDoubleLine('=', '=');
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

    private static void printSingleLine(char fill, char edge) {
        System.out.print('|');
        for (int i = 0; i < WIDTH - 2; i++) System.out.print(fill);
        System.out.println('|');
    }

    private static void printDoubleLine(char fill, char edge) {
        System.out.print('+');
        for (int i = 0; i < WIDTH - 2; i++) System.out.print(fill);
        System.out.println('+');
    }

    private static void printCentered(String text) {
        int padding = (WIDTH - 2 - text.length()) / 2;
        String spaces = " ".repeat(Math.max(0, padding));
        System.out.printf("|%s%s%s|%n", spaces, text, " ".repeat(WIDTH - 2 - spaces.length() - text.length()));
    }

    private static void printKeyValue(String label, String value) {
        if (label.length() > 18) label = label.substring(0, 18);
        if (value.length() > WIDTH - 22) value = value.substring(0, WIDTH - 22);
        System.out.printf("| %-18s : %-27s |%n", label, value);
    }

    private static String truncate(String text, int maxLength) {
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }
}
