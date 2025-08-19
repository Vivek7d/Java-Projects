<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h3>All Transactions</h3>
<table border="1">
    <tr>
        <th>Sender Acc No</th>
        <th>Receiver Acc No</th>
        <th>Type of Transaction</th>
        <th>Amount</th>
        <th>Date</th>
    </tr>
    <c:forEach var="tx" items="${requestScope.transactionList}">
        <tr>
            <td>${tx.senderAccountNumber}</td>
            <td>${tx.receiverAccountNumber}</td>
            <td>${tx.transactionType}</td>
            <td>${tx.amount}</td>
            <td>${tx.transactionDate}</td>
        </tr>
    </c:forEach>
</table>