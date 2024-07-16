package com.company.fintech.exception;

public class InvalidCurrencyException extends RuntimeException {
    public InvalidCurrencyException() {
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
