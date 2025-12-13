package com.example.twofa.util;

import com.example.twofa.fixture.SoapResponseFixtures;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Helper methods for WireMock setup in tests.
 */
public class WireMockHelper {

    /**
     * Sets up a successful SOAP response on the WireMock server.
     */
    public static void setupSuccessfulSoapResponse(WireMockServer server) {
        server.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.SUCCESS_RESPONSE))
        );
    }

    /**
     * Sets up a failed SOAP response on the WireMock server.
     */
    public static void setupFailedSoapResponse(WireMockServer server) {
        server.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.FAILURE_RESPONSE))
        );
    }

    /**
     * Sets up a timeout response (delayed response).
     */
    public static void setupTimeoutResponse(WireMockServer server, int delayMillis) {
        server.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(delayMillis)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.SUCCESS_RESPONSE))
        );
    }

    /**
     * Sets up a SOAP fault response.
     */
    public static void setupSoapFaultResponse(WireMockServer server) {
        server.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.FAULT_RESPONSE))
        );
    }

    /**
     * Sets up a connection error.
     */
    public static void setupConnectionError(WireMockServer server) {
        server.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withFault(Fault.CONNECTION_RESET_BY_PEER))
        );
    }

    /**
     * Verifies that a SOAP request was made.
     */
    public static void verifySoapRequest(WireMockServer server) {
        server.verify(postRequestedFor(urlEqualTo("/mock-adaptive-auth")));
    }

    /**
     * Verifies that a SOAP request was made with specific username.
     */
    public static void verifySoapRequestWithUsername(WireMockServer server, String username) {
        server.verify(
            postRequestedFor(urlEqualTo("/mock-adaptive-auth"))
                .withRequestBody(new ContainsPattern(username))
        );
    }
}

