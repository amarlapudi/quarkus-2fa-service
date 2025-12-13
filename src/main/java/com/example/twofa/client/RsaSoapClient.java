package com.example.twofa.client;

import com.example.twofa.exception.ClientException;
import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class RsaSoapClient {

    private static final Logger LOG = Logger.getLogger(RsaSoapClient.class);

    @ConfigProperty(name = "rsa.soap.endpoint.url", defaultValue = "http://localhost:8081/mock-adaptive-auth")
    String soapEndpointUrl;

    @ConfigProperty(name = "rsa.soap.connection.timeout", defaultValue = "5000")
    int connectionTimeout;

    @ConfigProperty(name = "rsa.soap.read.timeout", defaultValue = "10000")
    int readTimeout;

    public TwoFactorResponse callAdaptiveAuth(TwoFactorRequest request) {
        LOG.infof("Calling RSA Adaptive Auth SOAP service for user: %s from IP: %s at endpoint: %s", 
                  request.getUsername(), request.getIpAddress(), soapEndpointUrl);
        
        try {
            // Simulate SOAP call - in real implementation, this would make actual SOAP request
            TwoFactorResponse response = performSoapCall(request);
            
            LOG.infof("RSA SOAP service responded for user: %s - Success: %s", 
                     request.getUsername(), response.isSuccess());
            return response;
            
        } catch (ConnectException e) {
            LOG.errorf(e, "Failed to connect to RSA SOAP service at %s", soapEndpointUrl);
            throw new ClientException(
                "Unable to connect to RSA Adaptive Authentication service", 
                e, 
                503 // Service Unavailable
            );
        } catch (SocketTimeoutException | TimeoutException e) {
            LOG.errorf(e, "Timeout while calling RSA SOAP service at %s", soapEndpointUrl);
            throw new ClientException(
                "Timeout while calling RSA Adaptive Authentication service", 
                e, 
                503 // Service Unavailable
            );
        } catch (Exception e) {
            LOG.errorf(e, "Unexpected error calling RSA SOAP service at %s", soapEndpointUrl);
            throw new ClientException(
                "Error communicating with RSA Adaptive Authentication service: " + e.getMessage(), 
                e, 
                502 // Bad Gateway
            );
        }
    }

    /**
     * Performs the actual SOAP call to RSA Adaptive Authentication service.
     * This is a mock implementation for demonstration purposes.
     * In production, this would use a SOAP client library to make the actual call.
     */
    private TwoFactorResponse performSoapCall(TwoFactorRequest request) throws Exception {
        LOG.debugf("Performing SOAP call for user: %s", request.getUsername());
        
        // Mock implementation - simulate different scenarios
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        // Simulate successful authentication for testuser
        if ("testuser".equals(request.getUsername())) {
            return new TwoFactorResponse(true, "Authentication successful");
        }
        
        // Simulate network error for specific test case
        if ("networkerror".equals(request.getUsername())) {
            throw new ConnectException("Connection refused");
        }
        
        // Simulate timeout for specific test case
        if ("timeoutuser".equals(request.getUsername())) {
            throw new SocketTimeoutException("Read timed out");
        }
        
        // Default: authentication failed
        return new TwoFactorResponse(false, "Authentication failed");
    }
}

