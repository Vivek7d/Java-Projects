package com.practice.model;

import com.practice.model.jdbc.CustomerDao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CustomerManager {

	private final CustomerDao customerDao;

	public CustomerManager() {
		this.customerDao = new CustomerDao();
	}

	public Customer registerCustomer(Customer customer) {
		try {
			return customerDao.registerCustomer(customer);
		} catch (SQLException e) {

			if (e.getSQLState().equals("23000")) {
				System.err.println("Error: A customer with this email already exists.");
			} else {
				System.err.println("Database error during customer registration: " + e.getMessage());
			}
			return null;
		}
	}

	public List<Customer> getAllCustomers() {
		try {
			return customerDao.getAllCustomers();
		} catch (SQLException e) {
			System.err.println("Database error fetching all customers: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public boolean isCustomerAlreadyRegistered(String email) {
		return getCustomerByEmail(email) != null;
	}

	public Customer getCustomerByEmail(String email) {
		try {
			return customerDao.findCustomerByEmail(email);
		} catch (SQLException e) {
			System.err.println("Database error finding customer by email: " + e.getMessage());
			return null;
		}
	}
}