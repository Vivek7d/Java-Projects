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

		if (sender == null || !sender.isActive() || receiver == null || !receiver.isActive()) {
			System.err.println("Transaction failed: Sender or Receiver account does not exist or is inactive.");
			return false;
		}

		if (sender.getBalance().compareTo(amount) < 0) {
			return false;
		}

		BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
		BigDecimal newReceiverBalance = receiver.getBalance().add(amount);

		userDao.updateBalance(senderAccNo, newSenderBalance);
		userDao.updateBalance(receiverAccNo, newReceiverBalance);

		Transaction transaction = new Transaction(senderAccNo, receiverAccNo, "TRANSFER", amount);
		transactionDao.addTransaction(transaction);
		return true;
	}

	public boolean performCredit(String userAccNo, BigDecimal amount) throws SQLException {
		User user = userDao.getUserByAccountNumber(userAccNo);

		if (user == null || !user.isActive()) {
			return false;
		}

		BigDecimal newBalance = user.getBalance().add(amount);
		userDao.updateBalance(userAccNo, newBalance);

		Transaction transaction = new Transaction(userAccNo, userAccNo, "CREDIT", amount);
		transactionDao.addTransaction(transaction);

		return true;
	}

	public List<Transaction> getTransactionHistory(String accountNumber) throws SQLException {
		return transactionDao.getTransactionsByAccountNumber(accountNumber);
	}

	public List<Transaction> getAllTransactions() throws SQLException {
		return transactionDao.getAllTransactions();
	}

	public List<Transaction> getRecentTransactions(String accountNumber) throws SQLException {
		return transactionDao.getRecentTransactions(accountNumber);
	}
}