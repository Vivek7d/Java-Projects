<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Customer Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
html, body {
	height: 100%;
	min-height: 100%;
	margin: 0;
	padding: 0;
}

body {
	background: linear-gradient(135deg, #e3f0ff 0%, #fafcff 100%);
	min-height: 100vh;
	height: 100vh;
}

body.d-flex {
	height: 100vh;
	overflow: hidden;
}

.dashboard-layout {
	display: flex;
	min-height: 100vh;
	height: 100vh;
}

.sidebar {
	width: 270px;
	background: #212529;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding-top: 32px;
	padding-bottom: 32px;
	box-shadow: 2px 0 12px 0 rgba(33, 37, 41, 0.07);
}

.sidebar .avatar {
	width: 90px;
	height: 90px;
	background: #fff;
	border-radius: 50%;
	box-shadow: 0 0 0 4px #0d6efd33;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 2.5rem;
	color: #0d6efd;
	margin-bottom: 16px;
}

.sidebar .customer-name {
	font-weight: 700;
	color: #fff;
	font-size: 1.28rem;
	text-align: center;
}

.sidebar .acc-no {
	font-size: 1rem;
	color: #adb5bd;
	margin-bottom: 28px;
	text-align: center;
}

.sidebar hr {
	border-top: 2px solid #343a40;
	width: 90%;
	margin: 0 auto 18px auto;
}

.sidebar .nav {
	width: 100%;
}

.sidebar .nav-link {
	color: #b0b8c1;
	border-radius: 10px;
	margin: 4px 10px;
	transition: all 0.15s;
	font-size: 1.09rem;
	padding: 0.70rem 1.2rem;
	font-weight: 500;
}

.sidebar .nav-link.active, .sidebar .nav-link:hover {
	background: linear-gradient(90deg, #0d6efd 70%, #1e90ff 100%);
	color: #fff !important;
}

.sidebar .nav-link.text-danger {
	color: #ff6b6b !important;
}

.main-content {
	flex-grow: 1;
	padding: 32px 32px 32px 32px;
	min-height: 100vh;
	display: flex;
	flex-direction: column;
}

.profile-card {
	background: #fff;
	border-radius: 18px;
	box-shadow: 0 2px 24px #0d6efd13;
	padding: 2.2rem 1.8rem 1.2rem 1.8rem;
	margin-bottom: 2rem;
	display: flex;
	align-items: center;
	gap: 24px;
}

.profile-avatar {
	width: 65px;
	height: 65px;
	background: #e7f1ff;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 2.5rem;
	color: #0d6efd;
	box-shadow: 0 2px 8px #0d6efd16;
}

.profile-info .name {
	font-weight: 700;
	font-size: 1.35rem;
	margin-bottom: 6px;
}

.profile-info .acc-number {
	font-size: 1.09rem;
	color: #0d6efd;
}

.profile-info .balance {
	font-size: 1.11rem;
	color: #198754;
	font-weight: 500;
}

.card-content {
	background: #fff;
	border-radius: 18px;
	box-shadow: 0 1px 6px #0d6efd0c;
	padding: 2rem 1.5rem;
	margin-top: 0;
}

.toast-notification {
	position: fixed;
	top: 24px;
	right: 24px;
	color: white;
	padding: 13px 20px;
	border-radius: 9px;
	z-index: 1200;
	font-size: 1.08rem;
	box-shadow: 0 4px 18px rgba(0, 0, 0, 0.17);
	display: none;
	opacity: 0;
	transition: opacity 0.5s;
	align-items: center;
	gap: 10px;
}

.toast-notification.show {
	display: flex;
	opacity: 1;
}

.toast-notification.success {
	background: linear-gradient(90deg, #28a745 80%, #51d88a 100%);
}

@media ( max-width : 991.98px) {
	.dashboard-layout {
		flex-direction: column;
	}
	.sidebar {
		width: 100%;
		min-height: unset;
		height: auto;
		flex-direction: row;
		padding: 12px 0;
		justify-content: flex-start;
	}
	.sidebar .avatar, .sidebar .customer-name, .sidebar .acc-no, .sidebar hr
		{
		display: none;
	}
	.sidebar .nav {
		flex-direction: row;
	}
	.sidebar .nav-link {
		margin: 0 6px;
		padding: 0.6rem 0.9rem;
	}
	.main-content {
		padding: 18px 5vw 24px 5vw;
	}
}
</style>
</head>
<body>
	<div class="dashboard-layout">
		<div class="sidebar">
			<div class="avatar">
				<i class="bi bi-person-circle"></i>
			</div>
			<div class="customer-name">${sessionScope.user.firstName}
				${sessionScope.user.lastName}</div>
			<div class="acc-no">A/C No: ${sessionScope.user.accountNumber}</div>
			<hr>
			<ul class="nav nav-pills flex-column mb-auto">
				<li class="nav-item"><a href="customer?action=dashboard"
					class="nav-link ${requestScope.view == 'dashboard' ? 'active' : ''}">
						<i class="bi bi-bar-chart me-2"></i>Customer Insights
				</a></li>
				<li class="nav-item"><a href="customer?action=passbook"
					class="nav-link ${requestScope.view == 'passbook' ? 'active' : ''}">
						<i class="bi bi-journal-text me-2"></i>Passbook
				</a></li>
				<li><a href="customer?action=showTransaction"
					class="nav-link ${requestScope.view == 'new_transaction' ? 'active' : ''}">
						<i class="bi bi-arrow-left-right me-2"></i>New Transaction
				</a></li>
				<li><a href="customer?action=showProfile"
					class="nav-link ${requestScope.view == 'edit_profile' ? 'active' : ''}">
						<i class="bi bi-person-lines-fill me-2"></i>Edit Profile
				</a></li>
				<li><a href="auth?action=logout" class="nav-link text-danger">
						<i class="bi bi-box-arrow-right me-2"></i>Logout
				</a></li>
			</ul>
		</div>
		<div class="main-content" style="overflow-y: auto;">
			<div class="profile-card">
				<div class="profile-avatar">
					<i class="bi bi-person-bounding-box"></i>
				</div>
				<div class="profile-info">
					<div class="name">Welcome, ${sessionScope.user.firstName} 👋</div>
					<div class="acc-number mb-1">
						<strong>Account Number:</strong>
						${sessionScope.user.accountNumber}
					</div>
					<div class="balance">
						<strong>Balance:</strong> ₹${sessionScope.user.balance}
					</div>
				</div>
			</div>
			<div class="card-content">
				<c:if test="${requestScope.view == 'dashboard'}">
					<jsp:include page="customer_insights.jsp" />
				</c:if>
				<c:if test="${requestScope.view == 'passbook'}">
					<jsp:include page="passbook.jsp" />
				</c:if>
				<c:if test="${requestScope.view == 'new_transaction'}">
					<jsp:include page="new_transaction.jsp" />
				</c:if>
				<c:if test="${requestScope.view == 'edit_profile'}">
					<jsp:include page="edit_profile.jsp" />
				</c:if>
			</div>
		</div>
	</div>
	<div id="toast" class="toast-notification"></div>
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
	<c:if test="${requestScope.view == 'dashboard'}">
		<script>
        // Chart 1: Spending vs Credits (Doughnut)
        const spendingCtx = document.getElementById('spendingChart').getContext('2d');
        new Chart(spendingCtx, {
            type: 'doughnut',
            data: {
                labels: ['Money Spent (Transfers)', 'Money Received (Credits)'],
                datasets: [{
                    data: [
                        ${insights.totalTransferred != null ? insights.totalTransferred : 0}, 
                        ${insights.totalCredited != null ? insights.totalCredited : 0}
                    ],
                    backgroundColor: ['rgba(255, 99, 132, 0.7)', 'rgba(75, 192, 192, 0.7)'],
                    borderColor: ['rgba(255, 99, 132, 1)', 'rgba(75, 192, 192, 1)'],
                    borderWidth: 1
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });

        // Chart 2: Monthly Activity (Bar)
        const monthlyCtx = document.getElementById('monthlyActivityChart').getContext('2d');
        new Chart(monthlyCtx, {
            type: 'bar',
            data: {
                labels: [<c:forEach var="entry" items="${insights.monthlyActivity}">'${entry.key}',</c:forEach>],
                datasets: [{
                    label: 'Transactions per Day',
                    data: [<c:forEach var="entry" items="${insights.monthlyActivity}">${entry.value},</c:forEach>],
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    </script>
	</c:if>
</body>
</html>