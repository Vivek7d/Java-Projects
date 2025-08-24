<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    /* This style is to control the chart size */
    .chart-container {
        position: relative;
        height: 280px;
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>

<!-- KPI Cards -->
<div class="row g-3 mb-4">
    <div class="col-md-4">
        <div class="card text-white bg-primary shadow-sm h-100">
            <div class="card-body">
                <h6 class="card-title"><i class="bi bi-list-ol me-2"></i>Your Transactions</h6>
                <p class="card-text fs-3 fw-bold">${insights.totalUserTransactions}</p>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-success shadow-sm h-100">
            <div class="card-body">
                <h6 class="card-title"><i class="bi bi-arrow-down-circle me-2"></i>Total Credited</h6>
                <p class="card-text fs-3 fw-bold">₹${insights.totalCredited != null ? insights.totalCredited : '0.00'}</p>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-danger shadow-sm h-100">
            <div class="card-body">
                <h6 class="card-title"><i class="bi bi-arrow-up-circle me-2"></i>Total Spent</h6>
                <p class="card-text fs-3 fw-bold">₹${insights.totalTransferred != null ? insights.totalTransferred : '0.00'}</p>
            </div>
        </div>
    </div>
</div>

<!-- Charts -->
<div class="row g-3">
    <div class="col-lg-5">
        <h5 class="mb-3 text-secondary">Credits vs. Spending</h5>
        <div class="chart-container">
            <canvas id="spendingChart"></canvas>
        </div>
    </div>
    <div class="col-lg-7">
        <h5 class="mb-3 text-secondary">Activity (Last 30 Days)</h5>
        <div class="chart-container">
            <canvas id="monthlyActivityChart"></canvas>
        </div>
    </div>
</div>