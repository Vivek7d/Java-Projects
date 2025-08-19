package com.bankingapp.dao;

import com.bankingapp.model.Transaction;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    Transaction tx = new Transaction();
                    tx.setId(rs.getInt("id"));
                    tx.setSenderAccountNumber(rs.getString("sender_account_number"));
                    tx.setReceiverAccountNumber(rs.getString("receiver_account_number"));
                    tx.setTransactionType(rs.getString("transaction_type"));
                    tx.setAmount(rs.getBigDecimal("amount"));
                    tx.setTransactionDate(rs.getTimestamp("transaction_date"));
                    transactions.add(tx);
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
                Transaction tx = new Transaction();
                // ... (populate transaction object like above)
                tx.setId(rs.getInt("id"));
                tx.setSenderAccountNumber(rs.getString("sender_account_number"));
                tx.setReceiverAccountNumber(rs.getString("receiver_account_number"));
                tx.setTransactionType(rs.getString("transaction_type"));
                tx.setAmount(rs.getBigDecimal("amount"));
                tx.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(tx);
            }
        }
        return transactions;
    }
}