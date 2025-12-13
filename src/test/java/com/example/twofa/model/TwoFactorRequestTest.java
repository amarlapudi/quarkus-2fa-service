package com.example.twofa.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TwoFactorRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("192.168.1.100");

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullUsername() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(null);
        request.setIpAddress("192.168.1.100");

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        // Both @NotNull and @NotBlank may trigger, so check for at least 1
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("username")));
    }

    @Test
    void testBlankUsername() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("");
        request.setIpAddress("192.168.1.100");

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("username")));
    }

    @Test
    void testNullIpAddress() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress(null);

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("ipAddress")));
    }

    @Test
    void testInvalidIpAddress() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("invalid-ip");

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("ipAddress")));
    }

    @Test
    void testValidIpv4Address() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("10.0.0.1");

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidIpv6Address() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334");

        // When
        Set<ConstraintViolation<TwoFactorRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();

        // When
        request.setUsername("testuser");
        request.setIpAddress("192.168.1.100");

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("192.168.1.100", request.getIpAddress());
    }
}

