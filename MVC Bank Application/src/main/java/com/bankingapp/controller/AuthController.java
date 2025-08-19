package com.bankingapp.controller;

import com.bankingapp.model.User;
import com.bankingapp.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth")
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AuthService authService = new AuthService();

    /**
     * Handles POST requests, primarily for the login action.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            login(request, response);
        } else {
            // Handle other potential POST actions or show an error
            response.sendRedirect("login.jsp");
        }
    }

    /**
     * Handles GET requests, primarily for the logout action.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            logout(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    /**
     * Authenticates a user based on form parameters.
     * On success, creates a session and redirects to the appropriate dashboard.
     * On failure, forwards back to the login page with an error message.
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try {
            User user = authService.login(email, password, role);

            if (user != null) {
                // Login successful, create a session
                HttpSession session = request.getSession();
                session.setAttribute("user", user); // Store the entire user object
                session.setMaxInactiveInterval(30 * 60); // Set session timeout to 30 minutes

                // Redirect based on user role
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                } else if ("Customer".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/customer?action=dashboard");
                } else {
                    // Fallback for undefined roles
                    request.setAttribute("error", "User role is not recognized.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // Login failed
                request.setAttribute("error", "Invalid email, password, or role.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error during login. Please try again later.", e);
        }
    }

    /**
     * Invalidates the current session and redirects the user to the login page.
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // Get session if it exists, don't create a new one
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}