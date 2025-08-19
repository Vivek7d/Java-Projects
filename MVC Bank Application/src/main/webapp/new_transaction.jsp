<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<h3>New Transaction</h3>
<form action="customer" method="post">
	<input type="hidden" name="action" value="processTransaction">
	Type of Transaction: <select name="type">
		<option value="TRANSFER">Transfer</option>
		<%-- Add Credit/Debit options if implementing --%>
	</select><br> To Account Number: <input type="text" name="receiverAccNo"
		required><br> Amount: <input type="number" step="0.01"
		name="amount" required><br> <input type="submit"
		value="Submit">
</form>