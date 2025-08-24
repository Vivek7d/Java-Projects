package com.bankingapp.service;

import com.bankingapp.dao.UserDao;
import com.bankingapp.exception.UserAlreadyExistsException;
import com.bankingapp.model.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class UserService {

	private UserDao userDao = new UserDao();
	private AuthService authService = new AuthService();

	private String generateAccountNumber() {
		Random random = new Random();
		long number = 1000000000L + random.nextInt(900000000);
		return String.valueOf(number);
	}

	public void createCustomer(String firstName, String lastName, String email, String password)
			throws SQLException, UserAlreadyExistsException {
		if (userDao.getUserByEmail(email) != null) {
			throw new UserAlreadyExistsException("A customer with the email '" + email + "' already exists.");
		}

		User user = new User(firstName, lastName, email, authService.hashPassword(password), "Customer", null,
				new BigDecimal("0.00"));

		int newUserId = userDao.addUserAndReturnId(user);

		String accountNumber = "1000000000" + newUserId;

		userDao.updateAccountNumber(newUserId, accountNumber);
	}

	public List<User> getAllCustomers() throws SQLException {
		return userDao.getAllCustomers();
	}

	public User updateUserProfile(int userId, String firstName, String lastName, String newPassword)
			throws SQLException {

		User user = userDao.getUserById(userId);
		if (user == null) {
			return null;
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);

		if (newPassword != null && !newPassword.isEmpty()) {
			user.setPassword(authService.hashPassword(newPassword));
		}

		userDao.updateUserProfile(user);

		return user;
	}

	public User updateUserProfile(int userId, String firstName, String lastName, String email, String newPassword)
			throws SQLException {
		User user = userDao.getUserById(userId);
		if (user == null) {
			return null;
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);

		if (newPassword != null && !newPassword.isEmpty()) {
			user.setPassword(authService.hashPassword(newPassword));
		}

		userDao.updateUserProfile(user);
		return user;
	}

	public boolean isEmailTakenByAnotherUser(String email, int currentUserId) throws SQLException {
		User userByEmail = userDao.getUserByEmail(email);
		
		return userByEmail != null && userByEmail.getId() != currentUserId;
	}

	public void deleteCustomer(int customerId) throws SQLException {
		userDao.softDeleteUserById(customerId);
	}

	public User getCustomerById(int customerId) throws SQLException {
		return userDao.getUserById(customerId);
	}

	public void updateCustomerByAdmin(User user) throws SQLException {
		userDao.updateUserByAdmin(user);
	}

	public List<User> getAllCustomersWithStatus() throws SQLException {
		return userDao.getAllCustomersWithStatus();
	}

	public void reactivateCustomer(int customerId) throws SQLException {
		userDao.reactivateUserById(customerId);
	}
	public List<User> getAllUsersForAiContext() throws SQLException {
	    return userDao.getAllUsersForAiContext();
	}

}