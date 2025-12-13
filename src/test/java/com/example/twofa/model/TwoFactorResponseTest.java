package com.example.twofa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoFactorResponseTest {

    @Test
    void testDefaultConstructor() {
        // When
        TwoFactorResponse response = new TwoFactorResponse();

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    void testParameterizedConstructor() {
        // When
        TwoFactorResponse response = new TwoFactorResponse(true, "Authentication successful");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Authentication successful", response.getMessage());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        TwoFactorResponse response = new TwoFactorResponse();

        // When
        response.setSuccess(true);
        response.setMessage("Test message");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
    }

    @Test
    void testSuccessResponse() {
        // When
        TwoFactorResponse response = new TwoFactorResponse(true, "Success");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
    }

    @Test
    void testFailureResponse() {
        // When
        TwoFactorResponse response = new TwoFactorResponse(false, "Failed");

        // Then
        assertFalse(response.isSuccess());
        assertEquals("Failed", response.getMessage());
    }
}

