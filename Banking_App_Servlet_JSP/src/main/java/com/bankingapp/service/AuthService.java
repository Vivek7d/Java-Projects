package com.bankingapp.service;

import com.bankingapp.dao.UserDao;
import com.bankingapp.exception.AccountDeactivatedException;
import com.bankingapp.model.User;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthService {

	private UserDao userDao = new UserDao();

	public String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hash);
			StringBuilder hexString = new StringBuilder(number.toString(16));
			while (hexString.length() < 64) {
				hexString.insert(0, '0');
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public User login(String email, String password, String role) throws SQLException, AccountDeactivatedException {
		User user = userDao.getUserByEmail(email);

		if (user != null) {
			if (!user.isActive()) {
				throw new AccountDeactivatedException("Your account has been deactivated by an administrator.");
			}

			if (user.getRole().equalsIgnoreCase(role)) {
				String hashedPassword = hashPassword(password);
				if (user.getPassword().equals(hashedPassword)) {
					return user;
				}
			}
		}

		return null;
	}
}