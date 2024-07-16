package com.company.fintech.exception;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
