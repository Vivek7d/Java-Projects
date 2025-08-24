<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .chart-container {
        position: relative;
        height: 280px;
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>

<div class="row mb-4">
    <div class="col-md-4">
        <div class="card text-white bg-info shadow">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-people-fill me-2"></i>Total Customers</h5>
                <p class="card-text fs-2 fw-bold">${insights.totalCustomers}</p>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-warning shadow">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-arrow-left-right me-2"></i>Total Transactions</h5>
                <p class="card-text fs-2 fw-bold">${insights.totalTransactions}</p>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-success shadow">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-wallet2 me-2"></i>Total Bank Balance</h5>
                <p class="card-text fs-2 fw-bold">₹${insights.totalBalance}</p>
            </div>
        </div>
    </div>
</div>

<%-- Chart HTML (This part is correct) --%>
<div class="row">
    <div class="col-md-6">
        <div class="card shadow">
            <div class="card-body">
                <h5 class="card-title">Customer Status</h5>
                <div class="chart-container">
                    <canvas id="userStatusChart"></canvas>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow">
            <div class="card-body">
                <h5 class="card-title">Transaction Types</h5>
                <div class="chart-container">
                    <canvas id="transactionTypeChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row mt-4">
    <div class="col-md-12">
        <div class="card shadow">
            <div class="card-body">
                <h5 class="card-title">Transaction Volume (Last 7 Days)</h5>
                <div class="chart-container">
                    <canvas id="dailyTransactionsChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row mt-4">
    <div class="col-md-12">
        <div class="card shadow">
            <div class="card-body">
                <h5 class="card-title">Top 5 Customers by Balance</h5>
                <div class="chart-container">
                    <canvas id="topCustomersChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

