package com.bankingapp.exception;

public class AccountDeactivatedException extends Exception {
    private static final long serialVersionUID = 1L;

    public AccountDeactivatedException(String message) {
        super(message);
    }
}