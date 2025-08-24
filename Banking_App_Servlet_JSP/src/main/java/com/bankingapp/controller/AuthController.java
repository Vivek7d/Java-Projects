package com.bankingapp.controller;

import com.bankingapp.exception.AccountDeactivatedException;
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

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            logout(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try {
            User user = authService.login(email, password, role);

            if (user != null) {
                // Login successful, create a session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60);

                session.setAttribute("success_toast", "Login Successful!");
                // Redirect based on user role
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                } else if ("Customer".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/customer?action=dashboard");
                } else {
                    request.setAttribute("error", "User role is not recognized.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // Generic login failure
                request.setAttribute("error", "Invalid email, password, or role.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (AccountDeactivatedException e) {
            request.setAttribute("deactivated_error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error during login. Please try again later.", e);
        }
    }

    
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            session.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("logout_toast", "Logout Successful!");
        response.sendRedirect("login.jsp");
    }
}