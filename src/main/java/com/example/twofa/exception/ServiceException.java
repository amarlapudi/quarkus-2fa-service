package com.example.twofa.exception;

/**
 * Exception thrown when an internal service error occurs.
 * Maps to HTTP 500 Internal Server Error.
 */
public class ServiceException extends TwoFactorException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

