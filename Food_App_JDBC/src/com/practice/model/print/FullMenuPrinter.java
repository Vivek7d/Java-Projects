package com.practice.model.print;

import java.util.*;
import java.util.stream.Collectors;
import com.practice.model.FoodItem;

public class FullMenuPrinter {

    public static void printMenu(List<FoodItem> foodItems) {
        if (foodItems == null || foodItems.isEmpty()) {
            System.out.println("⚠️  No food items available.");
            return;
        }

        Set<String> cuisines = foodItems.stream()
                .map(FoodItem::getCuisineType)
                .collect(Collectors.toCollection(TreeSet::new)); // Sorted

        System.out.println("\n================== 🍽️ FULL MENU 🍽️ ==================\n");

        for (String cuisine : cuisines) {
            System.out.println("==========================================================");
            System.out.printf("🍴 CUISINE: %s%n", cuisine.toUpperCase());
            System.out.println("==========================================================");

            List<FoodItem> cuisineItems = foodItems.stream()
                    .filter(item -> item.getCuisineType().equalsIgnoreCase(cuisine))
                    .sorted(Comparator.comparing(FoodItem::getCategory).thenComparing(FoodItem::getName))
                    .collect(Collectors.toList());

            // Table border and header
            printTableBorder();
            System.out.printf("| %-4s | %-25s | %-12s | %-8s | %-8s | %-11s |%n",
                    "ID", "Name", "Category", "Price", "Discount", "Final Price");
            printTableBorder();

            for (FoodItem item : cuisineItems) {
                System.out.printf("| %-4d | %-25s | %-12s | ₹%-7.2f | %-8.1f | ₹%-10.2f |%n",
                        item.getId(),
                        item.getName(),
                        item.getCategory(),
                        item.getPrice(),
                        item.getDiscount(),
                        item.getDiscountedPrice());
            }

            printTableBorder();
            System.out.println(); // spacing after each cuisine
        }

        System.out.println("====================== ✅ END OF MENU ======================\n");
    }

    private static void printTableBorder() {
        System.out.println("+------+---------------------------+--------------+----------+----------+-------------+");
    }
}
