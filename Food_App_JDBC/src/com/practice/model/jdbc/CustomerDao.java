package com.practice.model.jdbc;

import com.practice.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

	public Customer registerCustomer(Customer customer) throws SQLException {
		String sql = "INSERT INTO customers (name, address, phone_number, email) VALUES (?, ?, ?, ?)";
		try (Connection conn = DbUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getAddress());
			pstmt.setString(3, customer.getPhoneNumber());
			pstmt.setString(4, customer.getEmail());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						customer.setId(generatedKeys.getInt(1));
						return customer;
					}
				}
			}
		}
		return null;
	}

	public Customer findCustomerByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM customers WHERE email = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapRowToCustomer(rs);
				}
			}
		}
		return null;
	}

	public List<Customer> getAllCustomers() throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM customers ORDER BY customer_id";
		try (Connection conn = DbUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				customers.add(mapRowToCustomer(rs));
			}
		}
		return customers;
	}

	private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
		int id = rs.getInt("customer_id");
		String name = rs.getString("name");
		String address = rs.getString("address");
		String phoneNumber = rs.getString("phone_number");
		String email = rs.getString("email");

		return new Customer(id, name, address, phoneNumber, email);
	}
}