package com.bankingapp.dao;

import com.bankingapp.model.Transaction;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransactionDao {

    private DataSource dataSource;

    public TransactionDao() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/banking-source");
        } catch (Exception e) {
            throw new RuntimeException("Error initializing DataSource", e);
        }
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (sender_account_number, receiver_account_number, transaction_type, amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaction.getSenderAccountNumber());
            stmt.setString(2, transaction.getReceiverAccountNumber());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.executeUpdate();
        }
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE sender_account_number = ? OR receiver_account_number = ? ORDER BY transaction_date DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    
                    transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("sender_account_number"),
                        rs.getString("receiver_account_number"),
                        rs.getString("transaction_type"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("transaction_date")
                    ));
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                
                transactions.add(new Transaction(
                    rs.getInt("id"),
                    rs.getString("sender_account_number"),
                    rs.getString("receiver_account_number"),
                    rs.getString("transaction_type"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("transaction_date")
                ));
            }
        }
        return transactions;
    }
    public int getTotalTransactionCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
 // This will return a map, e.g., { "TRANSFER": 50, "CREDIT": 15 }
    public Map<String, Integer> getTransactionTypeCounts() throws SQLException {
        // Use the simple 'Map' and 'HashMap' names
        Map<String, Integer> typeCounts = new HashMap<>();
        String sql = "SELECT transaction_type, COUNT(*) as count FROM transactions GROUP BY transaction_type";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                typeCounts.put(rs.getString("transaction_type"), rs.getInt("count"));
            }
        }
        return typeCounts;
    }
    public Map<String, Integer> getDailyTransactionCountForLast7Days() throws SQLException {
        Map<String, Integer> dailyCounts = new LinkedHashMap<>();
        String sql = "SELECT DATE(transaction_date) as day, COUNT(*) as count " +
                     "FROM transactions " +
                     "WHERE transaction_date >= CURDATE() - INTERVAL 6 DAY " +
                     "GROUP BY day " +
                     "ORDER BY day ASC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dailyCounts.put(rs.getString("day"), rs.getInt("count"));
            }
        }
        return dailyCounts;
    }
    public List<Transaction> getRecentTransactions(String accountNumber) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        // The SQL is very similar to getTransactionHistory, but with "LIMIT 5"
        String sql = "SELECT * FROM transactions " +
                     "WHERE sender_account_number = ? OR receiver_account_number = ? " +
                     "ORDER BY transaction_date DESC " +
                     "LIMIT 5";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Use the full constructor to create the transaction object
                    transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("sender_account_number"),
                        rs.getString("receiver_account_number"),
                        rs.getString("transaction_type"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("transaction_date")
                    ));
                }
            }
        }
        return transactions;
    }
    
    public int getTransactionCountForUser(String accountNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions WHERE sender_account_number = ? OR receiver_account_number = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Map<String, BigDecimal> getCreditAndTransferTotals(String accountNumber) throws SQLException {
        Map<String, BigDecimal> totals = new java.util.HashMap<>();
        String sql = "SELECT " +
                     "SUM(CASE WHEN transaction_type = 'CREDIT' THEN amount ELSE 0 END) as total_credits, " +
                     "SUM(CASE WHEN transaction_type = 'TRANSFER' AND sender_account_number = ? THEN amount ELSE 0 END) as total_transfers " +
                     "FROM transactions WHERE sender_account_number = ? OR receiver_account_number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            stmt.setString(3, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totals.put("CREDIT", rs.getBigDecimal("total_credits"));
                    totals.put("TRANSFER", rs.getBigDecimal("total_transfers"));
                }
            }
        }
        return totals;
    }

    public Map<String, Integer> getMonthlyActivity(String accountNumber) throws SQLException {
        Map<String, Integer> dailyCounts = new LinkedHashMap<>();
        String sql = "SELECT DATE(transaction_date) as day, COUNT(*) as count FROM transactions " +
                     "WHERE (sender_account_number = ? OR receiver_account_number = ?) AND transaction_date >= CURDATE() - INTERVAL 29 DAY " +
                     "GROUP BY day ORDER BY day ASC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dailyCounts.put(rs.getString("day"), rs.getInt("count"));
                }
            }
        }
        return dailyCounts;
    }
}