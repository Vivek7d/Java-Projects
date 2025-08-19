<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin Dashboard</title>
    <style> /* Add CSS for a basic layout */ </style>
</head>
<body>
    <h1>Admin Home</h1>
    <p>Welcome, ${sessionScope.user.firstName}!</p>
    <hr>
    
    <nav>
        <a href="admin?action=showAddCustomer">Add New Customer</a> |
        <a href="admin?action=listCustomers">View Customers</a> |
        <a href="admin?action=listTransactions">View All Transactions</a> |
        <a href="auth?action=logout">Logout</a>
    </nav>
    <hr>

    <%-- The content for different actions will be included here dynamically --%>
    <%-- For example, you can use JSTL to conditionally include pages --%>
    <c:if test="${requestScope.view == 'add_customer'}">
        <jsp:include page="add_customer.jsp" />
    </c:if>
    <c:if test="${requestScope.view == 'list_customers'}">
        <jsp:include page="view_customers.jsp" />
    </c:if>
     <c:if test="${requestScope.view == 'list_transactions'}">
        <jsp:include page="view_transactions_admin.jsp" />
    </c:if>
</body>
</html>