package com.nt.exception;

public class LeadAlreadyExistsException extends RuntimeException {
    public LeadAlreadyExistsException(String message) {
        super(message);
    }
}
