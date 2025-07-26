package com.practice.model.payment;

import java.util.List;
import java.util.Scanner;

import com.practice.model.exception.PaymentException;
import com.practice.model.exception.NegativeValueException; // ✅ Import the new exception

public class PaymentProcessor {
    private static final Scanner sc = new Scanner(System.in);

    public static String selectPaymentMode() throws PaymentException {
        List<PaymentMethod> methods = PaymentManager.loadMethods();
        if (methods.isEmpty()) {
            throw new PaymentException("No payment methods available. Please contact admin.");
        }

        System.out.println("\nAvailable Payment Methods:");
        for (PaymentMethod method : methods) {
            System.out.println(method);
        }

        System.out.print("Enter your choice: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new PaymentException("Invalid input. Please enter a number.");
        }

        PaymentMethod selected = methods.stream()
                .filter(m -> m.getId() == choice)
                .findFirst()
                .orElse(null);

        if (selected == null) {
            throw new PaymentException("Invalid payment method selected.");
        }

        switch (selected.getMethodName().toLowerCase()) {
        case "upi":
            try {
                return handleUPIPayment();
            } catch (NegativeValueException e) {
                throw new PaymentException("❌ " + e.getMessage());
            }
        case "card":
            return handleCardPayment();
        default:
            return selected.getMethodName(); // Cash, Wallet, etc.
    }
    }

    private static String handleUPIPayment() throws PaymentException, NegativeValueException {
        System.out.print("Enter UPI ID: ");
        String upiId = sc.nextLine().trim();

        System.out.print("Enter UPI PIN: ");
        String upiPin = sc.nextLine().trim();

        if (upiId.isEmpty()) {
            throw new PaymentException("UPI ID cannot be empty.");
        }

        if (!upiPin.matches("-?\\d{4}")) {
            throw new PaymentException("Invalid UPI PIN. It must be a 4-digit number.");
        }

        if (upiPin.startsWith("-")) {
            throw new NegativeValueException("UPI PIN cannot be negative.");
        }

        System.out.println("✅ UPI payment successful.");
        return "UPI";
    }
    private static String handleCardPayment() throws PaymentException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Card Number (16 digits): ");
        String cardNumber = scanner.nextLine().trim();

        if (!cardNumber.matches("\\d{16}")) {
            throw new PaymentException("Invalid card number. Must be 16 digits.");
        }

        System.out.print("Enter Cardholder Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            throw new PaymentException("Cardholder name cannot be empty.");
        }

        System.out.print("Enter Expiry Date (MM/YY): ");
        String expiry = scanner.nextLine().trim();
        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new PaymentException("Invalid expiry format. Use MM/YY.");
        }

        System.out.print("Enter CVV (3 digits): ");
        String cvv = scanner.nextLine().trim();
        if (!cvv.matches("\\d{3}")) {
            throw new PaymentException("Invalid CVV. Must be 3 digits.");
        }

        // Simulate OTP Generation
        int otp = (int) (100000 + Math.random() * 900000);
        System.out.println("🔐 OTP Sent to your registered mobile/email: " + otp);

        System.out.print("Enter OTP: ");
        String enteredOtp = scanner.nextLine().trim();

        if (!enteredOtp.equals(String.valueOf(otp))) {
            throw new PaymentException("❌ Invalid OTP. Payment failed.");
        }

        System.out.println("✅ Card payment successful.");
        return "Card";
    }

}
