package com.example.twofa.util;

import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import com.example.twofa.model.error.ErrorResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Factory class for creating test data objects.
 */
public class TestDataFactory {

    public static TwoFactorRequest createValidTwoFactorRequest() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("192.168.1.100");
        return request;
    }

    public static TwoFactorRequest createInvalidTwoFactorRequest() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(""); // Invalid: blank username
        request.setIpAddress("invalid-ip"); // Invalid: not a valid IP
        return request;
    }

    public static TwoFactorRequest createTwoFactorRequest(String username, String ipAddress) {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(username);
        request.setIpAddress(ipAddress);
        return request;
    }

    public static TwoFactorRequest createTwoFactorRequestWithNullUsername() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(null);
        request.setIpAddress("192.168.1.100");
        return request;
    }

    public static TwoFactorRequest createTwoFactorRequestWithNullIpAddress() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress(null);
        return request;
    }

    public static TwoFactorResponse createTwoFactorResponse(boolean success) {
        return new TwoFactorResponse(success, success ? "Authentication successful" : "Authentication failed");
    }

    public static TwoFactorResponse createTwoFactorResponse(boolean success, String message) {
        return new TwoFactorResponse(success, message);
    }

    public static ErrorResponse createErrorResponse(int status, String error, String message, String path) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status);
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        errorResponse.setPath(path);
        return errorResponse;
    }

    public static ErrorResponse createErrorResponseWithDetails(int status, String error, String message, 
                                                               String path, List<String> details) {
        ErrorResponse errorResponse = createErrorResponse(status, error, message, path);
        errorResponse.setDetails(details);
        return errorResponse;
    }

    public static ErrorResponse createValidationErrorResponse(String path, String... errors) {
        return createErrorResponseWithDetails(
            400,
            "Validation Error",
            "Request validation failed",
            path,
            Arrays.asList(errors)
        );
    }
}

