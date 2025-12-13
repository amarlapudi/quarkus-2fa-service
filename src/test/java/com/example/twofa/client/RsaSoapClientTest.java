package com.example.twofa.client;

import com.example.twofa.exception.ClientException;
import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RsaSoapClientTest {

    @Test
    void testSuccessfulAuthenticationForTestUser() {
        // Given
        RsaSoapClient client = new RsaSoapClient();
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("192.168.1.100");

        // When
        TwoFactorResponse response = client.callAdaptiveAuth(request);

        // Then
        assertThat(response).isNotNull();
        assertTrue(response.isSuccess());
        assertEquals("Authentication successful", response.getMessage());
    }

    @Test
    void testFailedAuthenticationForOtherUser() {
        // Given
        RsaSoapClient client = new RsaSoapClient();
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("otheruser");
        request.setIpAddress("192.168.1.100");

        // When
        TwoFactorResponse response = client.callAdaptiveAuth(request);

        // Then
        assertThat(response).isNotNull();
        assertFalse(response.isSuccess());
        assertEquals("Authentication failed", response.getMessage());
    }

    @Test
    void testNetworkErrorScenario() {
        // Given
        RsaSoapClient client = new RsaSoapClient();
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("networkerror");
        request.setIpAddress("192.168.1.100");

        // When/Then
        ClientException exception = assertThrows(ClientException.class, () -> client.callAdaptiveAuth(request));
        assertEquals(503, exception.getHttpStatus());
        assertThat(exception.getMessage()).contains("Unable to connect");
    }

    @Test
    void testTimeoutScenario() {
        // Given
        RsaSoapClient client = new RsaSoapClient();
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("timeoutuser");
        request.setIpAddress("192.168.1.100");

        // When/Then
        ClientException exception = assertThrows(ClientException.class, () -> client.callAdaptiveAuth(request));
        assertEquals(503, exception.getHttpStatus());
        assertThat(exception.getMessage()).contains("Timeout");
    }

    @Test
    void testNullUsername() {
        // Given
        RsaSoapClient client = new RsaSoapClient();
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(null);
        request.setIpAddress("192.168.1.100");

        // When/Then
        ClientException exception = assertThrows(ClientException.class, () -> client.callAdaptiveAuth(request));
        assertThat(exception.getMessage()).contains("Error communicating");
    }

    @Test
    void testEmptyUsername() {
        // Given
        RsaSoapClient client = new RsaSoapClient();
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("");
        request.setIpAddress("192.168.1.100");

        // When/Then
        ClientException exception = assertThrows(ClientException.class, () -> client.callAdaptiveAuth(request));
        assertThat(exception.getMessage()).contains("Error communicating");
    }

    @Test
    void testNullRequest() {
        // Given
        RsaSoapClient client = new RsaSoapClient();

        // When/Then
        assertThrows(Exception.class, () -> client.callAdaptiveAuth(null));
    }
}

