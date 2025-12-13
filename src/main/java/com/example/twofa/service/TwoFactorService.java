package com.example.twofa.service;

import com.example.twofa.client.RsaSoapClient;
import com.example.twofa.exception.ClientException;
import com.example.twofa.exception.ServiceException;
import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TwoFactorService {

    private static final Logger LOG = Logger.getLogger(TwoFactorService.class);

    @Inject
    RsaSoapClient rsaSoapClient;

    public TwoFactorResponse authenticate(TwoFactorRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        LOG.debugf("Authenticating user: %s via RSA SOAP client", request.getUsername());
        
        try {
            TwoFactorResponse response = rsaSoapClient.callAdaptiveAuth(request);
            LOG.debugf("RSA SOAP client returned response for user: %s - Success: %s", 
                      request.getUsername(), response.isSuccess());
            return response;
        } catch (ClientException e) {
            LOG.errorf(e, "Client error during authentication for user: %s", request.getUsername());
            throw e;
        } catch (Exception e) {
            LOG.errorf(e, "Unexpected error during authentication for user: %s", request.getUsername());
            throw new ServiceException("Failed to authenticate user: " + request.getUsername(), e);
        }
    }
}

