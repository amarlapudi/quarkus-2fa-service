package com.example.2fa.service;

import com.example.2fa.client.RsaSoapClient;
import com.example.2fa.model.TwoFactorRequest;
import com.example.2fa.model.TwoFactorResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TwoFactorService {

    @Inject
    RsaSoapClient rsaSoapClient;

    public TwoFactorResponse authenticate(TwoFactorRequest request) {
        return rsaSoapClient.callAdaptiveAuth(request);
    }
}