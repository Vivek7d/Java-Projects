package com.bankingapp.service;

import com.bankingapp.dao.UserDao;
import com.bankingapp.model.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class UserService {

    private UserDao userDao = new UserDao();
    private AuthService authService = new AuthService();

    private String generateAccountNumber() {
        // Generates a random 10-digit account number
        Random random = new Random();
        long number = 1000000000L + random.nextInt(900000000);
        return String.valueOf(number);
    }

    public void createCustomer(String firstName, String lastName, String email, String password) throws SQLException {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(authService.hashPassword(password));
        user.setRole("Customer");
        user.setAccountNumber(generateAccountNumber());
        user.setBalance(new BigDecimal("0.00")); // Initial balance
        userDao.addUser(user);
    }

    public List<User> getAllCustomers() throws SQLException {
        return userDao.getAllCustomers();
    }
    
    // Add other user-related business logic here, like updating profiles
}