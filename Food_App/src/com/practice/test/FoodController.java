package com.practice.test;

import java.util.InputMismatchException;
import java.util.Scanner;
import com.practice.test.launcher.AdminLauncher;
import com.practice.test.launcher.CustomerLauncher;

public class FoodController {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        runApplication();
    }

    public static void runApplication() {
        while (true) {
            try {
                System.out.println("\n1. Admin");
                System.out.println("2. Customer");
                System.out.println("3. Exit");
                System.out.print("Choose role: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        AdminLauncher.launch();
                        break;
                    case 2:
                        CustomerLauncher.launch();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("❌ Invalid option. Please choose 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                sc.nextLine(); // Clear the invalid input from the scanner buffer
            }
        }
    }
}
