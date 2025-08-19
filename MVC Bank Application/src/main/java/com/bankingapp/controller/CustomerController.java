package com.bankingapp.controller;

import com.bankingapp.model.Transaction;
import com.bankingapp.model.User;
import com.bankingapp.service.TransactionService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customer")
public class CustomerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "dashboard";
        
        // Basic check to ensure a customer is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            switch (action) {
                case "passbook":
                    showPassbook(request, response);
                    break;
                case "showTransaction":
                    showTransactionForm(request, response);
                    break;
                case "showProfile":
                    showProfileForm(request, response);
                    break;
                case "dashboard":
                default:
                    request.getRequestDispatcher("customer_home.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in Customer Controller", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            if ("processTransaction".equals(action)) {
                processTransaction(request, response);
            } else if ("updateProfile".equals(action)) { // <-- ADD THIS
                updateProfile(request, response);
            }
        }  catch (SQLException e) {
            throw new ServletException("Database error on POST action", e);
        }
    }
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Get form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");

        // Update the user object
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        
        // Only update the password in the service layer if a new one was provided
        // You will need a new method in UserService for this logic

        // TODO: Create an 'updateUserProfile' method in your UserService
        // This method should handle the logic of checking if the password is new
        // and then call the appropriate DAO method.
        // Example: userService.updateUserProfile(currentUser, password);

        // For now, we will add a placeholder message
        request.setAttribute("message", "Profile updated successfully!");

        // After updating, you must refresh the session object so the user sees the changes
        session.setAttribute("user", currentUser); 
        
        // Forward back to the edit profile page to show the success message
        request.setAttribute("view", "edit_profile");
        request.getRequestDispatcher("customer_home.jsp").forward(request, response);
    }
    private void showPassbook(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Transaction> history = transactionService.getTransactionHistory(user.getAccountNumber());
        request.setAttribute("transactionHistory", history);
        request.setAttribute("view", "passbook");
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_home.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showTransactionForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("view", "new_transaction");
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_home.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showProfileForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("view", "edit_profile");
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_home.jsp");
        dispatcher.forward(request, response);
    }
    
    private void processTransaction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        User sender = (User) request.getSession().getAttribute("user");
        String receiverAccNo = request.getParameter("receiverAccNo");
        String amountStr = request.getParameter("amount");

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            boolean success = transactionService.performTransfer(sender.getAccountNumber(), receiverAccNo, amount);

            if (success) {
                // IMPORTANT: Refresh user data in session to show updated balance
                // You might need to add a method in UserService to get a fresh user object by ID
                // For now, redirecting to passbook which doesn't show the new balance on the top bar
                response.sendRedirect(request.getContextPath() + "/customer?action=passbook");
            } else {
                // Set an error message and forward back to the form
                request.setAttribute("error", "Transaction failed. Check receiver account number and your balance.");
                request.setAttribute("view", "new_transaction");
                request.getRequestDispatcher("customer_home.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount entered.");
            request.setAttribute("view", "new_transaction");
            request.getRequestDispatcher("customer_home.jsp").forward(request, response);
        }
    }
}