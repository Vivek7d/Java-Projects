package com.bankingapp.controller;

import com.bankingapp.model.User;
import com.bankingapp.service.TransactionService;
import com.bankingapp.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();
    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "dashboard";

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
                case "dashboard":
                default:
                    // Just show the main admin home page
                    request.getRequestDispatcher("admin_home.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in Admin Controller", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("addCustomer".equals(action)) {
                addCustomer(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error on POST action", e);
        }
    }

    private void showAddCustomerForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("view", "add_customer");
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin_home.jsp");
        dispatcher.forward(request, response);
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> customerList = userService.getAllCustomers();
        request.setAttribute("customerList", customerList);
        request.setAttribute("view", "list_customers");
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin_home.jsp");
        dispatcher.forward(request, response);
    }

    private void listAllTransactions(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        request.setAttribute("transactionList", transactionService.getAllTransactions());
        request.setAttribute("view", "list_transactions");
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin_home.jsp");
        dispatcher.forward(request, response);
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Add validation here if needed
        
        userService.createCustomer(firstName, lastName, email, password);
        response.sendRedirect(request.getContextPath() + "/admin?action=listCustomers");
    }
}