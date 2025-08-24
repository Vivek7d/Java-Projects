package com.bankingapp.service;

import com.bankingapp.dao.TransactionDao;
import com.bankingapp.dao.UserDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DashboardService {

    private final UserDao userDao = new UserDao();
    private final TransactionDao transactionDao = new TransactionDao();

    public Map<String, Object> getDashboardInsights() throws SQLException {
        Map<String, Object> insights = new HashMap<>();

       
        insights.put("totalCustomers", userDao.getTotalCustomerCount());
        insights.put("totalTransactions", transactionDao.getTotalTransactionCount());
        insights.put("totalBalance", userDao.getTotalBankBalance());
        insights.put("userStatusCounts", userDao.getUserStatusCounts());
        insights.put("transactionTypeCounts", transactionDao.getTransactionTypeCounts());
        insights.put("dailyTransactionCounts", transactionDao.getDailyTransactionCountForLast7Days());
        insights.put("top5Customers", userDao.getTop5CustomersByBalance());
        
        return insights;
    }

    public Map<String, Object> getCustomerDashboardInsights(String accountNumber) throws SQLException {
        Map<String, Object> insights = new HashMap<>();

       
        insights.put("totalUserTransactions", transactionDao.getTransactionCountForUser(accountNumber));
        
       
        Map<String, BigDecimal> creditAndTransferTotals = transactionDao.getCreditAndTransferTotals(accountNumber);
        insights.put("totalCredited", creditAndTransferTotals.getOrDefault("CREDIT", BigDecimal.ZERO));
        insights.put("totalTransferred", creditAndTransferTotals.getOrDefault("TRANSFER", BigDecimal.ZERO));
        
        
        insights.put("monthlyActivity", transactionDao.getMonthlyActivity(accountNumber));

        return insights;
    }
}