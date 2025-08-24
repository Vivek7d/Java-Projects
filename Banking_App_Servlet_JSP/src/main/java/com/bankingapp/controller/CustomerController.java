package com.bankingapp.controller;

import com.bankingapp.model.Transaction;
import com.bankingapp.model.User;
import com.bankingapp.service.DashboardService;
import com.bankingapp.service.TransactionService;
import com.bankingapp.service.UserService;

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
import java.util.Map;

@WebServlet("/customer")
public class CustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final TransactionService transactionService = new TransactionService();
	private final UserService userService = new UserService();
	private final DashboardService dashboardService = new DashboardService();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
        if (action == null) {
            action = "dashboard";
        }

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
        User user = (User) session.getAttribute("user");

		try {
			switch (action) {
    			case "passbook":
    				showPassbook(request, response);
    				break;
    			case "showTransaction":
    				
    				showTransactionForm(request, response, user); 
    				break;
    			case "showProfile":
    				showProfileForm(request, response);
    				break;
    			case "dashboard":
    			default:
                    Map<String, Object> insights = dashboardService.getCustomerDashboardInsights(user.getAccountNumber());
                    request.setAttribute("insights", insights);
                    request.setAttribute("view", "dashboard");
    				request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp").forward(request, response);
    				break;
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
			throw new ServletException("Database error in Customer Controller", e);
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		try {
			if ("processTransaction".equals(action)) {
				processTransaction(request, response);
			} else if ("updateProfile".equals(action)) { 
				updateProfile(request, response);
			}
		} catch (SQLException e) {
			throw new ServletException("Database error on POST action", e);
		}
	}

	private void updateProfile(HttpServletRequest request, HttpServletResponse response)
	        throws SQLException, ServletException, IOException {
	    HttpSession session = request.getSession();
	    User currentUser = (User) session.getAttribute("user");

	    String firstName = request.getParameter("firstName");
	    String lastName = request.getParameter("lastName");
	    String email = request.getParameter("email");
	    String newPassword = request.getParameter("password");

	    
	    Runnable onError = () -> {
	        request.setAttribute("submittedFirstName", firstName);
	        request.setAttribute("submittedLastName", lastName);
	        request.setAttribute("submittedEmail", email);
	        try {
	            showProfileForm(request, response);
	        } catch (ServletException | IOException e) {
	            e.printStackTrace();
	        }
	    };

	    if (!firstName.matches("[a-zA-Z\\s]+")) {
	        request.setAttribute("error_toast", "First name must contain only letters.");
	        onError.run();
	        return;
	    }
	    if (!lastName.matches("[a-zA-Z\\s]+")) {
	        request.setAttribute("error_toast", "Last name must contain only letters.");
	        onError.run();
	        return;
	    }
	    if (userService.isEmailTakenByAnotherUser(email, currentUser.getId())) {
	        request.setAttribute("error_toast", "This email is already registered.");
	        onError.run();
	        return;
	    }

	    // --- If validation passes ---
	    User updatedUser = userService.updateUserProfile(currentUser.getId(), firstName, lastName, email, newPassword);

	    if (updatedUser != null) {
	        session.setAttribute("user", updatedUser);
	        session.setAttribute("success_toast", "Profile updated successfully!");
	        response.sendRedirect(request.getContextPath() + "/customer?action=dashboard");
	    } else {
	        request.setAttribute("error_toast", "An unexpected error occurred while updating.");
	        onError.run();
	    }
	}
	private void showPassbook(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		List<Transaction> history = transactionService.getTransactionHistory(user.getAccountNumber());
		request.setAttribute("transactionHistory", history);
		request.setAttribute("view", "passbook");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp");
		dispatcher.forward(request, response);
	}

	private void showTransactionForm(HttpServletRequest request, HttpServletResponse response, User user)
			throws SQLException, ServletException, IOException {
		List<Transaction> recentTransactions = transactionService.getRecentTransactions(user.getAccountNumber());
		request.setAttribute("recentTransactions", recentTransactions);
		request.setAttribute("view", "new_transaction");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp");
		dispatcher.forward(request, response);
	}

	private void showProfileForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("view", "edit_profile");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp");
		dispatcher.forward(request, response);
	}

	private void processTransaction(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		User sender = (User) session.getAttribute("user");
		String transactionType = request.getParameter("type");
		String amountStr = request.getParameter("amount");

		try {
			BigDecimal amount = new BigDecimal(amountStr);
			boolean success = false;
			if ("CREDIT".equals(transactionType)) {
				success = transactionService.performCredit(sender.getAccountNumber(), amount);
				if (success) {
					session.setAttribute("success_toast", "Amount credited successfully!");
				}
			} else { // Default to TRANSFER
				String receiverAccNo = request.getParameter("receiverAccNo");
				if (sender.getAccountNumber().equals(receiverAccNo)) {
					request.setAttribute("error", "Transfer to the same account is not allowed.");
					request.setAttribute("view", "new_transaction");
					request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp").forward(request, response);
					return; 
				}
				success = transactionService.performTransfer(sender.getAccountNumber(), receiverAccNo, amount);
				if (success) {
					session.setAttribute("success_toast", "Transaction Successful!");
				}
			}
			if (success) {
				User updatedUser = userService.getCustomerById(sender.getId());
				session.setAttribute("user", updatedUser);
				session.setAttribute("success_toast", "Transaction Successful!");
				response.sendRedirect(request.getContextPath() + "/customer?action=passbook");
			} else {
				request.setAttribute("error", "Transaction failed. Check receiver account number and your balance.");
				request.setAttribute("error", "Transaction failed. Check receiver account number and your balance.");
				request.setAttribute("view", "new_transaction");
				request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp").forward(request, response);
			}
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid amount entered.");
			request.setAttribute("view", "new_transaction");
			request.getRequestDispatcher("/WEB-INF/views/customer_home.jsp").forward(request, response);
		}
	}
}