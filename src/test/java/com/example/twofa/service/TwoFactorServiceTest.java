package com.example.twofa.service;

import com.example.twofa.client.RsaSoapClient;
import com.example.twofa.exception.ClientException;
import com.example.twofa.exception.ServiceException;
import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwoFactorServiceTest {

    @InjectMocks
    TwoFactorService service;

    @Mock
    RsaSoapClient client;

    private TwoFactorRequest request;

    @BeforeEach
    void setUp() {
        request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("127.0.0.1");
    }

    @Test
    void testSuccessfulAuthentication() {
        // Given
        TwoFactorResponse expectedResponse = new TwoFactorResponse(true, "Success");
        when(client.callAdaptiveAuth(any(TwoFactorRequest.class))).thenReturn(expectedResponse);

        // When
        TwoFactorResponse response = service.authenticate(request);

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        verify(client, times(1)).callAdaptiveAuth(request);
    }

    @Test
    void testFailedAuthentication() {
        // Given
        TwoFactorResponse expectedResponse = new TwoFactorResponse(false, "Authentication failed");
        when(client.callAdaptiveAuth(any(TwoFactorRequest.class))).thenReturn(expectedResponse);

        // When
        TwoFactorResponse response = service.authenticate(request);

        // Then
        assertFalse(response.isSuccess());
        assertEquals("Authentication failed", response.getMessage());
        verify(client, times(1)).callAdaptiveAuth(request);
    }

    @Test
    void testClientThrowsClientException() {
        // Given
        ClientException clientException = new ClientException("Service unavailable", 503);
        when(client.callAdaptiveAuth(any(TwoFactorRequest.class))).thenThrow(clientException);

        // When/Then
        ClientException thrown = assertThrows(ClientException.class, () -> service.authenticate(request));
        assertEquals("Service unavailable", thrown.getMessage());
        assertEquals(503, thrown.getHttpStatus());
        verify(client, times(1)).callAdaptiveAuth(request);
    }

    @Test
    void testClientThrowsGenericException() {
        // Given
        RuntimeException runtimeException = new RuntimeException("Unexpected error");
        when(client.callAdaptiveAuth(any(TwoFactorRequest.class))).thenThrow(runtimeException);

        // When/Then
        ServiceException thrown = assertThrows(ServiceException.class, () -> service.authenticate(request));
        assertThat(thrown.getMessage()).contains("Failed to authenticate user");
        assertThat(thrown.getCause()).isEqualTo(runtimeException);
        verify(client, times(1)).callAdaptiveAuth(request);
    }

    @Test
    void testClientThrowsNullPointerException() {
        // Given
        NullPointerException nullPointerException = new NullPointerException("Null value");
        when(client.callAdaptiveAuth(any(TwoFactorRequest.class))).thenThrow(nullPointerException);

        // When/Then
        ServiceException thrown = assertThrows(ServiceException.class, () -> service.authenticate(request));
        assertThat(thrown.getMessage()).contains("Failed to authenticate user");
        assertThat(thrown.getCause()).isEqualTo(nullPointerException);
        verify(client, times(1)).callAdaptiveAuth(request);
    }

    @Test
    void testNullRequest() {
        // Given - null request
        // When/Then - should throw IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.authenticate(null));
        assertEquals("Request cannot be null", exception.getMessage());
    }
}

