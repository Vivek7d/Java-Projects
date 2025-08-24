package com.bankingapp.dao;

import com.bankingapp.model.User;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {

	private DataSource dataSource;

	public UserDao() {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/banking-source");
		} catch (Exception e) {
			throw new RuntimeException("Error initializing DataSource", e);
		}
	}

	public void addUser(User user) throws SQLException {
		String sql = "INSERT INTO users (first_name, last_name, email, password, role, account_number, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getPassword());
			stmt.setString(5, user.getRole());
			stmt.setString(6, user.getAccountNumber());
			stmt.setBigDecimal(7, user.getBalance());
			stmt.executeUpdate();
		}
	}

	public User getUserByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM users WHERE email = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {

					return new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
							rs.getString("email"), rs.getString("password"), rs.getString("role"),
							rs.getString("account_number"), rs.getBigDecimal("balance"), rs.getBoolean("is_active"));
				}
			}
		}
		return null;
	}

	public User getUserByAccountNumber(String accountNumber) throws SQLException {
		String sql = "SELECT * FROM users WHERE account_number = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, accountNumber);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {

					return new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
							rs.getString("email"), rs.getString("password"), rs.getString("role"),
							rs.getString("account_number"), rs.getBigDecimal("balance"), rs.getBoolean("is_active"));
				}
			}
		}
		return null;
	}

	public List<User> getAllCustomers() throws SQLException {
		List<User> customers = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE role = 'Customer' AND is_active = 1";
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {

				customers.add(new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("email"), rs.getString("password"), // Note: you can pass null if not needed on
																			// this screen
						rs.getString("role"), rs.getString("account_number"), rs.getBigDecimal("balance"),
						rs.getBoolean("is_active")));
			}
		}
		return customers;
	}

	public void softDeleteUserById(int userId) throws SQLException {
		String sql = "UPDATE users SET is_active = 0 WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		}
	}

	public void updateUserProfile(User user) throws SQLException {

		String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getPassword());
			stmt.setInt(5, user.getId());
			stmt.executeUpdate();
		}
	}

	public User getUserById(int userId) throws SQLException {
		String sql = "SELECT * FROM users WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {

					return new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
							rs.getString("email"), rs.getString("password"), rs.getString("role"),
							rs.getString("account_number"), rs.getBigDecimal("balance"), rs.getBoolean("is_active"));
				}
			}
		}
		return null;
	}

	public void updateBalance(String accountNumber, BigDecimal newBalance) throws SQLException {
		String sql = "UPDATE users SET balance = ? WHERE account_number = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setBigDecimal(1, newBalance);
			stmt.setString(2, accountNumber);
			stmt.executeUpdate();
		}
	}

	public void deleteUserById(int userId) throws SQLException {

		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		}
	}

	public void updateUserByAdmin(User user) throws SQLException {
		String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getEmail());
			stmt.setInt(4, user.getId());
			stmt.executeUpdate();
		}
	}

	public int getTotalCustomerCount() throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE role = 'Customer'";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public BigDecimal getTotalBankBalance() throws SQLException {
		String sql = "SELECT SUM(balance) FROM users WHERE role = 'Customer'";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getBigDecimal(1);
			}
		}
		return BigDecimal.ZERO;
	}

	 public Map<String, Integer> getUserStatusCounts() throws SQLException {
			Map<String, Integer> statusCounts = new HashMap<>();
			String sql = "SELECT is_active, COUNT(*) as count FROM users WHERE role = 'Customer' GROUP BY is_active";
			try (Connection conn = dataSource.getConnection();
					PreparedStatement stmt = conn.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					boolean isActive = rs.getBoolean("is_active");
					int count = rs.getInt("count");
					if (isActive) {
						statusCounts.put("active", count);
					} else {
						statusCounts.put("inactive", count);
					}
				}
			}
			return statusCounts;
		}

	public List<User> getTop5CustomersByBalance() throws SQLException {
		List<User> topCustomers = new ArrayList<>();
		String sql = "SELECT first_name, last_name, balance FROM users WHERE role = 'Customer' ORDER BY balance DESC LIMIT 5";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {

				topCustomers.add(
						new User(rs.getString("first_name"), rs.getString("last_name"), rs.getBigDecimal("balance")));
			}
		}
		return topCustomers;
	}

	public int addUserAndReturnId(User user) throws SQLException {

		String sql = "INSERT INTO users (first_name, last_name, email, password, role, balance) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getPassword());
			stmt.setString(5, user.getRole());
			stmt.setBigDecimal(6, user.getBalance());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
		}
	}

	public void updateAccountNumber(int userId, String accountNumber) throws SQLException {
		String sql = "UPDATE users SET account_number = ? WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, accountNumber);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
		}
	}

	public List<User> getAllCustomersWithStatus() throws SQLException {
		List<User> customers = new ArrayList<>();

		String sql = "SELECT * FROM users WHERE role = 'Customer'";
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				customers.add(new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("email"), rs.getString("password"), rs.getString("role"),
						rs.getString("account_number"), rs.getBigDecimal("balance"), rs.getBoolean("is_active")));
			}
		}
		return customers;
	}

	public void reactivateUserById(int userId) throws SQLException {
		String sql = "UPDATE users SET is_active = 1 WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		}
	}
	public List<User> getAllUsersForAiContext() throws SQLException {
	    List<User> allUsers = new ArrayList<>();
	    // Get all users, including admin, active, and inactive
	    String sql = "SELECT * FROM users";
	    try (Connection conn = dataSource.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            allUsers.add(new User(
	                rs.getInt("id"),
	                rs.getString("first_name"),
	                rs.getString("last_name"),
	                rs.getString("email"),
	                // We don't need the password for the AI
	                null, 
	                rs.getString("role"),
	                rs.getString("account_number"),
	                rs.getBigDecimal("balance"),
	                rs.getBoolean("is_active")
	            ));
	        }
	    }
	    return allUsers;
	}
}