package com.example.twofa.exception;

/**
 * Exception thrown when an error occurs communicating with external SOAP client.
 * Maps to HTTP 502 Bad Gateway or 503 Service Unavailable.
 */
public class ClientException extends TwoFactorException {

    private final int httpStatus;

    public ClientException(String message) {
        super(message);
        this.httpStatus = 502; // Bad Gateway default
    }

    public ClientException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = 502; // Bad Gateway default
    }

    public ClientException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

