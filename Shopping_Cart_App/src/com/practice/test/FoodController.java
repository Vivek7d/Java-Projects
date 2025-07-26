package com.practice.test;

import java.util.*;
import com.practice.model.*;

public class FoodController {
    private static Scanner sc = new Scanner(System.in);
    private static FoodManager foodManager = new FoodManager();
    private static List<LineItem> cart = new ArrayList<>();
    private static List<DeliveryPartner> deliveryPartners = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Choose role: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: adminMenu(); break;
                case 2: customerMenu(); break;
                case 3: System.exit(0); break;
                default: System.out.println("Invalid option."); break;
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Food Menu");
            System.out.println("2. Remove Food Item");
            System.out.println("3. Update Food Item");
            System.out.println("4. Display All Food Items");
            System.out.println("5. Add Delivery Partner");
            System.out.println("6. View Delivery Partners");
            System.out.println("7. Search Food by Name");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: addFoodItem(); break;
                case 2: removeFoodItem(); break;
                case 3: updateFoodItem(); break;
                case 4: displayFoodItems(); break;
                case 5: addDeliveryPartner(); break;
                case 6: viewDeliveryPartners(); break;
                case 7: searchFoodByName(); break;
                case 8: return;
                default: System.out.println("Invalid option."); break;
            }
        }
    }

    private static void customerMenu() {
    	Customer customer = createCustomer();
        cart.clear();
        cart.clear();

        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Food Menu");
            System.out.println("2. Add Food to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Remove Item from Cart");
            System.out.println("5. Place Order & Get Bill");
            System.out.println("6. Search Food by Name");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: displayFoodItems(); break;
                case 2: addItemToCart(); break;
                case 3: viewCart(); break;
                case 4: removeItemFromCart(); break;
                case 5: placeOrder(customer); break;
                case 6: searchFoodByName(); break;
                case 7: return;
                default: System.out.println("Invalid option."); break;
            }
        }
    }
    private static Customer createCustomer() {
        sc.nextLine(); // Consume leftover newline

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = sc.nextLine();

        System.out.print("Enter your email: ");
        String email = sc.nextLine();

        int customerId = new Random().nextInt(1000); // You can replace with actual ID logic

        return new Customer(customerId, name, address, phone, email);
    }

    private static void addFoodItem() {
        System.out.print("Enter Food ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Food Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Discount Percent: ");
        double discount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Cuisine Type (Indian, Italian, Chinese): ");
        String cuisineType = sc.nextLine();
        System.out.print("Enter Category (Starter, Main Course, Dessert): ");
        String category = sc.nextLine();

        FoodItem food = new FoodItem(id, name, price, discount, cuisineType, category);
        foodManager.addFood(food);
        System.out.println("Food item added successfully.");
    }

    private static void removeFoodItem() {
        System.out.print("Enter Food ID to remove: ");
        int id = sc.nextInt();
        if (foodManager.removeFood(id))
            System.out.println("Food item removed.");
        else
            System.out.println("Food item not found.");
    }

    private static void updateFoodItem() {
        System.out.print("Enter Food ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Price: ");
        double price = sc.nextDouble();

        System.out.print("Enter New Discount: ");
        double discount = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter New Cuisine Type: ");
        String cuisine = sc.nextLine();

        System.out.print("Enter New Category: ");
        String category = sc.nextLine();

        FoodItem updatedFood = new FoodItem(id, name, price, discount, cuisine, category);

        if (foodManager.updateFood(id, updatedFood))
            System.out.println("Food item updated.");
        else
            System.out.println("Food item not found.");
    }

    private static void displayFoodItems() {
        List<FoodItem> foodItems = foodManager.getAllFoods();

        if (foodItems.isEmpty()) {
            System.out.println("No food items available.");
            return;
        }

        System.out.println("\n----------- Full Menu -----------\n");

        // Loop through each cuisine type
        for (String cuisine : List.of("Indian", "Italian", "Chinese")) {
            System.out.println("🍽️ " + cuisine + " Cuisine:");

            // Filter food items of this cuisine
            List<FoodItem> cuisineItems = new ArrayList<>();
            for (FoodItem item : foodItems) {
                if (item.getCuisineType().equalsIgnoreCase(cuisine)) {
                    cuisineItems.add(item);
                }
            }

            // Now loop through each category inside the cuisine
            for (String category : List.of("Starter", "Main Course", "Dessert")) {
                System.out.println("\n➡️ " + category + ":\n");

                // Print table header
                System.out.printf("%-5s %-20s %-12s %-12s %-12s%n",
                        "ID", "Name", "Price", "Discount", "Final Price");
                System.out.println("---------------------------------------------------------------");

                // Print food items in that category
                for (FoodItem food : cuisineItems) {
                    if (food.getCategory().equalsIgnoreCase(category)) {
                        System.out.printf("%-5d %-20s ₹%-11.2f %-11.1f ₹%-11.2f%n",
                                food.getId(),
                                food.getName(),
                                food.getPrice(),
                                food.getDiscount(),
                                food.getDiscountedPrice());
                    }
                }
            }

            System.out.println(); // Add space between cuisines
        }
    }


    
    private static FoodItem selectFoodByCuisineAndCategory() {
        sc.nextLine(); 

        System.out.println("\nSelect Cuisine Type:");
        System.out.println("1. Indian");
        System.out.println("2. Italian");
        System.out.println("3. Chinese");
        System.out.print("Enter your choice: ");
        int cuisineChoice = sc.nextInt();

        String cuisine = switch (cuisineChoice) {
            case 1 -> "Indian";
            case 2 -> "Italian";
            case 3 -> "Chinese";
            default -> {
                System.out.println("❌ Invalid cuisine choice.");
                yield null;
            }
        };

        if (cuisine == null) return null;

        System.out.println("\nSelect Category:");
        System.out.println("1. Starter");
        System.out.println("2. Main Course");
        System.out.println("3. Dessert");
        System.out.print("Enter your choice: ");
        int categoryChoice = sc.nextInt();

        String category = switch (categoryChoice) {
            case 1 -> "Starter";
            case 2 -> "Main Course";
            case 3 -> "Dessert";
            default -> {
                System.out.println("❌ Invalid category choice.");
                yield null;
            }
        };

        if (category == null) return null;

        // Filter and display matching food items
        List<FoodItem> filtered = foodManager.getAllFoods().stream()
                .filter(f -> f.getCuisineType().equalsIgnoreCase(cuisine) &&
                             f.getCategory().equalsIgnoreCase(category))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("\n⚠️ No food items found for this selection.");
            return null;
        }

        // Display table header
        System.out.println("\n--- Available Food Items ---");
        System.out.printf("%-5s %-20s %-12s %-12s %-12s%n",
                          "ID", "Name", "Price", "Discount", "Final Price");
        System.out.println("---------------------------------------------------------------");

        for (FoodItem food : filtered) {
            System.out.printf("%-5d %-20s ₹%-11.2f %-11.1f ₹%-11.2f%n",
                              food.getId(),
                              food.getName(),
                              food.getPrice(),
                              food.getDiscount(),
                              food.getDiscountedPrice());
        }

        System.out.print("\nEnter Food ID to add to cart: ");
        int id = sc.nextInt();

        for (FoodItem food : filtered) {
            if (food.getId() == id) {
                return food;
            }
        }

        System.out.println("❌ Invalid Food ID.");
        return null;
    }

    private static void addItemToCart() {
        // Step 1: Show full menu
        displayFoodItems();

        // Step 2: Ask user to enter food ID and quantity
        System.out.print("\nEnter the ID of the food item to add to cart: ");
        int foodId = sc.nextInt();

        FoodItem selectedFood = null;
        for (FoodItem item : foodManager.getAllFoods()) {
            if (item.getId() == foodId) {
                selectedFood = item;
                break;
            }
        }

        if (selectedFood == null) {
            System.out.println("❌ Invalid Food ID! Please try again.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        // Step 3: Check if item already exists in cart
        boolean itemExists = false;
        for (LineItem item : cart) {
            if (item.getFoodItem().getId() == selectedFood.getId()) {
                item.setQuantity(item.getQuantity() + qty);  // Increase quantity
                System.out.println("✅ Updated quantity of: " + selectedFood.getName() + " to " + item.getQuantity());
                itemExists = true;
                break;
            }
        }

        // Step 4: If not in cart, add new item
        if (!itemExists) {
            LineItem newItem = new LineItem(cart.size() + 1, qty, selectedFood);
            cart.add(newItem);
            System.out.println("🛒 Added to cart: " + selectedFood.getName() + " | Quantity: " + qty + " | Price: ₹" + selectedFood.getDiscountedPrice());
        }
    }


    private static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("🛒 Your cart is empty.");
            return;
        }

        System.out.println("\n---------------- Your Cart ----------------\n");
        System.out.printf("%-20s %-10s %-12s %-12s%n", "Food Item", "Quantity", "Unit Price", "Total");
        System.out.println("----------------------------------------------------------");

        double cartTotal = 0.0;
        for (LineItem item : cart) {
            String foodName = item.getFoodItem().getName();
            int qty = item.getQuantity();
            double unitPrice = item.getFoodItem().getDiscountedPrice(); // price after discount
            double total = unitPrice * qty;
            cartTotal += total;

            System.out.printf("%-20s %-10d ₹%-11.2f ₹%-11.2f%n", foodName, qty, unitPrice, total);
        }

        System.out.println("----------------------------------------------------------");
        System.out.printf("Total Cart Amount: ₹%.2f%n", cartTotal);
        System.out.println();
    }


    private static void removeItemFromCart() {
        System.out.print("Enter LineItem ID to remove: ");
        int id = sc.nextInt();
        boolean removed = cart.removeIf(item -> item.getId() == id);
        System.out.println(removed ? "Item removed from cart." : "Item not found.");
    }

    private static void placeOrder(Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Cannot place order.");
            return;
        }

        // Load delivery partners from file
        List<DeliveryPartner> deliveryPartners = DeliveryPartnerManager.loadPartners();
        if (deliveryPartners.isEmpty()) {
            System.out.println("No delivery partner available. Please contact admin.");
            return;
        }

        // Randomly assign a delivery partner
        DeliveryPartner assignedPartner = deliveryPartners.get(new Random().nextInt(deliveryPartners.size()));

        int orderId = new Random().nextInt(1000);
        Order order = new Order(orderId, new Date());

        for (LineItem item : cart) {
            order.addLineItem(item);
        }

        customer.addOrder(order);

        // Display cart items and prices
        System.out.println("\n--- Order Summary ---");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Order ID: " + order.getId());
        System.out.println("Date: " + order.getDate());

        System.out.println("\nItems:");
        double total = 0.0;
        for (LineItem item : order.getItems()) {
            double itemCost = item.calculateLineItemCost();
            System.out.println("- " + item.getFoodItem().getName() + " (Qty: " + item.getQuantity() + ") - ₹" + itemCost);
            total += itemCost;
        }

        // Apply discount if total > ₹500
        double discount = 0.0;
        if (total > 500) {
            discount = total * 0.10;
            total -= discount;
            System.out.printf("\n10%% Discount Applied: -₹%.2f\n", discount);
        }

        System.out.printf("Total Payable: ₹%.2f\n", total);

        // Select payment mode
        System.out.println("\nSelect Payment Mode: ");
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.print("Enter choice: ");
        int paymentChoice = sc.nextInt();
        sc.nextLine();

        String paymentMode = "";
        switch (paymentChoice) {
            case 1:
                paymentMode = "Cash";
                break;
            case 2:
                paymentMode = "UPI";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Cash.");
                paymentMode = "Cash";
        }

        // Final invoice
        System.out.println("\n======= INVOICE =======");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.println("Address: " + customer.getAddress());
        System.out.println("Order ID: " + order.getId());
        System.out.println("Date: " + order.getDate());
        System.out.println("Payment Mode: " + paymentMode);

        System.out.println("\nItems:");
        for (LineItem item : order.getItems()) {
            System.out.println("- " + item.getFoodItem().getName() + " (Qty: " + item.getQuantity() + ") - ₹" + item.calculateLineItemCost());
        }

        if (discount > 0) {
            System.out.printf("\nDiscount: -₹%.2f", discount);
        }

        System.out.printf("\nTotal Amount Paid: ₹%.2f", total);

        // Show assigned delivery partner
        System.out.println("\nDelivered By: " + assignedPartner.getName() + " | Contact: " + assignedPartner.getContactNumber());

        System.out.println("\nThank you for your order!");
        System.out.println("========================");

        cart.clear();
    }


    private static void searchFoodByName() {
        sc.nextLine();
        System.out.print("Enter food name to search: ");
        String name = sc.nextLine();

        List<FoodItem> results = foodManager.searchFoodByName(name);
        if (results.isEmpty()) {
            System.out.println("No food items found with name: " + name);
        } else {
            System.out.println("Matched Food Items:");
            for (FoodItem food : results) {
                System.out.println(food);
            }
        }
    }

    private static void addDeliveryPartner() {
    	sc.nextLine();
        System.out.print("Enter Delivery Partner Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();

        List<DeliveryPartner> existing = DeliveryPartnerManager.loadPartners();
        int newId = existing.size() + 1;

        DeliveryPartner partner = new DeliveryPartner(newId, name, contact);
        DeliveryPartnerManager.addPartner(partner);

        System.out.println("Delivery partner added successfully.");
    }


    private static void viewDeliveryPartners() {
        List<DeliveryPartner> deliveryPartners = DeliveryPartnerManager.loadPartners();

        if (deliveryPartners == null || deliveryPartners.isEmpty()) {
            System.out.println("No delivery partners available.");
            return;
        }

        System.out.println("\n--- Delivery Partners ---");
        for (DeliveryPartner dp : deliveryPartners) {
            System.out.println(dp);
        }
    }


    private static DeliveryPartner getRandomDeliveryPartner() {
        if (deliveryPartners.isEmpty()) return null;
        Random rand = new Random();
        return deliveryPartners.get(rand.nextInt(deliveryPartners.size()));
    }
}
