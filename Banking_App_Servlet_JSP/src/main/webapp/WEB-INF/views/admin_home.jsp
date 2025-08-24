<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
body.d-flex {
	height: 100vh;
	overflow: hidden;
}

.toast-notification {
	position: fixed;
	top: 20px;
	right: 20px;
	color: white;
	padding: 10px 14px;
	border-radius: 6px;
	z-index: 1050;
	font-family: sans-serif;
	font-size: 15px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
	display: none;
	opacity: 0;
	transition: opacity 0.5s ease-in-out;
	display: flex;
	align-items: center;
	gap: 8px;
}

.toast-notification.show {
	display: flex;
	opacity: 1;
}

.toast-notification.success {
	background-color: #28a745;
}

.toast-notification.error {
	background-color: #dc3545; /* red */
}
</style>
</head>

<body class="d-flex">
	<div id="toast" class="toast-notification"></div>

	<div class="d-flex flex-column flex-shrink-0 p-3 bg-primary text-white"
		style="width: 250px; height: 100vh;">
		<%-- ... sidebar links ... --%>
		<a href="/"
			class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
			<i class="bi bi-bank2 me-2 fs-4"></i> <span class="fs-5 fw-bold">Bank
				Admin</span>
		</a>
		<hr class="border-light">
		<ul class="nav nav-pills flex-column mb-auto">
			
			<li class="nav-item"><a href="admin?action=showAddCustomer"
				class="nav-link ${requestScope.view == 'add_customer' ? 'active' : 'text-white'}">
					<i class="bi bi-person-plus me-2"></i>Add Customer
			</a></li>
			<li><a href="admin?action=listCustomers"
				class="nav-link ${requestScope.view == 'list_customers' ? 'active' : 'text-white'}">
					<i class="bi bi-people me-2"></i>View Customers
			</a></li>

			<li><a href="admin?action=listTransactions"
				class="nav-link ${requestScope.view == 'list_transactions' ? 'active' : 'text-white'}">
					<i class="bi bi-cash-stack me-2"></i>Transactions
			</a></li>
			<li class="nav-item"><a href="admin?action=showInsights"
				class="nav-link ${requestScope.view == 'insights' ? 'active' : 'text-white'}">
					<i class="bi bi-bar-chart-line-fill me-2"></i>Insights
			</a></li>
			<li class="nav-item"><a href="admin?action=showAiChat"
				class="nav-link ${requestScope.view == 'bank_ai' ? 'active' : 'text-white'}">
					<i class="bi bi-robot me-2"></i>Bank AI
			</a></li>
			<li><a href="auth?action=logout" class="nav-link text-white">
					<i class="bi bi-box-arrow-right me-2"></i>Logout
			</a></li>
		</ul>
	</div>

	<div class="flex-grow-1 p-4" style="overflow-y: auto;">

		<h2 class="mb-4">Welcome, ${sessionScope.user.firstName} 👋</h2>
		<c:if test="${requestScope.view == 'insights'}">
			<jsp:include page="insights.jsp" />
		</c:if>

		<c:if test="${requestScope.view == 'add_customer'}">
			<jsp:include page="add_customer.jsp" />
		</c:if>
		<c:if test="${requestScope.view == 'list_customers'}">
			<jsp:include page="view_customers.jsp" />
		</c:if>
		<c:if test="${requestScope.view == 'bank_ai'}">
			<jsp:include page="bank_ai.jsp" />
		</c:if>
		<c:if test="${requestScope.view == 'update_customer'}">
			<jsp:include page="update_customer_form.jsp" />
		</c:if>
		<c:if test="${requestScope.view == 'list_transactions'}">
			<jsp:include page="view_transactions_admin.jsp" />
		</c:if>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<c:if test="${not empty sessionScope.success_toast}">
		<script>
        (function() {
            const toastElement = document.getElementById('toast');
            const message = "${sessionScope.success_toast}";
            if (toastElement && message) {
                toastElement.innerHTML = '<i class="bi bi-check-circle-fill"></i>' + message;
                toastElement.classList.add('success');
                toastElement.classList.add('show');
                setTimeout(function() {
                    toastElement.classList.remove('show');
                }, 4000);
            }
        })();
    </script>
		<c:remove var="success_toast" scope="session" />
	</c:if>
	<c:if test="${not empty requestScope.error_toast}">
		<script>
        (function() {
            const toastElement = document.getElementById('toast');
            const message = "${requestScope.error_toast}";
            if (toastElement && message) {
                toastElement.innerHTML = '<i class="bi bi-x-circle-fill"></i>' + message;
                toastElement.classList.add('error');
                toastElement.classList.add('show');
                setTimeout(() => toastElement.classList.remove('show'), 4000);
            }
        })();
    </script>
	</c:if>
	<c:if test="${requestScope.view == 'insights'}">
		<script>

    // Chart 1: User Status (Pie Chart)
    const userStatusCtx = document.getElementById('userStatusChart').getContext('2d');
    new Chart(userStatusCtx, {
        type: 'pie',
        data: {
            labels: ['Active', 'Inactive'],
            datasets: [{
                label: 'Customer Status',
                data: [
                    ${insights.userStatusCounts['active'] != null ? insights.userStatusCounts['active'] : 0}, 
                    ${insights.userStatusCounts['inactive'] != null ? insights.userStatusCounts['inactive'] : 0}
                ],
                backgroundColor: ['rgba(54, 162, 235, 0.7)', 'rgba(255, 99, 132, 0.7)'],
                borderColor: ['rgba(54, 162, 235, 1)', 'rgba(255, 99, 132, 1)'],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, 
            plugins: { legend: { position: 'top' } }
        }
    });

    // Chart 2: Transaction Types (Doughnut Chart)
    const transactionTypeCtx = document.getElementById('transactionTypeChart').getContext('2d');
    new Chart(transactionTypeCtx, {
        type: 'doughnut',
        data: {
            labels: ['Transfers', 'Credits'],
            datasets: [{
                label: 'Transaction Types',
                data: [
                    ${insights.transactionTypeCounts['TRANSFER'] != null ? insights.transactionTypeCounts['TRANSFER'] : 0},
                    ${insights.transactionTypeCounts['CREDIT'] != null ? insights.transactionTypeCounts['CREDIT'] : 0}
                ],
                backgroundColor: ['rgba(255, 159, 64, 0.7)', 'rgba(75, 192, 192, 0.7)'],
                borderColor: ['rgba(255, 159, 64, 1)', 'rgba(75, 192, 192, 1)'],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, 
            plugins: { legend: { position: 'top' } }
        }
    });
    

    // Chart 3: Daily Transactions (Line Chart)
    const dailyTxCtx = document.getElementById('dailyTransactionsChart').getContext('2d');
    new Chart(dailyTxCtx, {
        type: 'line',
        data: {
            labels: [ // Extract keys (dates) from our map
                <c:forEach var="entry" items="${insights.dailyTransactionCounts}">
                    '${entry.key}',
                </c:forEach>
            ],
            datasets: [{
                label: 'Number of Transactions',
                data: [ // Extract values (counts) from our map
                    <c:forEach var="entry" items="${insights.dailyTransactionCounts}">
                        ${entry.value},
                    </c:forEach>
                ],
                fill: true,
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                tension: 0.4 // Makes the line smooth
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    // Chart 4: Top 5 Customers (Horizontal Bar Chart)
    const topCustomersCtx = document.getElementById('topCustomersChart').getContext('2d');
    new Chart(topCustomersCtx, {
        type: 'bar', // 'bar' for vertical, we'll make it horizontal in options
        data: {
            labels: [ // Create labels from first and last names
                <c:forEach var="customer" items="${insights.top5Customers}">
                    '${customer.firstName} ${customer.lastName}',
                </c:forEach>
            ],
            datasets: [{
                label: 'Account Balance (₹)',
                data: [ // Get the balance for each customer
                    <c:forEach var="customer" items="${insights.top5Customers}">
                        ${customer.balance},
                    </c:forEach>
                ],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(153, 102, 255, 0.7)'
                ]
            }]
        },
        options: {
            indexAxis: 'y', // This is what makes the bar chart horizontal!
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false // Hide legend as the axis label is clear enough
                }
            }
        }
    });

</script>
	</c:if>
</body>
</html>