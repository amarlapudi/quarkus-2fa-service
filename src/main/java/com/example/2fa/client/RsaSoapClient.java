package com.example.2fa.client;

import com.example.2fa.model.TwoFactorRequest;
import com.example.2fa.model.TwoFactorResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RsaSoapClient {

    public TwoFactorResponse callAdaptiveAuth(TwoFactorRequest request) {
        if ("testuser".equals(request.getUsername())) {
            return new TwoFactorResponse(true, "Authentication successful");
        }
        return new TwoFactorResponse(false, "Authentication failed");
    }
}