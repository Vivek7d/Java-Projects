package com.practice.test.launcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.practice.model.Customer;
import com.practice.model.CustomerManager;
import com.practice.model.FoodItem;
import com.practice.model.FoodManager;
import com.practice.model.LineItem;
import com.practice.model.Order;
import com.practice.model.delivery.DeliveryPartner;
import com.practice.model.delivery.DeliveryPartnerManager;
import com.practice.model.exception.EmptyCartException;
import com.practice.model.exception.InvalidCustomerInputException;
import com.practice.model.exception.InvalidInputException;
import com.practice.model.exception.NegativeValueException;
import com.practice.model.exception.PaymentException;
import com.practice.model.jdbc.OrderDao;
import com.practice.model.payment.PaymentProcessor;
import com.practice.model.print.CartPrinter;
import com.practice.model.print.FullMenuPrinter;
import com.practice.model.print.InvoiceGenerator;

public class CustomerLauncher {

	private static Scanner sc = new Scanner(System.in);
	private static FoodManager foodManager = new FoodManager();
	private static List<LineItem> cart = new ArrayList<>();
	private static CustomerManager customerManager = new CustomerManager();
	private static final DeliveryPartnerManager partnerManager = new DeliveryPartnerManager();
	private static OrderDao orderDao = new OrderDao(); 
	private static int orderCounter = 1;

