package com.company.fintech.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
