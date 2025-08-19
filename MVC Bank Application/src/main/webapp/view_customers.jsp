<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h3>All Customers</h3>
<table border="1">
    <tr>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Account Number</th>
        <th>Balance</th>
    </tr>
    <c:forEach var="customer" items="${requestScope.customerList}">
        <tr>
            <td>${customer.firstName}</td>
            <td>${customer.lastName}</td>
            <td>${customer.accountNumber}</td>
            <td>${customer.balance}</td>
        </tr>
    </c:forEach>
</table>