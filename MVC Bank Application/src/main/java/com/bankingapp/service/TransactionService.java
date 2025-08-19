package com.bankingapp.service;

import com.bankingapp.dao.TransactionDao;
import com.bankingapp.dao.UserDao;
import com.bankingapp.model.Transaction;
import com.bankingapp.model.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private TransactionDao transactionDao = new TransactionDao();
    private UserDao userDao = new UserDao();

    public boolean performTransfer(String senderAccNo, String receiverAccNo, BigDecimal amount) throws SQLException {
        User sender = userDao.getUserByAccountNumber(senderAccNo);
        User receiver = userDao.getUserByAccountNumber(receiverAccNo);

        if (sender == null || receiver == null) {
            return false; // One of the accounts does not exist
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            return false; // Insufficient funds
        }

        // Perform the transaction
        BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
        BigDecimal newReceiverBalance = receiver.getBalance().add(amount);

        userDao.updateBalance(senderAccNo, newSenderBalance);
        userDao.updateBalance(receiverAccNo, newReceiverBalance);

        // Log the transaction
        Transaction transaction = new Transaction();
        transaction.setSenderAccountNumber(senderAccNo);
        transaction.setReceiverAccountNumber(receiverAccNo);
        transaction.setAmount(amount);
        transaction.setTransactionType("TRANSFER");
        transactionDao.addTransaction(transaction);
        
        return true;
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) throws SQLException {
        return transactionDao.getTransactionsByAccountNumber(accountNumber);
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDao.getAllTransactions();
    }
}