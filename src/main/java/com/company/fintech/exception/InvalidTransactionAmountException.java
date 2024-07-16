package com.company.fintech.exception;

public class InvalidTransactionAmountException extends RuntimeException{

    public InvalidTransactionAmountException() {
    }

    public InvalidTransactionAmountException(String message) {
        super(message);
    }
}
