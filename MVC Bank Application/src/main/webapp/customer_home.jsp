<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Customer Dashboard</title>
</head>
<body>
    <h1>Customer Home</h1>
    <p>Welcome, ${sessionScope.user.firstName}!</p>
    <p>Account Number: ${sessionScope.user.accountNumber}</p>
    <p>Balance: $${sessionScope.user.balance}</p>
    <hr>

    <nav>
        <a href="customer?action=passbook">Passbook</a> |
        <a href="customer?action=showTransaction">New Transaction</a> |
        <a href="customer?action=showProfile">Edit Profile</a> |
        <a href="auth?action=logout">Logout</a>
    </nav>
    <hr>
    
    <c:if test="${requestScope.view == 'passbook'}">
        <jsp:include page="passbook.jsp" />
    </c:if>
    <c:if test="${requestScope.view == 'new_transaction'}">
        <jsp:include page="new_transaction.jsp" />
    </c:if>
    <c:if test="${requestScope.view == 'edit_profile'}">
        <jsp:include page="edit_profile.jsp" />
    </c:if>

</body>
</html>