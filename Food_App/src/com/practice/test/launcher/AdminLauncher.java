package com.practice.test.launcher;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.practice.model.Customer;
import com.practice.model.CustomerManager;
import com.practice.model.FoodItem;
import com.practice.model.FoodManager;
import com.practice.model.delivery.DeliveryPartner;
import com.practice.model.delivery.DeliveryPartnerManager;
import com.practice.model.exception.DuplicatePartnerException;
import com.practice.model.exception.FoodNotFoundException;
import com.practice.model.exception.InvalidFoodInputException;
import com.practice.model.exception.InvalidInputException;
import com.practice.model.payment.PaymentManager;
import com.practice.model.payment.PaymentMethod;
import com.practice.model.print.FullMenuPrinter;

public class AdminLauncher {

	private static final Scanner sc = new Scanner(System.in);
	private static final FoodManager foodManager = new FoodManager();
	private List<FoodItem> foodList = new ArrayList<>();


	public static void launch() {
		while (true) {
			System.out.println("\n--- Admin Menu ---");
			System.out.println("1. Add Food Menu");
			System.out.println("2. Remove Food Item");
			System.out.println("3. Update Food Item");
			System.out.println("4. Display All Food Items");
			System.out.println("5. Manage Delivery Partners");
			System.out.println("6. Manage Payment Methods");
			System.out.println("7. Search Food by Name");
			System.out.println("8. View Registered Customers");
			System.out.println("9. Back to Main Menu");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				addFoodItem();
				break;
			case 2:
				removeFoodItem();
				break;
			case 3:
				updateFoodItem();
				break;
			case 4:
				displayFoodItems();
				break;
			case 5:
				manageDeliveryPartners();
				break;
			case 6:
				managePaymentMethods();
				break;
			case 7:
				searchFoodByName();
				break;
			case 8:
				displayRegisteredCustomers();
				break;
			case 9:
				return;
			default:
				System.out.println("❌ Invalid option. Please try again.");
				break;
			}
		}
		

	}
	private static void displayRegisteredCustomers() {
        CustomerManager manager = new CustomerManager();
        List<Customer> allCustomers = manager.getAllCustomers();

        if (allCustomers.isEmpty()) {
            System.out.println("🚫 No registered customers found.");
            return;
        }

        System.out.println("\n📋 Registered Customers:");
        for (Customer c : allCustomers) {
            System.out.println("ID: " + c.getId() + ", Name: " + c.getName() + ", Email: " + c.getEmail());
        }
    }

	private static void addFoodItem() {
		try {
			System.out.print("Enter Food ID: ");
			int id = sc.nextInt();
			sc.nextLine();

			if (foodManager.existsById(id)) {
				throw new InvalidFoodInputException("Food ID already exists. Please enter a unique ID.");
			}

			System.out.print("Enter Food Name: ");
			String name = sc.nextLine().trim();
			if (name.isEmpty()) {
				throw new InvalidFoodInputException("Food name cannot be empty.");
			}

			
			if (foodManager.existsByName(name)) {
				throw new InvalidFoodInputException("Food item with this name already exists.");
			}

			System.out.print("Enter Price: ");
			double price = sc.nextDouble();
			if (price <= 0) {
				throw new InvalidFoodInputException("Price must be greater than 0.");
			}

			System.out.print("Enter Discount Percent: ");
			double discount = sc.nextDouble();
			if (discount < 0 || discount > 100) {
				throw new InvalidFoodInputException("Discount must be between 0 and 100.");
			}

			sc.nextLine(); 

			System.out.print("Enter Cuisine Type(Indian,Italian,Chinese): ");
			String cuisineType = sc.nextLine().trim();
			if (cuisineType.isEmpty()) {
				throw new InvalidFoodInputException("Cuisine type cannot be empty.");
			}

			System.out.print("Enter Category (Starter, Main Course, Dessert): ");
			String category = sc.nextLine().trim();

			if (category.isEmpty()) {
				throw new InvalidFoodInputException("⚠️ Category cannot be empty.");
			}
			FoodItem food = new FoodItem(id, name, price, discount, cuisineType, category);
			foodManager.addFood(food);
			System.out.println("Food item added successfully.");
			System.out.println(food); 

		} catch (InvalidFoodInputException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (InputMismatchException e) {
			System.out.println("Invalid input type. Please enter numbers correctly.");
			sc.nextLine(); 
		}
	}
	private FoodItem findFoodItemById(int id) {
	    for (FoodItem item : foodList) {
	        if (item.getId() == id) {
	            return item;
	        }
	    }
	    return null;
	}

	private static void removeFoodItem() {
	    while (true) {
	        System.out.println("\n--- 🗑️ Food Item Removal ---");
	        System.out.println("1. Soft Delete");
	        System.out.println("2. Hard Delete");
	        System.out.println("3. Reactivate Item");
	        System.out.println("4. 🔙 Back");
	        System.out.print("Enter choice: ");

	        int ch = sc.nextInt();

	        switch (ch) {
	            case 1 -> {
	                List<FoodItem> foods = foodManager.getAllFoods();
	                if (foods.isEmpty()) {
	                    System.out.println("⚠️ No active food items to delete.");
	                    break;
	                }
	                displayFoodItems();
	                System.out.print("Enter Food ID to soft delete: ");
	                int id = sc.nextInt();
	                boolean removed = foodManager.removeFood(id);
	                System.out.println(removed
	                        ? "✅ Soft deleted and moved to inactive list."
	                        : "❌ Food item not found.");
	            }
	            case 2 -> {
	                if (!displayInactiveItems()) {
	                    break;
	                }
	                System.out.print("Enter Food ID to hard delete: ");
	                int id = sc.nextInt();
	                boolean deleted = foodManager.hardDeleteFood(id);
	                System.out.println(deleted
	                        ? "🗑️ Hard deleted successfully."
	                        : "❌ Food item not found in inactive list.");
	            }
	            case 3 -> {
	                if (!displayInactiveItems()) {
	                    break;
	                }
	                System.out.print("Enter Food ID to reactivate: ");
	                int id = sc.nextInt();
	                boolean reactivated = foodManager.reactivateFood(id);
	                System.out.println(reactivated
	                        ? "✅ Reactivated successfully."
	                        : "❌ Food item not found in inactive list.");
	            }
	            case 4 -> {
	                return;
	            }
	            default -> System.out.println("❌ Invalid option. Please choose from 1 to 4.");
	        }
	    }
	}

	private static boolean displayInactiveItems() {
	    List<FoodItem> inactiveList = foodManager.getAllInactiveFoods();

	    if (inactiveList.isEmpty()) {
	        System.out.println("⚠️ No inactive food items found.");
	        return false;
	    }

	    System.out.println("\n--- ❌ Inactive Food Items ---");
	    for (FoodItem item : inactiveList) {
	        System.out.println(item);
	    }
	    return true;
	}

	 private static void viewActiveItems() {
	        List<FoodItem> active = foodManager.getAllFoods().stream()
	            .filter(item -> !item.isDeleted())
	            .collect(Collectors.toList());
	        System.out.println("\n--- 🍽️ Active Food Items ---");
	        active.forEach(System.out::println);
	    }

	    private static void viewAllItems() {
	        System.out.println("\n--- 📋 All Food Items ---");
	        foodManager.getAllFoods().forEach(System.out::println);
	    }
	private static void updateFoodItem() {
		displayFoodItems();
		System.out.print("Enter Food ID to update: ");
		int id = sc.nextInt();
		sc.nextLine();

		System.out.print("Enter New Name: ");
		String name = sc.nextLine().trim();

		System.out.print("Enter New Price: ");
		double price = sc.nextDouble();

		System.out.print("Enter New Discount: ");
		double discount = sc.nextDouble();
		sc.nextLine();

		System.out.print("Enter New Cuisine Type: ");
		String cuisine = sc.nextLine().trim();

		System.out.print("Enter New Category: ");
		String category = sc.nextLine().trim();

		FoodItem updatedFood = new FoodItem(id, name, price, discount, cuisine, category);

		try {
			if (foodManager.updateFood(id, updatedFood)) {
				System.out.println("✅ Food item updated.");
			} else {
				throw new InvalidInputException("❌ Food item with ID " + id + " not found.");
			}
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void displayFoodItems() {
	    List<FoodItem> allItems = foodManager.getAllFoods();
	    List<FoodItem> activeItems = new ArrayList<>();

	    for (FoodItem item : allItems) {
	        if (!item.isDeleted()) {
	            activeItems.add(item);
	        }
	    }

	    if (activeItems.isEmpty()) {
	        System.out.println("⚠️ No active food items available.");
	    } else {
	        FullMenuPrinter.printMenu(activeItems);
	    }
	}

	private static void searchFoodByName() {
		sc.nextLine();
		System.out.print("Enter food name to search: ");
		String name = sc.nextLine();

		if (name.trim().isEmpty()) {
			System.out.println("Search term cannot be empty.");
			return;
		}

		try {
			List<FoodItem> results = foodManager.searchFoodByName(name);

			if (results == null || results.isEmpty()) {
				throw new FoodNotFoundException("No food items found with name: " + name);
			}

			System.out.println("\nMatched Food Items:\n");
			System.out.printf("%-5s %-20s %-12s %-12s %-12s %-12s %-12s%n", "ID", "Name", "Price", "Discount",
					"Final Price", "Cuisine", "Category");
			System.out.println(
					"------------------------------------------------------------------------------------------");

			for (FoodItem food : results) {
				System.out.printf("%-5d %-20s ₹%-11.2f %-11.1f ₹%-11.2f %-12s %-12s%n", food.getId(), food.getName(),
						food.getPrice(), food.getDiscount(), food.getDiscountedPrice(), food.getCuisineType(),
						food.getCategory());
			}
		} catch (FoodNotFoundException e) {
			System.out.println("❌ " + e.getMessage());
		} catch (Exception e) {
			System.out.println("⚠️ Unexpected error occurred: " + e.getMessage());
		}
	}

	private static void managePaymentMethods() {
		while (true) {
			try {
				System.out.println("\n--- Manage Payment Methods ---");
				System.out.println("1. Add Payment Method");
				System.out.println("2. View Payment Methods");
				System.out.println("3. Remove Payment Method");
				System.out.println("4. Back");
				System.out.print("Enter your choice: ");

				int choice = sc.nextInt();
				sc.nextLine(); // consume leftover newline

				switch (choice) {
				case 1:
					System.out.print("Enter Payment Method Name (e.g., Cash, UPI): ");
					String methodName = sc.nextLine().trim();

					if (methodName.isEmpty()) {
						System.out.println("Payment method name cannot be empty.");
						break;
					}

					if (!methodName.matches("^[a-zA-Z\\s]+$")) {
						System.out.println("Invalid name. Only letters and spaces are allowed.");
						break;
					}

					List<PaymentMethod> existing = PaymentManager.loadMethods();
					int id = existing.size() + 1;
					PaymentMethod newMethod = new PaymentMethod(id, methodName);
					PaymentManager.addMethod(newMethod);
					System.out.println("Payment method added successfully.");
					break;

				case 2:
					List<PaymentMethod> methods = PaymentManager.loadMethods();
					if (methods.isEmpty()) {
						System.out.println("No payment methods available.");
					} else {
						System.out.println("\nAvailable Methods:");
						for (PaymentMethod m : methods) {
							System.out.println(m);
						}
					}
					break;

				case 3:
					List<PaymentMethod> methodList = PaymentManager.loadMethods();
					if (methodList.isEmpty()) {
						System.out.println("No payment methods to remove.");
						break;
					}

					System.out.println("Select the ID of the payment method to remove:");
					for (PaymentMethod m : methodList) {
						System.out.println(m);
					}

					System.out.print("Enter ID to remove: ");
					int removeId = sc.nextInt();
					sc.nextLine();

					if (removeId <= 0) {
						System.out.println("Invalid ID. ID must be greater than 0.");
						break;
					}

					boolean removed = PaymentManager.removeMethodById(removeId);
					if (removed) {
						System.out.println("Payment method removed successfully.");
					} else {
						System.out.println("Invalid ID. No payment method removed.");
					}
					break;

				case 4:
					return;

				default:
					System.out.println("Invalid option. Please enter a number between 1 and 4.");
				}

			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a valid number.");
				sc.nextLine(); 
			} catch (Exception e) {
				System.out.println("An unexpected error occurred: " + e.getMessage());
				sc.nextLine(); 
			}
		}
	}

	private static void manageDeliveryPartners() {
		while (true) {
			System.out.println("\n--- Manage Delivery Partners ---");
			System.out.println("1. Add Delivery Partner");
			System.out.println("2. View Delivery Partners");
			System.out.println("3. Remove Delivery Partner");
			System.out.println("4. Back to Admin Menu");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				addDeliveryPartner();
				break;
			case 2:
				viewDeliveryPartners();
				break;
			case 3:
				removeDeliveryPartner();
				break;
			case 4:
				return;
			default:
				System.out.println("❌ Invalid option. Please try again.");
				break;
			}
		}
	}

	private static void addDeliveryPartner() {
		sc.nextLine();
		try {
			System.out.print("Enter Delivery Partner Name: ");
			String name = sc.nextLine().trim();

			System.out.print("Enter Contact Number: ");
			String contact = sc.nextLine().trim();

			if (name.isEmpty() || contact.isEmpty()) {
				throw new IllegalArgumentException("Name or contact number cannot be empty.");
			}

			List<DeliveryPartner> existing = DeliveryPartnerManager.loadPartners();

			// Check for duplicate name (case-insensitive)
			for (DeliveryPartner dp : existing) {
				if (dp.getName().equalsIgnoreCase(name)) {
					throw new DuplicatePartnerException("Partner with name '" + name + "' already exists.");
				}
			}

			int newId = existing.size() + 1;
			DeliveryPartner partner = new DeliveryPartner(newId, name, contact);
			DeliveryPartnerManager.addPartner(partner);

			System.out.println("✅ Delivery partner added successfully.");

		} catch (DuplicatePartnerException e) {
			System.out.println("❌ Error: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("❌ Invalid input: " + e.getMessage());
		}
	}

	private static void removeDeliveryPartner() {
		List<DeliveryPartner> partners = DeliveryPartnerManager.loadPartners();

		if (partners.isEmpty()) {
			System.out.println("⚠️ No delivery partners found.");
			return;
		}

	
		System.out.println("📦 Existing Delivery Partners:");
		for (DeliveryPartner dp : partners) {
			System.out
					.println("ID: " + dp.getId() + " | Name: " + dp.getName() + " | Contact: " + dp.getContactNumber());
		}

	
		System.out.print("\nEnter ID or Name of the partner to remove: ");
		sc.nextLine();
		String input = sc.nextLine().trim();

		boolean removed = false;

		
		try {
			int id = Integer.parseInt(input);
			removed = DeliveryPartnerManager.removePartnerById(id);
		} catch (NumberFormatException e) {
			
			removed = DeliveryPartnerManager.removePartnerByName(input);
		}

		if (removed) {
			System.out.println("✅ Delivery Partner removed successfully.");
		} else {
			System.out.println("❌ No matching delivery partner found.");
		}
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

}
