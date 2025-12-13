package com.example.twofa.fixture;

import com.example.twofa.model.TwoFactorRequest;

/**
 * Predefined request objects for common test scenarios.
 */
public class RequestFixtures {

    public static TwoFactorRequest validRequest() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("192.168.1.100");
        return request;
    }

    public static TwoFactorRequest requestWithNullUsername() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(null);
        request.setIpAddress("192.168.1.100");
        return request;
    }

    public static TwoFactorRequest requestWithBlankUsername() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("");
        request.setIpAddress("192.168.1.100");
        return request;
    }

    public static TwoFactorRequest requestWithNullIpAddress() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress(null);
        return request;
    }

    public static TwoFactorRequest requestWithInvalidIpAddress() {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("invalid-ip");
        return request;
    }

    public static TwoFactorRequest requestForUser(String username) {
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(username);
        request.setIpAddress("192.168.1.100");
        return request;
    }
}