	public static void launch() {
		while (true) {
			System.out.println("\n--- Welcome to the Food Ordering App ---");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Back");
			System.out.print("Enter your choice: ");

			int option;
			try {
				option = sc.nextInt();
			} catch (java.util.InputMismatchException e) {
				System.out.println(" Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}
			sc.nextLine();

			switch (option) {
			case 1:

				System.out.print("Enter your email to login: ");
				String loginEmail = sc.nextLine().trim();

				Customer customer = customerManager.getCustomerByEmail(loginEmail);
				if (customer != null) {
					System.out.println("Welcome back, " + customer.getName() + "!");
					customerMenu(customer);
				} else {
					System.out.println(" Customer with this email not found. Please register first.");
				}
				break;

			case 2:

				try {
					Customer newCustomer = createCustomer();
					Customer registeredCustomer = customerManager.registerCustomer(newCustomer);
					if (registeredCustomer != null) {
						System.out.println("Registration successful. Welcome, " + registeredCustomer.getName() + "!");
						System.out.println("Your new Customer ID is: " + registeredCustomer.getId());
						customerMenu(registeredCustomer);
					} else {
						System.out.println(" Registration failed. The email might already be in use.");
					}
				} catch (InvalidCustomerInputException e) {
					System.out.println("❌ " + e.getMessage());
				}
				break;

			case 3:
				return;

			default:
				System.out.println("Invalid option. Please choose 1, 2, or 3.");
			}
		}
	}

	private static void customerMenu(Customer customer) {
		cart.clear();
		System.out.println("\n--- Logged in as: " + customer.getName() + " ---");
		while (true) {
			System.out.println("\n--- Customer Menu ---");
			System.out.println("1. View Food Menu");
			System.out.println("2. Add Food to Cart");
			System.out.println("3. View Cart");
			System.out.println("4. Manage Cart");
			System.out.println("5. Place Order & Get Bill");
			System.out.println("6. Search by Name or Cuisine");
			System.out.println("7. View Order History");
			System.out.println("8. Logout");

			System.out.print("Enter your choice: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				displayFoodItems();
				break;
			case 2:
				addItemToCart();
				break;
			case 3:
				viewCart();
				break;
			case 4:
				manageCart();
				break;
			case 5:
				placeOrder(customer);
				break;
			case 6:
				searchItems();
				break;
			case 7:
				viewOrderHistory(customer);
				break;
			case 8:
				return;
			default:
				System.out.println("❌ Invalid option.");
				break;
			}
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
	            System.out.println("Invalid input. Please enter a valid food name or cuisine .");
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

	 public static void viewOrderHistory(Customer customer) {
	        try {
	            List<Order> history = orderDao.getOrdersByCustomerId(customer.getId());

	            if (history.isEmpty()) {
	                System.out.println("No order history found.");
	                return;
	            }

	            System.out.println("\n--- Your Order History ---");
	            for (Order order : history) {
	                InvoiceGenerator.printInvoice(order, customer);
	                System.out.println(); 
	            }
	        } catch (SQLException e) {
	            System.err.println("Error fetching order history: " + e.getMessage());
	        }
	    }

	private static void displayFoodItems() {
		List<FoodItem> foodItems = foodManager.getAllFoods();
		FullMenuPrinter.printMenu(foodItems);
	}

	private static void manageCart() {
		try {
			if (cart.isEmpty()) {
				throw new EmptyCartException("Your cart is empty.");
			}

			while (true) {
				System.out.println("\n --- Manage Cart ---");
				viewCart();

				System.out.println("1. Remove item from cart");
				System.out.println("2. Update quantity of item");
				System.out.println("3. Go back");
				System.out.print("Enter your choice: ");

				int choice = sc.nextInt();

				switch (choice) {
				case 1:
					System.out.print("Enter food item ID to remove: ");
					int removeId = sc.nextInt();
					boolean removed = false;

					Iterator<LineItem> iterator = cart.iterator();
					while (iterator.hasNext()) {
						LineItem item = iterator.next();
						if (item.getFoodItem().getId() == removeId) {
							iterator.remove();
							removed = true;
							System.out.println("Item removed from cart.");
							break;
						}
					}
					if (!removed) {
						throw new InvalidInputException("Item with ID " + removeId + " not found in cart.");
					}
					break;

				case 2:
					System.out.print("Enter food item ID to update quantity: ");
					int updateId = sc.nextInt();
					boolean updated = false;

					for (LineItem item : cart) {
						if (item.getFoodItem().getId() == updateId) {
							System.out.print("Enter new quantity: ");
							int newQty = sc.nextInt();
							if (newQty <= 0) {
								throw new NegativeValueException(" Quantity must be greater than 0.");
							}
							item.setQuantity(newQty);
							System.out.println("Quantity updated.");
							updated = true;
							break;
						}
					}
					if (!updated) {
						throw new InvalidInputException(" Item with ID " + updateId + " not found in cart.");
					}
					break;

				case 3:
					return;

				default:
					System.out.println("Invalid choice. Try again.");
					break;
				}
			}
		} catch (EmptyCartException | NegativeValueException | InvalidInputException e) {
			System.out.println(e.getMessage());
		} catch (InputMismatchException e) {
			System.out.println(" Invalid input. Please enter a valid number.");
			sc.nextLine();
		}
	}

	private static Customer createCustomer() throws InvalidCustomerInputException {
		System.out.print("Enter your name: ");
		String name = sc.nextLine().trim();
		if (!name.matches("^[a-zA-Z\\s]+$")) {
			throw new InvalidCustomerInputException("Name must contain only letters and spaces.");
		}

		System.out.print("Enter your address: ");
		String address = sc.nextLine().trim();
		if (address.isEmpty()) {
			throw new InvalidCustomerInputException("Address cannot be empty.");
		}

		System.out.print("Enter your phone number: ");
		String phone = sc.nextLine().trim();
		if (!phone.matches("\\d{10}")) {
			throw new InvalidCustomerInputException("Phone number must be 10 digits.");
		}

		System.out.print("Enter your email: ");
		String email = sc.nextLine().trim();
		if (!email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$")) {
			throw new InvalidCustomerInputException("Invalid email format.");
		}

		if (customerManager.isCustomerAlreadyRegistered(email)) {
			throw new InvalidCustomerInputException("This email is already registered. Please login.");
		}

		return new Customer(name, address, phone, email);
	}

	private static void addItemToCart() {
		try {
			displayFoodItems();

			System.out.print("\nEnter the ID of the food item to add to cart: ");
			int foodId = sc.nextInt();
			sc.nextLine();

			FoodItem selectedFood = null;
			for (FoodItem item : foodManager.getAllFoods()) {
				if (item.getId() == foodId) {
					selectedFood = item;
					break;
				}
			}

			if (selectedFood == null) {
				throw new InvalidInputException("Invalid Food ID! Please try again.");
			}

			System.out.print("Enter quantity: ");
			int qty = sc.nextInt();
			sc.nextLine();

			if (qty <= 0) {
				throw new InvalidInputException(" Quantity must be greater than 0.");
			}

			boolean itemExists = false;
			for (LineItem item : cart) {
				if (item.getFoodItem().getId() == selectedFood.getId()) {
					item.setQuantity(item.getQuantity() + qty);
					System.out
							.println("✅ Updated quantity of: " + selectedFood.getName() + " to " + item.getQuantity());
					itemExists = true;
					break;
				}
			}

			if (!itemExists) {
				LineItem newItem = new LineItem(cart.size() + 1, qty, selectedFood);
				cart.add(newItem);
				BigDecimal trimmedPrice = BigDecimal.valueOf(selectedFood.getDiscountedPrice()).setScale(2,
						RoundingMode.DOWN);
				System.out.println("Added to cart: " + selectedFood.getName() + " | Quantity: " + qty + " | Price: ₹"
						+ trimmedPrice);
			}

		} catch (InputMismatchException e) {
			System.out.println("Please enter numbers only for ID and quantity.");
			sc.nextLine();
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void viewCart() {
		CartPrinter.printCart(cart);
	}

	private static void removeItemFromCart() {
		System.out.print("Enter Food Item ID to remove: ");
		int foodItemId = sc.nextInt();
		sc.nextLine();

		try {
			boolean removed = cart.removeIf(item -> item.getFoodItem().getId() == foodItemId);
			if (removed) {
				System.out.println("✅ Item removed from cart.");
			} else {
				throw new InvalidInputException("❌ Item with Food ID " + foodItemId + " not found in cart.");
			}
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void placeOrder(Customer customer) {
		
		try {
			if (cart.isEmpty()) {
				throw new EmptyCartException("Cart is empty. Cannot place order.");
			}

			sc.nextLine();
			System.out.print("Do you want to add more items before placing the order? (yes/no): ");
			String input = sc.nextLine().trim().toLowerCase();

			if (input.equals("yes")) {
				addItemToCart();
				return;
			}

			List<DeliveryPartner> deliveryPartners = partnerManager.getActivePartners();

			if (deliveryPartners.isEmpty()) {
				System.out.println("No delivery partner available. Please contact admin.");
				return;
			}

			DeliveryPartner assignedPartner = deliveryPartners.get(new Random().nextInt(deliveryPartners.size()));

			int orderId = orderCounter++;
			Order order = new Order(orderId, new Date());

			for (LineItem item : cart) {
				order.addLineItem(item);
			}

			System.out.println("\n--- Order Summary ---");
			System.out.println("Customer: " + customer.getName());
			System.out.println("Order ID: " + order.getId());
			System.out.println("Date: " + order.getDate());

			System.out.println("\nItems:");
			double total = 0.0;
			for (LineItem item : order.getItems()) {
				double itemCost = item.calculateLineItemCost();
				System.out.printf("- %s (Qty: %d) - ₹%.2f\n", item.getFoodItem().getName(), item.getQuantity(),
						itemCost);
				total += itemCost;
			}

			double discount = 0.0;
			if (total > 500) {
				discount = total * 0.10;
				total -= discount;
				System.out.printf("\n10%% Discount Applied: -₹%.2f\n", discount);
			}

			System.out.printf("Total Payable: ₹%.2f\n", total);

			String paymentMode;
			try {
				paymentMode = PaymentProcessor.selectPaymentMode();
			} catch (PaymentException e) {
				System.out.println("Payment failed: " + e.getMessage());
				System.out.println("Order not placed. Please try again.");
				return;
			}

			order.setPaymentMode(paymentMode);
			order.setDiscount(discount);
			order.setTotalPaid(total);
			order.setDeliveryPartner(assignedPartner);

			 try {
	                orderDao.createOrder(order, customer.getId());
	                System.out.println("Order successfully placed and saved.");
	            } catch (SQLException e) {
	                System.err.println("Error saving order to the database: " + e.getMessage());
	                
	            }
			customer.addOrderToHistory(order);

			InvoiceGenerator.printInvoice(order, customer, paymentMode, discount, total, assignedPartner);

			cart.clear();

		} catch (EmptyCartException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
