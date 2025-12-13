package com.example.twofa.model.error;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testDefaultConstructor() {
        // When
        ErrorResponse errorResponse = new ErrorResponse();

        // Then
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
        assertThat(errorResponse.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testParameterizedConstructor() {
        // When
        ErrorResponse errorResponse = new ErrorResponse(400, "Validation Error", "Invalid request", "/test");

        // Then
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("Invalid request", errorResponse.getMessage());
        assertEquals("/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testParameterizedConstructorWithDetails() {
        // Given
        List<String> details = Arrays.asList("Error 1", "Error 2");

        // When
        ErrorResponse errorResponse = new ErrorResponse(400, "Validation Error", "Invalid request", "/test", details);

        // Then
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("Invalid request", errorResponse.getMessage());
        assertEquals("/test", errorResponse.getPath());
        assertNotNull(errorResponse.getDetails());
        assertEquals(2, errorResponse.getDetails().size());
        assertTrue(errorResponse.getDetails().contains("Error 1"));
        assertTrue(errorResponse.getDetails().contains("Error 2"));
    }

    @Test
    void testGettersAndSetters() {
        // Given
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime timestamp = LocalDateTime.now();
        List<String> details = Arrays.asList("Detail 1", "Detail 2");

        // When
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("An error occurred");
        errorResponse.setPath("/api/test");
        errorResponse.setDetails(details);

        // Then
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An error occurred", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void testNullDetails() {
        // When
        ErrorResponse errorResponse = new ErrorResponse(400, "Error", "Message", "/path");

        // Then
        assertNull(errorResponse.getDetails());
    }
}

