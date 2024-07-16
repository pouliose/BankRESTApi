package com.company.fintech.exception;

public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException() {
    }

    public InvalidAccountException(String message) {
        super(message);
    }
}
