<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .profile-form {
        width: 400px;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
        background-color: #f9f9f9;
    }
    .profile-form h3 {
        margin-top: 0;
        text-align: center;
    }
    .profile-form label {
        display: block;
        margin-bottom: 5px;
    }
    .profile-form input[type="text"],
    .profile-form input[type="password"] {
        width: 100%;
        padding: 8px;
        margin-bottom: 15px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box; /* Important for padding to not affect width */
    }
    .profile-form .form-actions {
        display: flex;
        justify-content: space-between;
    }
    .profile-form button {
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .profile-form .update-btn {
        background-color: #28a745;
        color: white;
    }
    .profile-form .cancel-btn {
        background-color: #6c757d;
        color: white;
        text-decoration: none; /* For the <a> tag styled as a button */
    }
    .message {
        text-align: center;
        padding: 10px;
        margin-bottom: 15px;
        border-radius: 4px;
    }
    .message.success {
        color: #155724;
        background-color: #d4edda;
        border: 1px solid #c3e6cb;
    }
    .message.error {
        color: #721c24;
        background-color: #f8d7da;
        border: 1px solid #f5c6cb;
    }
</style>

<div class="profile-form">
    <h3>Edit Your Profile</h3>

    <%-- Display success or error messages from the servlet --%>
    <c:if test="${not empty requestScope.message}">
        <p class="message success">${requestScope.message}</p>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <p class="message error">${requestScope.error}</p>
    </c:if>

    <form action="customer" method="post">
        <%-- This hidden field tells the CustomerController which action to perform --%>
        <input type="hidden" name="action" value="updateProfile">

        <div>
            <label for="firstName">Customer First Name:</label>
            <%-- Pre-fill the input with the user's current first name from the session --%>
            <input type="text" id="firstName" name="firstName" value="${sessionScope.user.firstName}" required>
        </div>

        <div>
            <label for="lastName">Customer Last Name:</label>
            <%-- Pre-fill the input with the user's current last name from the session --%>
            <input type="text" id="lastName" name="lastName" value="${sessionScope.user.lastName}" required>
        </div>
        
        <div>
            <label for="password">New Password:</label>
            <%-- For security, the password field is never pre-filled --%>
            <input type="password" id="password" name="password" placeholder="Leave blank to keep current password">
        </div>

        <div class="form-actions">
            <button type="submit" class="update-btn">Update</button>
            <%-- The cancel button simply navigates back to the main customer dashboard --%>
            <a href="${pageContext.request.contextPath}/customer?action=dashboard" class="cancel-btn">Cancel</a>
        </div>
    </form>
</div>