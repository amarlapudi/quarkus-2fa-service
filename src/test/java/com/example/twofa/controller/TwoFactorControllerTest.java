package com.example.twofa.controller;

import com.example.twofa.exception.ClientException;
import com.example.twofa.exception.ServiceException;
import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import com.example.twofa.service.TwoFactorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwoFactorControllerTest {

    @Mock
    private TwoFactorService twoFactorService;

    @InjectMocks
    private TwoFactorController controller;

    private TwoFactorRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new TwoFactorRequest();
        validRequest.setUsername("testuser");
        validRequest.setIpAddress("192.168.1.100");
    }

    @Test
    void testSuccessfulAuthentication() {
        // Given
        TwoFactorResponse expectedResponse = new TwoFactorResponse(true, "Authentication successful");
        when(twoFactorService.authenticate(any(TwoFactorRequest.class))).thenReturn(expectedResponse);

        // When
        Response response = controller.authenticate(validRequest);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isInstanceOf(TwoFactorResponse.class);
        TwoFactorResponse result = (TwoFactorResponse) response.getEntity();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("Authentication successful");
        verify(twoFactorService, times(1)).authenticate(validRequest);
    }

    @Test
    void testFailedAuthentication() {
        // Given
        TwoFactorResponse expectedResponse = new TwoFactorResponse(false, "Authentication failed");
        when(twoFactorService.authenticate(any(TwoFactorRequest.class))).thenReturn(expectedResponse);

        // When
        Response response = controller.authenticate(validRequest);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        TwoFactorResponse result = (TwoFactorResponse) response.getEntity();
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Authentication failed");
    }

    @Test
    void testServiceThrowsClientException() {
        // Given
        ClientException clientException = new ClientException("Service unavailable", 503);
        when(twoFactorService.authenticate(any(TwoFactorRequest.class))).thenThrow(clientException);

        // When/Then
        assertThrows(ClientException.class, () -> controller.authenticate(validRequest));
        verify(twoFactorService, times(1)).authenticate(validRequest);
    }

    @Test
    void testServiceThrowsServiceException() {
        // Given
        ServiceException serviceException = new ServiceException("Internal error");
        when(twoFactorService.authenticate(any(TwoFactorRequest.class))).thenThrow(serviceException);

        // When/Then
        assertThrows(ServiceException.class, () -> controller.authenticate(validRequest));
        verify(twoFactorService, times(1)).authenticate(validRequest);
    }

    @Test
    void testServiceThrowsGenericException() {
        // Given
        RuntimeException runtimeException = new RuntimeException("Unexpected error");
        when(twoFactorService.authenticate(any(TwoFactorRequest.class))).thenThrow(runtimeException);

        // When/Then
        assertThrows(RuntimeException.class, () -> controller.authenticate(validRequest));
        verify(twoFactorService, times(1)).authenticate(validRequest);
    }

    @Test
    void testNullRequest() {
        // When/Then
        // Validation will be handled by Bean Validation, but we test that controller doesn't crash
        assertThrows(Exception.class, () -> controller.authenticate(null));
    }
}

