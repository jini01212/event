package com.example.eventplatform.exception;

public class SoldOutException extends RuntimeException {
    public SoldOutException(String message) {
        super(message);
    }
}
