package com.company.fintech.exception;

public class InvalidBalanceException extends RuntimeException{
    public InvalidBalanceException() {
    }

    public InvalidBalanceException(String message) {
        super(message);
    }
}
