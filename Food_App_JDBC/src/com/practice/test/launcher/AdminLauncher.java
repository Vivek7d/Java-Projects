package com.practice.test.launcher;

import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.practice.model.FoodItem;
import com.practice.model.FoodManager;
import com.practice.model.Customer;
import com.practice.model.Order;
import com.practice.model.jdbc.OrderDao;

import com.practice.model.payment.PaymentMethod;
import com.practice.model.payment.PaymentMethodManager;
import com.practice.model.CustomerManager;
import com.practice.model.delivery.DeliveryPartner;
import com.practice.model.delivery.DeliveryPartnerManager;

import com.practice.model.exception.InvalidInputException;

public class AdminLauncher {

	private static final Scanner sc = new Scanner(System.in);
	private static final FoodManager foodManager = new FoodManager();
	private static final CustomerManager customerManager = new CustomerManager();
	private static final DeliveryPartnerManager partnerManager = new DeliveryPartnerManager();
	private static final OrderDao orderDao = new OrderDao();
	private static final PaymentMethodManager paymentMethodManager = new PaymentMethodManager();

	public static void launch() {
		while (true) {
			System.out.println("\n--- Admin Menu ---");
			System.out.println("1. Manage Food Items"); 
			System.out.println("2. Search by Name or Cuisine");
			System.out.println("3. Manage Item Deletions");
			System.out.println("4. View Registered Customers");
			System.out.println("5. Manage Delivery Partners");
			System.out.println("6. Manage Payment Methods");
			System.out.println("7. View Revenue and All Orders");
			System.out.println("8. Back to Main Menu"); 
			System.out.print("Enter your choice: ");

			int choice;
			try {
				choice = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("❌ Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}
			sc.nextLine();

			switch (choice) {
			case 1:
				manageFoodItems(); 
				break;
			case 2:
				searchItems();
				break;
			case 3:
				manageDeletions();
				break;
			case 4:
				displayRegisteredCustomers();
				break;
			case 5:
				manageDeliveryPartners();
				break;
			case 6:
				managePaymentMethods();
				break;
			case 7:
                viewRevenueAndOrders(); 
                break;
            case 8:
                System.out.println("Returning to main menu...");
                return;
			default:
				System.out.println("❌ Invalid option. Please try again.");
				break;
			}
		}
	}
	private static void manageFoodItems() {
	    while (true) {
	        System.out.println("\n--- Manage Food Items ---");
	        System.out.println("1. Add New Food Item");
	        System.out.println("2. View Full Menu");
	        System.out.println("3. Update a Food Item");
	        System.out.println("4. Back to Admin Menu");
	        System.out.print("Enter your choice: ");

	        int choice;
	        try {
	            choice = Integer.parseInt(sc.nextLine());
	        } catch (NumberFormatException e) {
	            System.out.println("❌ Invalid input. Please enter a number.");
	            continue;
	        }

	        switch (choice) {
	            case 1:
	                addFoodItem(); 
	                break;
	            case 2:
	                displayFoodItems(); 
	                break;
	            case 3:
	                updateFoodItem(); 
	                break;
	            case 4:
	                return; // Exits this sub-menu and returns to the main menu
	            default:
	                System.out.println("❌ Invalid option. Please choose from 1 to 4.");
	        }
	    }
	}

	private static void managePaymentMethods() {
	    while (true) {
	        System.out.println("\n--- Manage Payment Methods ---");
	        System.out.println("1. Add Payment Method");
	        System.out.println("2. View All Payment Methods");
	        System.out.println("3. Manage Method Status (Deactivate/Reactivate)");
	        System.out.println("4. Back to Admin Menu");
	        System.out.print("Enter your choice: ");

	        int choice;
	        try {
	            choice = sc.nextInt();
	        } catch (InputMismatchException e) {
	            System.out.println("❌ Invalid input. Please enter a number.");
	            sc.nextLine();
	            continue;
	        }
	        sc.nextLine();

	        switch (choice) {
	            case 1:
	                addPaymentMethod();
	                break;
	            case 2:
	                viewAllPaymentMethods();
	                break;
	            case 3:
	                manageMethodStatus();
	                break;
	            case 4:
	                return;
	            default:
	                System.out.println("❌ Invalid option. Please choose from 1 to 4.");
	        }
	    }
	}

	private static void addPaymentMethod() {
	    String name;
	    while (true) {
	        System.out.print("Enter Payment Method Name (e.g., Cash, UPI): ");
	        name = sc.nextLine().trim();
	        if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
	            System.out.println("Error: Name must contain only letters and spaces.");
	        } else if (paymentMethodManager.existsByName(name)) {
	            System.out.println("Error: A payment method with this name already exists.");
	        } else {
	            break;
	        }
	    }

	    PaymentMethod newMethod = new PaymentMethod(name);
	    if (paymentMethodManager.addMethod(newMethod) != null) {
	        System.out.println("✅ Payment method added successfully!");
	    } else {
	        System.out.println("❌ Failed to add payment method. Try again.");
	    }
	}

	private static void viewAllPaymentMethods() {
	    List<PaymentMethod> methods = paymentMethodManager.getAllMethods();
	    if (methods.isEmpty()) {
	        System.out.println("\nNo payment methods found.");
	    } else {
	        System.out.println("\n--- All Payment Methods ---");
	        System.out.printf("%-10s | %-25s | %-10s%n", "ID", "Name", "Status");
	        System.out.println("----------------------------------------------");
	        for (PaymentMethod p : methods) {
	            System.out.printf("%-10d | %-25s | %-10s%n", p.getId(), p.getMethodName(),
	                    p.isDeleted() ? "Inactive" : "Active");
	        }
	        System.out.println("----------------------------------------------");
	    }
	}

	private static void manageMethodStatus() {
	    while (true) {
	        System.out.println("\n--- Method Status Management ---");
	        System.out.println("1. Deactivate Method (Soft Delete)");
	        System.out.println("2. Reactivate Method");
	        System.out.println("3. Permanently Delete Method");
	        System.out.println("4. Back to Payment Menu");
	        System.out.print("Enter your choice: ");
	        
	        int actionChoice;
	        try {
	            actionChoice = Integer.parseInt(sc.nextLine());
	        } catch (NumberFormatException e) {
	            System.out.println("❌ Invalid input. Please enter a number.");
	            continue;
	        }

	        if (actionChoice == 4) return;

	        viewAllPaymentMethods(); 

	        System.out.print("Enter the ID of the method to manage: ");
	        int idToManage;
	        try {
	            idToManage = Integer.parseInt(sc.nextLine());
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid ID. Please enter a number.");
	            continue;
	        }

	        switch (actionChoice) {
	            case 1:
	                if (paymentMethodManager.softDeleteMethod(idToManage)) {
	                    System.out.println("Method deactivated successfully.");
	                } else {
	                    System.out.println(" Failed to deactivate method. Check the ID.");
	                }
	                break;
	            case 2:
	                if (paymentMethodManager.reactivateMethod(idToManage)) {
	                    System.out.println("Method reactivated successfully.");
	                } else {
	                    System.out.println("Failed to reactivate method. Check the ID.");
	                }
	                break;
	            case 3:
	                System.out.println("This is irreversible. Are you sure?");
	                System.out.print("Type 'DELETE' to confirm: ");
	                if ("DELETE".equals(sc.nextLine().trim())) {
	                    if (paymentMethodManager.permanentlyDeleteMethod(idToManage)) {
	                        System.out.println("Method permanently deleted.");
	                    } else {
	                        System.out.println(" Failed to permanently delete.");
	                    }
	                } else {
	                    System.out.println("Deletion cancelled.");
	                }
	                break;
	            default:
	                System.out.println("❌ Invalid option. Please choose from 1 to 4.");
	                break;
	        }
	    }
	}
	   
	 private static void viewRevenueAndOrders() {
	        try {
	            List<Order> allOrders = orderDao.getAllOrders();

	            if (allOrders.isEmpty()) {
	                System.out.println("\nNo orders have been placed yet. The revenue is ₹0.00");
	                return;
	            }

	            System.out.println("\n------------------------------------- 📈 All Orders & Revenue Report 📈 --------------------------------------");
	            System.out.printf("%-8s | %-20s | %-15s | %-15s | %-10s | %-10s | %-10s%n", 
	                              "Order ID", "Customer Name", "Order Date", "Partner", "Payment", "Discount", "Total Paid");
	            System.out.println("---------------------------------------------------------------------------------------------------------");

	            double totalRevenue = 0.0;
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	            for (Order order : allOrders) {
	                String partnerName = (order.getDeliveryPartner() != null) ? order.getDeliveryPartner().getName() : "N/A";
	                String customerName = (order.getCustomer() != null) ? order.getCustomer().getName() : "N/A";
	                String orderDate = sdf.format(order.getDate());
	                
	                System.out.printf("%-8d | %-20s | %-15s | %-15s | %-10s | ₹%-9.2f | ₹%-9.2f%n",
	                                  order.getId(),
	                                  customerName,
	                                  orderDate,
	                                  partnerName,
	                                  order.getPaymentMode(),
	                                  order.getDiscount(),
	                                  order.getTotalPaid());
	                
	                totalRevenue += order.getTotalPaid();
	            }

	            System.out.println("---------------------------------------------------------------------------------------------------------");
	            System.out.printf("Total Revenue from %d orders: ₹%.2f%n", allOrders.size(), totalRevenue);
	            System.out.println("---------------------------------------------------------------------------------------------------------");

	        } catch (Exception e) {
	            System.err.println("An error occurred while fetching the revenue report: " + e.getMessage());
	            e.printStackTrace(); 
	        }
	    }

	   
	
	private static void manageDeliveryPartners() {
		while (true) {
			System.out.println("\n--- Manage Delivery Partners ---");
			System.out.println("1. Add Delivery Partner");
			System.out.println("2. View All Delivery Partners");
			System.out.println("3. Manage Partner Status (Delete/Reactivate)");
			System.out.println("4. Back to Admin Menu");
			System.out.print("Enter your choice: ");

			int choice;
			try {
				choice = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("❌ Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}
			sc.nextLine();

			switch (choice) {
			case 1:
				addPartner(); 
				break;
			case 2:
				viewAllPartners(); 
				break;
			case 3:
				managePartnerStatus(); 
				break;
			case 4:
				return; 
			default:
				System.out.println("❌ Invalid option. Please choose from 1 to 4.");
			}
		}
	}

	private static void managePartnerStatus() {
		while (true) {
			System.out.println("\n--- Partner Status Management ---");
			System.out.println("1. Deactivate Partner (Soft Delete)");
			System.out.println("2. Reactivate Partner");
			System.out.println("3. Permanently Delete Partner");
			System.out.println("4. Back to Partner Menu");
			System.out.print("Enter your choice: ");

			int actionChoice;
			try {
				actionChoice = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("❌ Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}
			sc.nextLine();

			if (actionChoice == 4)
				return;

			
			viewAllPartners();

			System.out.print("Enter the ID of the partner to manage: ");
			int idToManage;
			try {
				idToManage = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid ID. Please enter a number.");
				sc.nextLine();
				continue;
			}
			sc.nextLine();

			switch (actionChoice) {
			case 1:
				if (partnerManager.softDeletePartner(idToManage)) {
					System.out.println("Partner deactivated successfully.");
				} else {
					System.out.println(" Failed to deactivate partner. Check the ID.");
				}
				break;
			case 2:
				if (partnerManager.reactivatePartner(idToManage)) {
					System.out.println("Partner reactivated successfully.");
				} else {
					System.out.println(" Failed to reactivate partner. Check the ID.");
				}
				break;
			case 3:
				System.out.println(
						"This action is irreversible. Are you sure you want to permanently delete this partner?");
				System.out.print("Type 'DELETE' to confirm: ");
				String confirmation = sc.nextLine().trim();
				if (confirmation.equals("DELETE")) {
					if (partnerManager.permanentlyDeletePartner(idToManage)) {
						System.out.println("Partner permanently deleted.");
					} else {
						System.out.println("Failed to permanently delete partner.");
					}
				} else {
					System.out.println("Deletion cancelled.");
				}
				break;
			default:
				System.out.println("Invalid option. Please choose from 1 to 4.");
			}
		}
	}

	private static void viewAllPartners() {
		List<DeliveryPartner> partners = partnerManager.getAllPartners();
		if (partners.isEmpty()) {
			System.out.println(" No delivery partners found.");
		} else {
			System.out.println("\n---  All Delivery Partners ---");
			System.out.printf("%-10s | %-25s | %-15s | %-10s%n", "ID", "Name", "Contact", "Status");
			System.out.println("------------------------------------------------------------------");
			for (DeliveryPartner p : partners) {
				System.out.printf("%-10d | %-25s | %-15s | %-10s%n", p.getId(), p.getName(), p.getContactNumber(),
						p.isDeleted() ? "Inactive" : "Active");
			}
			System.out.println("------------------------------------------------------------------");
		}
	}
	private static void addPartner() {
	    String name = "";
	    String contact = "";

	    // Loop for Name
	    while (true) {
	        try {
	            System.out.print("Enter Delivery Partner Name: ");
	            name = sc.nextLine().trim();

	            if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
	                throw new InvalidInputException("Name must contain only letters and spaces.");
	            }

	            if (partnerManager.existsByName(name)) {
	                throw new InvalidInputException("A partner with this name already exists.");
	            }

	            break; // valid name entered, break out of loop

	        } catch (InvalidInputException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }

	    
	    while (true) {
	        try {
	            System.out.print("Enter Contact Number: ");
	            contact = sc.nextLine().trim();

	            if (contact.isEmpty() || !contact.matches("\\d+")) {
	                throw new InvalidInputException("Contact number must contain digits only.");
	            }

	            break; 

	        } catch (InvalidInputException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }


	    DeliveryPartner newPartner = new DeliveryPartner(name, contact);
	    if (partnerManager.addPartner(newPartner) != null) {
	        System.out.println("Delivery partner added successfully!");
	    } else {
	        System.out.println("Failed to add delivery partner. Try again.");
	    }
	}


	private static void searchItems() {
	    String searchTerm;

	    while (true) {
	        System.out.print("Enter food name or cuisine to search: ");
	        searchTerm = sc.nextLine().trim();

	        if (searchTerm.isEmpty()) {
	            System.out.println("Search term cannot be empty.");
	            continue;
	        }

	        if (searchTerm.matches(".*\\d.*")) {
	            System.out.println("Invalid input. Please enter a valid food name or cuisine.");
	            continue;
	        }

	        break;
	    }

	    List<FoodItem> results = foodManager.searchItems(searchTerm);

	    if (results.isEmpty()) {
	        System.out.println("No food items found matching '" + searchTerm + "'.");
	    } else {
	        System.out.println("\n--- Search Results for '" + searchTerm + "' ---");
	        printMenuTable(results);
	    }
	}


	private static void displayRegisteredCustomers() {
		List<Customer> customers = customerManager.getAllCustomers();

		if (customers.isEmpty()) {
			System.out.println("\n No customers are registered in the system yet.");
			return;
		}

		System.out.println("\n----------------- 👥 Registered Customers 👥 -----------------");

		System.out.printf("%-10s | %-20s | %-30s | %-15s%n", "Cust ID", "Name", "Email", "Phone Number");
		System.out.println("---------------------------------------------------------------------");

		for (Customer c : customers) {
			System.out.printf("%-10d | %-20s | %-30s | %-15s%n", c.getId(), c.getName(), c.getEmail(),
					c.getPhoneNumber());
		}
		System.out.println("---------------------------------------------------------------------");
	}

	private static void manageDeletions() {
	    while (true) {
	        System.out.println("\n---  Deletion Management ---");
	        System.out.println("1. Deactivate Item (Soft Delete)");
	        System.out.println("2. Permanently Delete Item (Hard Delete)");
	        System.out.println("3. Reactivate Item");
	        System.out.println("4. Back to Admin Menu");
	        System.out.print("Enter your choice: ");

	        int choice;
	        try {
	            choice = sc.nextInt();
	            sc.nextLine(); // consume newline
	        } catch (InputMismatchException e) {
	            System.out.println(" Invalid input. Please enter a number.");
	            sc.nextLine(); // clear buffer
	            continue;
	        }

	        switch (choice) {
	            case 1:
	                displayFoodItems();
	                int idToSoftDelete;
	                while (true) {
	                    System.out.print("Enter the ID of the item to deactivate: ");
	                    try {
	                        idToSoftDelete = sc.nextInt();
	                        sc.nextLine();
	                        break;
	                    } catch (InputMismatchException e) {
	                        System.out.println("Invalid ID. Please enter a valid number.");
	                        sc.nextLine();
	                    }
	                }

	                if (foodManager.softDeleteFood(idToSoftDelete)) {
	                    System.out.println("Item deactivated successfully.");
	                } else {
	                    System.out.println("Failed to deactivate item. It may not exist.");
	                }
	                break;

	            case 2:
	                if (displayInactiveItems()) {
	                    int idToHardDelete;
	                    while (true) {
	                        System.out.print("Enter the ID of the inactive item to PERMANENTLY delete: ");
	                        try {
	                            idToHardDelete = sc.nextInt();
	                            sc.nextLine();
	                            break;
	                        } catch (InputMismatchException e) {
	                            System.out.println("Invalid ID. Please enter a valid number.");
	                            sc.nextLine();
	                        }
	                    }

	                    if (foodManager.hardDeleteFood(idToHardDelete)) {
	                        System.out.println("Item permanently deleted.");
	                    } else {
	                        System.out.println("Failed to delete item. Make sure it's an inactive item ID.");
	                    }
	                }
	                break;

	            case 3:
	                if (displayInactiveItems()) {
	                    int idToReactivate;
	                    while (true) {
	                        System.out.print("Enter the ID of the item to reactivate: ");
	                        try {
	                            idToReactivate = sc.nextInt();
	                            sc.nextLine();
	                            break;
	                        } catch (InputMismatchException e) {
	                            System.out.println("Invalid ID. Please enter a valid number.");
	                            sc.nextLine();
	                        }
	                    }

	                    if (foodManager.reactivateFood(idToReactivate)) {
	                        System.out.println("Item reactivated successfully.");
	                    } else {
	                        System.out.println("Failed to reactivate item. Make sure it's an inactive item ID.");
	                    }
	                }
	                break;

	            case 4:
	                return;

	            default:
	                System.out.println(" Invalid option. Please choose from 1 to 4.");
	        }
	    }
	}

	private static boolean displayInactiveItems() {
		List<FoodItem> inactiveMenu = foodManager.getAllInactiveFoods();
		if (inactiveMenu.isEmpty()) {
			System.out.println("\nNo inactive items found.");
			return false;
		}
		System.out.println("\n--------------------------- ❌ Inactive Items ❌ ---------------------------");
		printMenuTable(inactiveMenu);
		return true;
	}

	private static void updateFoodItem() {
	    displayFoodItems();

	    int id;
	    FoodItem existingFood;

	    while (true) {
	        System.out.print("\nEnter the ID of the food item to update: ");
	        try {
	            id = sc.nextInt();
	            sc.nextLine();

	            existingFood = foodManager.getFoodById(id);
	            if (existingFood == null) {
	                System.out.println("Food item with ID " + id + " not found. Please try again.");
	            } else {
	                break;
	            }
	        } catch (InputMismatchException e) {
	            System.out.println("Invalid ID format. Please enter a number.");
	            sc.nextLine();
	        }
	    }

	    try {
	        String name;
	        while (true) {
	            System.out.print("Enter New Name (current: " + existingFood.getName() + "): ");
	            name = sc.nextLine().trim();
	            if (name.isEmpty()) {
	                System.out.println("Name cannot be empty.");
	            } else if (!name.matches("^[A-Za-z ]+$")) {
	                System.out.println("Name must contain only letters and spaces.");
	            } else {
	                break;
	            }
	        }

	        double price;
	        while (true) {
	            System.out.print("Enter New Price (current: " + existingFood.getPrice() + "): ");
	            try {
	                price = sc.nextDouble();
	                sc.nextLine();
	                if (price <= 0) {
	                    System.out.println("Price must be greater than 0.");
	                } else {
	                    break;
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input. Please enter a valid number for price.");
	                sc.nextLine();
	            }
	        }

	        double discount;
	        while (true) {
	            System.out.print("Enter New Discount (current: " + existingFood.getDiscount() + "): ");
	            try {
	                discount = sc.nextDouble();
	                sc.nextLine();
	                if (discount < 0 || discount > 100) {
	                    System.out.println("Discount must be between 0 and 100.");
	                } else {
	                    break;
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input. Please enter a valid number for discount.");
	                sc.nextLine();
	            }
	        }

	        String cuisineType;
	        while (true) {
	            System.out.print("Enter New Cuisine Type (current: " + existingFood.getCuisineType() + "): ");
	            cuisineType = sc.nextLine().trim();
	            if (cuisineType.isEmpty()) {
	                System.out.println("Cuisine type cannot be empty.");
	            } else if (!cuisineType.matches("^[A-Za-z ]+$")) {
	                System.out.println("Cuisine type must contain only letters and spaces.");
	            } else {
	                break;
	            }
	        }

	        String category;
	        while (true) {
	            System.out.print("Enter New Category (current: " + existingFood.getCategory() + "): ");
	            category = sc.nextLine().trim();
	            if (category.isEmpty()) {
	                System.out.println("Category cannot be empty.");
	            } else if (!category.matches("^[A-Za-z ]+$")) {
	                System.out.println("Category must contain only letters and spaces.");
	            } else {
	                break;
	            }
	        }

	        FoodItem updatedFood = new FoodItem(name, price, discount, cuisineType, category);
	        updatedFood.setId(id);

	        if (foodManager.updateFood(updatedFood)) {
	            System.out.println("Food item updated successfully!");
	        } else {
	            System.out.println("Failed to update food item.");
	        }

	    } catch (Exception e) {
	        System.out.println("An error occurred: " + e.getMessage());
	        sc.nextLine();
	    }
	}



	private static void displayFoodItems() {
		List<FoodItem> menu = foodManager.getAllFoods();
		if (menu.isEmpty()) {
			System.out.println("\nThe menu is currently empty. Please add some food items.");
			return;
		}
		System.out.println("\n----------------------------- 🍽️  Current Menu 🍽️ -----------------------------");
		printMenuTable(menu);
	}

	private static void printMenuTable(List<FoodItem> itemList) {

		System.out.printf("%-5s | %-20s | %-10s | %-12s | %-15s | %-15s%n", "ID", "Name", "Price", "Discount",
				"Cuisine", "Category");
		System.out.println("-----------------------------------------------------------------------------------");

		for (FoodItem item : itemList) {
			System.out.printf("%-5d | %-20s | ₹%-8.2f | %-12.1f%% | %-15s | %-15s%n", item.getId(), item.getName(),
					item.getPrice(), item.getDiscount(), item.getCuisineType(), item.getCategory());
		}
		System.out.println("-----------------------------------------------------------------------------------");
	}

	private static void addFoodItem() {
	    try {
	        String name;
	        while (true) {
	            System.out.print("Enter Food Name: ");
	            name = sc.nextLine().trim();
	            if (name.isEmpty()) {
	                System.out.println("Food name cannot be empty.");
	            } else if (!name.matches("^[A-Za-z ]+$")) {
	                System.out.println("Food name must contain only letters and spaces.");
	            } else if (foodManager.existsByName(name)) {
	                System.out.println("An active food item with this name already exists.");
	            } else {
	                break;
	            }
	        }

	        double price;
	        while (true) {
	            System.out.print("Enter Price: ");
	            try {
	                price = sc.nextDouble();
	                if (price <= 0) {
	                    System.out.println("Price must be greater than 0.");
	                } else {
	                    break;
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input. Please enter a valid number for price.");
	            }
	            sc.nextLine(); 
	        }

	        double discount;
	        while (true) {
	            System.out.print("Enter Discount Percent: ");
	            try {
	                discount = sc.nextDouble();
	                if (discount < 0 || discount > 100) {
	                    System.out.println("Discount must be between 0 and 100.");
	                } else {
	                    break;
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input. Please enter a valid number for discount.");
	            }
	            sc.nextLine(); 
	        }
	        sc.nextLine(); 

	        String cuisineType;
	        while (true) {
	            System.out.print("Enter Cuisine Type (e.g., Indian, Italian): ");
	            cuisineType = sc.nextLine().trim();
	            if (cuisineType.isEmpty()) {
	                System.out.println("Cuisine type cannot be empty.");
	            } else if (!cuisineType.matches("^[A-Za-z ]+$")) {
	                System.out.println("Cuisine type must contain only letters and spaces.");
	            } else {
	                break;
	            }
	        }

	        String category;
	        while (true) {
	            System.out.print("Enter Category (e.g., Starter, Main Course): ");
	            category = sc.nextLine().trim();
	            if (category.isEmpty()) {
	                System.out.println("Category cannot be empty.");
	            } else if (!category.matches("^[A-Za-z ]+$")) {
	                System.out.println("Category must contain only letters and spaces.");
	            } else {
	                break;
	            }
	        }

	        
	        FoodItem newFood = new FoodItem(name, price, discount, cuisineType, category);
	        FoodItem addedFoodWithId = foodManager.addFood(newFood);
	        if (addedFoodWithId != null) {
	            System.out.println("\nFood item added successfully to the database.");
	            System.out.println(addedFoodWithId);
	        } else {
	            System.out.println("Failed to add food item to the database due to a system error.");
	        }

	    } catch (Exception e) {
	        System.out.println("Unexpected error: " + e.getMessage());
	        sc.nextLine();
	    }
	}

}