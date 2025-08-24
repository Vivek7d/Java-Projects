<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3 class="mb-4 text-primary d-flex align-items-center" style="font-weight:700;">
    <i class="bi bi-journal-text me-2"></i>Transaction History (Passbook)
</h3>
<div class="card p-3 mb-2" style="border-radius: 16px;">
    <div class="row g-2 align-items-end mb-3">
        <div class="col-md-4">
            <label for="searchInput" class="form-label mb-1">Search</label>
            <input type="text" id="searchInput" class="form-control" placeholder="Search account, amount...">
        </div>
        <div class="col-md-3">
            <label for="typeFilter" class="form-label mb-1">Transaction Type</label>
            <select id="typeFilter" class="form-select">
                <option value="ALL">All Types</option>
                <option value="CREDIT">Credit</option>
                <option value="TRANSFER">Transfer/Debit</option>
            </select>
        </div>
        <div class="col-md-2">
            <label for="fromDate" class="form-label mb-1">From</label>
            <input type="date" id="fromDate" class="form-control">
        </div>
        <div class="col-md-2">
            <label for="toDate" class="form-label mb-1">To</label>
            <input type="date" id="toDate" class="form-control">
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle" id="passbookTable">
            <thead class="table-dark">
                <tr>
                    <th>#</th>
                    <th>Sender Acc No</th>
                    <th>Receiver Acc No</th>
                    <th>Type</th>
                    <th>Amount (₹)</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody id="tableBody">
                <c:forEach var="tx" items="${requestScope.transactionHistory}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${tx.senderAccountNumber}</td>
                        <td>${tx.receiverAccountNumber}</td>
                        <td>
                            <span class="badge 
                                ${tx.transactionType == 'CREDIT' ? 'bg-success' : 'bg-danger'}">
                                ${tx.transactionType}
                            </span>
                        </td>
                        <td><strong>₹${tx.amount}</strong></td>
                        <td>${tx.transactionDate}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div id="noResultsRow" class="text-center py-3 text-muted" style="display: none;">
            <i class="bi bi-exclamation-circle me-2"></i>No transactions found.
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const typeFilter = document.getElementById('typeFilter');
    const fromDate = document.getElementById('fromDate');
    const toDate = document.getElementById('toDate');
    const table = document.getElementById('passbookTable');
    const rows = Array.from(table.getElementsByTagName('tbody')[0].getElementsByTagName('tr'));
    const noResultsRow = document.getElementById('noResultsRow');

    function filterTable() {
        const searchVal = (searchInput.value || '').trim().toLowerCase();
        const typeVal = typeFilter.value;
        const fromVal = fromDate.value;
        const toVal = toDate.value;
        let visibleRows = 0;

        rows.forEach(row => {
            let cells = row.getElementsByTagName('td');
            const sender = cells[1].innerText.toLowerCase();
            const receiver = cells[2].innerText.toLowerCase();
            const type = cells[3].innerText.toUpperCase();
            const amount = cells[4].innerText.toLowerCase();
            const date = cells[5].innerText.substring(0,10); // date in yyyy-mm-dd

            // Filter by search (matches any cell except index and date)
            const matchesSearch = 
                sender.includes(searchVal) ||
                receiver.includes(searchVal) ||
                amount.includes(searchVal);

            const matchesType =
                (typeVal === 'ALL') ||
                (typeVal === 'CREDIT' && type.includes('CREDIT')) ||
                (typeVal === 'TRANSFER' && type.includes('TRANSFER'));

            // Filter by date
            let matchesDate = true;
            if(fromVal) matchesDate = date >= fromVal;
            if(matchesDate && toVal) matchesDate = date <= toVal;

            if (matchesSearch && matchesType && matchesDate) {
                row.style.display = '';
                visibleRows++;
            } else {
                row.style.display = 'none';
            }
        });
        if (visibleRows === 0) {
            noResultsRow.style.display = 'block';
        } else {
            noResultsRow.style.display = 'none';
        }
    }

    searchInput.addEventListener('input', filterTable);
    typeFilter.addEventListener('change', filterTable);
    fromDate.addEventListener('change', filterTable);
    toDate.addEventListener('change', filterTable);
});
</script>