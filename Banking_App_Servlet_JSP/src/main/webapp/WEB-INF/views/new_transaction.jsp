<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="toast" class="toast-notification"></div>

<div class="row">
    <div class="col-lg-7 col-md-12">
        <div class="transaction-form-wrapper">
            <h3 class="mb-3 text-primary d-flex align-items-center" style="font-weight: 700;">
                <i class="bi bi-arrow-left-right me-2"></i>New Transaction
            </h3>
            <form action="customer" method="post" autocomplete="off" class="needs-validation" novalidate>
                <input type="hidden" name="action" value="processTransaction">

                <div class="mb-3">
                    <label for="type" class="form-label">Type of Transaction</label>
                    <select id="type" name="type" class="form-select" onchange="toggleReceiverField()" required>
                        <option value="TRANSFER">Transfer</option>
                        <option value="CREDIT">Credit</option>
                    </select>
                </div>
                
                <div class="mb-3" id="receiver-account-container">
                    <label for="receiverAccNo" class="form-label">To Account Number</label>
                    <input type="text" id="receiverAccNo" name="receiverAccNo" class="form-control" placeholder="Enter account number" required>
                </div>

                <div class="mb-4">
                    <label for="amount" class="form-label">Amount</label>
                    <input type="number" step="0.01" id="amount" name="amount" class="form-control" min="1.00" placeholder="Enter amount" required>
                </div>
                
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-send me-1"></i>Submit
                    </button>
                </div>
            </form>
        </div>
    </div>
    <!-- Right: Recent Transactions Panel -->
    <div class="col-lg-5 d-none d-lg-block">
        <div class="card shadow-sm mb-4" style="border-radius: 18px;">
            <div class="card-body">
                <h5 class="card-title mb-3"><i class="bi bi-clock-history me-2"></i>Recent Transactions</h5>
                <ul class="list-group list-group-flush">
                    <c:forEach var="tx" items="${requestScope.recentTransactions}">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span>
                                <i class="bi bi-arrow-left-right me-2"></i>
                                <strong>${tx.transactionType}</strong> ₹${tx.amount}
                            </span>
                            <span class="text-muted small">${tx.transactionDate}</span>
                        </li>
                    </c:forEach>
                    <c:if test="${empty requestScope.recentTransactions}">
                        <li class="list-group-item text-muted text-center">No recent transactions</li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>

<style>
.transaction-form-wrapper {
    background: none;
    box-shadow: none;
    border-radius: 0;
    max-width: 480px;
    margin-left: 20px;
    margin-top: 0;
    padding: 36px 28px 28px 28px;
}
@media (max-width: 900px) {
    .transaction-form-wrapper {
        margin-left: 0;
        padding: 24px 6vw;
        max-width: 100%;
    }
}
.toast-notification {
    position: fixed;
    top: 28px;
    right: 28px;
    color: white;
    padding: 14px 22px;
    border-radius: 12px;
    z-index: 1200;
    font-size: 1.08rem;
    box-shadow: 0 4px 18px rgba(0,0,0,0.17);
    display: flex;
    align-items: center;
    gap: 10px;
    opacity: 0;
    transition: opacity 0.5s;
}
.toast-notification.show {
    opacity: 1;
}
.toast-notification.error {
    background: linear-gradient(90deg, #dc3545 80%, #e75480 100%);
}
</style>
<script>
    function toggleReceiverField() {
        const typeDropdown = document.getElementById('type');
        const receiverContainer = document.getElementById('receiver-account-container');
        const receiverInput = document.getElementById('receiverAccNo');
        if (typeDropdown.value === 'CREDIT') {
            receiverContainer.style.display = 'none';
            receiverInput.required = false;
        } else {
            receiverContainer.style.display = '';
            receiverInput.required = true;
        }
    }
    document.addEventListener('DOMContentLoaded', toggleReceiverField);
</script>
<c:if test="${not empty error}">
    <script>
        (function() {
            const toastElement = document.getElementById('toast');
            const message = "${error}";
            if (toastElement && message) {
                toastElement.innerHTML = '<i class="bi bi-x-circle-fill"></i> ' + message;
                toastElement.classList.add('error');
                toastElement.classList.add('show');
                setTimeout(function() {
                    toastElement.classList.remove('show');
                }, 4000);
            }
        })();
    </script>
</c:if>