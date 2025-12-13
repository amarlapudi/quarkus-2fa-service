package com.example.twofa.exception;

/**
 * Base exception class for all 2FA service exceptions.
 */
public class TwoFactorException extends RuntimeException {

    public TwoFactorException(String message) {
        super(message);
    }

    public TwoFactorException(String message, Throwable cause) {
        super(message, cause);
    }
}

