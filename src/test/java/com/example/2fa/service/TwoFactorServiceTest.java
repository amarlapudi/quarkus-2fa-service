package com.example.2fa.service;

import com.example.2fa.client.RsaSoapClient;
import com.example.2fa.model.TwoFactorRequest;
import com.example.2fa.model.TwoFactorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TwoFactorServiceTest {

    @InjectMocks
    TwoFactorService service;

    @Mock
    RsaSoapClient client;

    @Test
    void testSuccessfulAuthentication() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("127.0.0.1");

        Mockito.when(client.callAdaptiveAuth(any()))
               .thenReturn(new TwoFactorResponse(true, "Success"));

        TwoFactorResponse response = service.authenticate(request);

        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
    }
}