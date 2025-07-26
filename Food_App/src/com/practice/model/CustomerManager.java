package com.practice.model;



import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private static final String FILE_NAME = "customer.txt";
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = loadCustomers();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomers();
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    private void saveCustomers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(customers);
        } catch (IOException e) {
            System.out.println("❌ Error saving customer data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Customer> loadCustomers() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Customer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public boolean isCustomerAlreadyRegistered(String name) {
        List<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public Customer getCustomerByName(String name) {
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }


}
