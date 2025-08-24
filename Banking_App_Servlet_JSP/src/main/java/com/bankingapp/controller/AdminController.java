package com.bankingapp.controller;

import com.bankingapp.exception.UserAlreadyExistsException;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserService();
	private final TransactionService transactionService = new TransactionService();
	private final DashboardService dashboardService = new DashboardService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "showInsights";
		}

		try {
			switch (action) {
			case "showAddCustomer":
				showAddCustomerForm(request, response);
				break;
			case "listCustomers":
				listCustomers(request, response);
				break;
			case "listTransactions":
				listAllTransactions(request, response);
				break;
			case "showAiChat":
		        request.setAttribute("view", "bank_ai");
		        request.getRequestDispatcher("/WEB-INF/views/admin_home.jsp").forward(request, response);
		        break;
			case "delete":
				deleteCustomer(request, response);
				break;

			case "reactivate":
				reactivateCustomer(request, response);
				break;

			case "showUpdateForm":
				loadCustomerForEdit(request, response);
				break;
			case "dashboard":
			case "showInsights":
			default:
				showInsights(request, response);
				break;
			}
		} catch (SQLException e) {
			throw new ServletException("Database error in Admin Controller", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		try {
			if ("addCustomer".equals(action)) {
				addCustomer(request, response);
			} else if ("updateCustomer".equals(action)) {
				updateCustomer(request, response);
			}
		} catch (SQLException e) {
			throw new ServletException("Database error on POST action", e);
		}
	}

	private void showInsights(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {

		Map<String, Object> insights = dashboardService.getDashboardInsights();
		request.setAttribute("insights", insights);

		request.setAttribute("view", "insights");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin_home.jsp");
		dispatcher.forward(request, response);
	}

	private void loadCustomerForEdit(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		User existingCustomer = userService.getCustomerById(customerId);

		request.setAttribute("customer", existingCustomer);

		request.setAttribute("view", "update_customer");

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin_home.jsp");
		dispatcher.forward(request, response);
	}

	private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		if (!firstName.matches("[a-zA-Z\\s]+")) {
			request.setAttribute("error_toast", "First name can only contain letters and spaces.");
			loadCustomerForEdit(request, response);
			return;
		}

		if (!lastName.matches("[a-zA-Z\\s]+")) {
			request.setAttribute("error_toast", "Last name can only contain letters and spaces.");
			loadCustomerForEdit(request, response);
			return;
		}

		if (userService.isEmailTakenByAnotherUser(email, id)) {
			request.setAttribute("error_toast", "This email address is already registered to another customer.");
			loadCustomerForEdit(request, response);
			return;
		}

		User customer = new User(id, firstName, lastName, email);
		userService.updateCustomerByAdmin(customer);

		HttpSession session = request.getSession();
		session.setAttribute("success_toast", "Customer details updated successfully!");
		response.sendRedirect(request.getContextPath() + "/admin?action=listCustomers");
	}

	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		userService.deleteCustomer(customerId);
		response.sendRedirect(request.getContextPath() + "/admin?action=listCustomers");
	}

	private void showAddCustomerForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("view", "add_customer");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin_home.jsp");
		dispatcher.forward(request, response);
	}

	private void listCustomers(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		List<User> customerList = userService.getAllCustomersWithStatus();
		request.setAttribute("customerList", customerList);
		request.setAttribute("view", "list_customers");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin_home.jsp");
		dispatcher.forward(request, response);
	}

	private void reactivateCustomer(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		userService.reactivateCustomer(customerId);

		HttpSession session = request.getSession();
		session.setAttribute("success_toast", "Account reactivated successfully!");

		response.sendRedirect(request.getContextPath() + "/admin?action=listCustomers");
	}

	private void listAllTransactions(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		request.setAttribute("transactionList", transactionService.getAllTransactions());
		request.setAttribute("view", "list_transactions");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin_home.jsp");
		dispatcher.forward(request, response);
	}

	private void addCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		if (!firstName.matches("[a-zA-Z\\s]+")) {
			request.setAttribute("error_toast", "First name can only contain letters and spaces.");
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.setAttribute("email", email);
			showAddCustomerForm(request, response);
			return;
		}

		if (!lastName.matches("[a-zA-Z\\s]+")) {
			request.setAttribute("error_toast", "Last name can only contain letters and spaces.");
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.setAttribute("email", email);
			showAddCustomerForm(request, response);
			return;
		}

		try {
			userService.createCustomer(firstName, lastName, email, password);

			HttpSession session = request.getSession();
			session.setAttribute("success_toast", "Customer created successfully!");
			response.sendRedirect(request.getContextPath() + "/admin?action=listCustomers");

		} catch (UserAlreadyExistsException e) {
			request.setAttribute("error_toast", e.getMessage());

			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.setAttribute("email", email);

			showAddCustomerForm(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error_toast", "A database error occurred. Please check logs.");
			showAddCustomerForm(request, response);
		}
	}

}