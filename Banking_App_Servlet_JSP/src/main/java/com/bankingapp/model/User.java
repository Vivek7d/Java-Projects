package com.bankingapp.model;

import java.math.BigDecimal;

public class User {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String role;
	private String accountNumber;
	private BigDecimal balance;
	private boolean isActive;

	public User() {
	}

	public User(String firstName, String lastName, String email, String password, String role, String accountNumber,
			BigDecimal balance) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.isActive = true;
	}

	public User(int id, String firstName, String lastName, String email, String password, String role,
			String accountNumber, BigDecimal balance, boolean isActive) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.isActive = isActive;
	}

	public User(String firstName, String lastName, BigDecimal balance) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.balance = balance;
	}

	public User(int id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}