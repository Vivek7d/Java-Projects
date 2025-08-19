<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Banking App - Login</title>
    <style>
        /* Add some basic styling */
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
        .login-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 350px; }
        .login-container h2 { text-align: center; margin-bottom: 1.5rem; }
        .login-container input, .login-container select, .login-container button { width: 100%; padding: 10px; margin-bottom: 1rem; border-radius: 4px; border: 1px solid #ddd; box-sizing: border-box; }
        .login-container button { background-color: #007bff; color: white; border: none; cursor: pointer; }
        .login-container .error { color: red; text-align: center; margin-bottom: 1rem; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Welcome to Bank</h2>
        
        <%-- Display error message if login fails --%>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p class="error"><%= error %></p>
        <%
            }
        %>

        <form action="auth" method="post">
            <input type="hidden" name="action" value="login">
            
            <label for="email">Username (Email):</label>
            <input type="email" id="email" name="email" required>
            
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            
            <label for="role">Login AS:</label>
            <select id="role" name="role">
                <option value="Customer">Customer</option>
                <option value="Admin">Admin</option>
            </select>
            
            <button type="submit">Login</button>
        </form>
    </div>
</body>
