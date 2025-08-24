<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3 class="mb-4 text-center">All Transactions</h3>
<div class="row mb-3 align-items-center">
    <!-- Search Input -->
    <div class="col-md-5">
        <div class="input-group">
            <span class="input-group-text"><i class="bi bi-search"></i></span>
            <input type="text" id="searchInput" class="form-control" placeholder="Search by Sender or Receiver Account No..." onkeyup="filterAndSearchTable()">
        </div>
    </div>
    <!-- Type Filter Dropdown -->
    <div class="col-md-3">
        <select id="typeFilter" class="form-select" onchange="filterAndSearchTable()">
            <option value="ALL" selected>All Transaction Types</option>
            <option value="CREDIT">Credits Only</option>
            <option value="TRANSFER">Transfers Only</option>
        </select>
    </div>
    <!-- Date Filter -->
    <div class="col-md-4">
        <input type="date" id="dateFilter" class="form-control" onchange="filterAndSearchTable()">
    </div>
</div>
<div class="table-responsive">
    <table id="transactionsTable" class="table table-striped table-hover table-bordered align-middle shadow-sm">
        <thead class="table-dark">
            <tr>
                <th>Sender Acc No</th>
                <th>Receiver Acc No</th>
                <th>Transaction Type</th>
                <th>Amount</th>
                <th>Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="tx" items="${requestScope.transactionList}">
                <tr>
                    <td>${tx.senderAccountNumber}</td>
                    <td>${tx.receiverAccountNumber}</td>
                    <td>
                        <span class="badge 
                            ${tx.transactionType == 'CREDIT' ? 'bg-success' : 
                              tx.transactionType == 'TRANSFER' ? 'bg-primary' : 'bg-secondary'}">
                            ${tx.transactionType}
                        </span>
                    </td>
                    <td>₹ ${tx.amount}</td>
                    <td>${tx.transactionDate}</td>
                </tr>
            </c:forEach>
            <tr id="noResultsRow" style="display:none;">
                <td colspan="5" class="text-center text-danger">No results found.</td>
            </tr>
        </tbody>
    </table>
</div>
<script>
function filterAndSearchTable() {
    const searchInput = document.getElementById('searchInput').value.toUpperCase();
    const typeFilter = document.getElementById('typeFilter').value;
    const dateFilter = document.getElementById('dateFilter').value; // yyyy-mm-dd or ""

    const table = document.getElementById('transactionsTable');
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const noResultsRow = document.getElementById('noResultsRow');

    let visibleRowCount = 0;
    for (let i = 0; i < rows.length; i++) {
        if (rows[i].id === 'noResultsRow') continue;

        const senderCell = rows[i].getElementsByTagName('td')[0];
        const receiverCell = rows[i].getElementsByTagName('td')[1];
        const typeCell = rows[i].getElementsByTagName('td')[2];
        const dateCell = rows[i].getElementsByTagName('td')[4];

        if (senderCell && receiverCell && typeCell && dateCell) {
            const senderText = senderCell.textContent || senderCell.innerText;
            const receiverText = receiverCell.textContent || receiverCell.innerText;
            const typeText = (typeCell.querySelector('.badge').textContent || typeCell.querySelector('.badge').innerText).trim();
            const dateText = dateCell.textContent || dateCell.innerText; // e.g., 2025-08-21 16:24:21.0

            // Extract only the date part (YYYY-MM-DD)
            const rowDate = dateText.substring(0, 10);

            const matchesSearch = senderText.toUpperCase().indexOf(searchInput) > -1 || 
                                  receiverText.toUpperCase().indexOf(searchInput) > -1;
            const matchesType = typeFilter === 'ALL' || typeText === typeFilter;
            const matchesDate = !dateFilter || rowDate === dateFilter;

            if (matchesSearch && matchesType && matchesDate) {
                rows[i].style.display = "";
                visibleRowCount++;
            } else {
                rows[i].style.display = "none";
            }
        }
    }
    if (visibleRowCount === 0) {
        noResultsRow.style.display = "";
    } else {
        noResultsRow.style.display = "none";
    }
}
</script>