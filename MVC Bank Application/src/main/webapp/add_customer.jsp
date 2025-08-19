<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3>Add New Customer</h3>
<form action="admin" method="post">
    <input type="hidden" name="action" value="addCustomer">
    First Name: <input type="text" name="firstName" required><br>
    Last Name: <input type="text" name="lastName" required><br>
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <input type="submit" value="Submit">
</form>