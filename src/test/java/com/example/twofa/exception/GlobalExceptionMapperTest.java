package com.example.twofa.exception;

import com.example.twofa.model.error.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionMapperTest {

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private GlobalExceptionMapper exceptionMapper;

    @BeforeEach
    void setUp() {
        when(uriInfo.getPath()).thenReturn("/2fa/authenticate");
    }

    @Test
    void testValidationExceptionMapping() {
        // Given
        ValidationException exception = new ValidationException("Validation failed");

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(400, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("Validation failed", errorResponse.getMessage());
        assertEquals("/2fa/authenticate", errorResponse.getPath());
    }

    @Test
    void testValidationExceptionWithDetails() {
        // Given
        List<String> errors = List.of("Username is required", "IP address is invalid");
        ValidationException exception = new ValidationException("Validation failed", errors);

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(400, errorResponse.getStatus());
        assertNotNull(errorResponse.getDetails());
        assertEquals(2, errorResponse.getDetails().size());
        assertTrue(errorResponse.getDetails().contains("Username is required"));
        assertTrue(errorResponse.getDetails().contains("IP address is invalid"));
    }

    @Test
    void testClientExceptionMappingWithDefaultStatus() {
        // Given
        ClientException exception = new ClientException("Service unavailable");

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(502, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(502, errorResponse.getStatus());
        assertEquals("Bad Gateway", errorResponse.getError());
        assertEquals("Service unavailable", errorResponse.getMessage());
    }

    @Test
    void testClientExceptionMappingWith503Status() {
        // Given
        ClientException exception = new ClientException("Service unavailable", 503);

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(503, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(503, errorResponse.getStatus());
        assertEquals("Service Unavailable", errorResponse.getError());
    }

    @Test
    void testServiceExceptionMapping() {
        // Given
        ServiceException exception = new ServiceException("Internal error");

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(500, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An internal server error occurred", errorResponse.getMessage());
    }

    @Test
    void testConstraintViolationExceptionMapping() {
        // Given
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException exception = new ConstraintViolationException("Validation failed", violations);

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("Request validation failed", errorResponse.getMessage());
    }

    @Test
    void testWebApplicationExceptionMapping() {
        // Given
        WebApplicationException exception = new WebApplicationException("Not found", 404);

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(404, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(404, errorResponse.getStatus());
        assertEquals("HTTP 404", errorResponse.getError());
    }

    @Test
    void testGenericExceptionMapping() {
        // Given
        RuntimeException exception = new RuntimeException("Unexpected error");

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(500, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
    }

    @Test
    void testNullPointerExceptionMapping() {
        // Given
        NullPointerException exception = new NullPointerException("Null value");

        // When
        Response response = exceptionMapper.toResponse(exception);

        // Then
        assertEquals(500, response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
    }
}

