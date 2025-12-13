package com.example.twofa.infrastructure;

import com.example.twofa.fixture.SoapResponseFixtures;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import org.jboss.logging.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Utility class for setting up mock SOAP server responses using WireMock.
 */
public class MockSoapServer {

    private static final Logger LOG = Logger.getLogger(MockSoapServer.class);
    private final WireMockServer wireMockServer;

    public MockSoapServer(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    /**
     * Sets up a successful SOAP response.
     */
    public void setupSuccessfulSoapResponse() {
        LOG.info("Setting up successful SOAP response");
        wireMockServer.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.SUCCESS_RESPONSE))
        );
    }

    /**
     * Sets up a failed SOAP response.
     */
    public void setupFailedSoapResponse() {
        LOG.info("Setting up failed SOAP response");
        wireMockServer.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.FAILURE_RESPONSE))
        );
    }

    /**
     * Sets up a SOAP fault response.
     */
    public void setupSoapFaultResponse() {
        LOG.info("Setting up SOAP fault response");
        wireMockServer.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.FAULT_RESPONSE))
        );
    }

    /**
     * Sets up a timeout response (delayed response).
     */
    public void setupTimeoutResponse(int delayMillis) {
        LOG.infof("Setting up timeout response with delay: %d ms", delayMillis);
        wireMockServer.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(delayMillis)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(SoapResponseFixtures.SUCCESS_RESPONSE))
        );
    }

    /**
     * Sets up a connection error (connection refused).
     */
    public void setupConnectionError() {
        LOG.info("Setting up connection error");
        wireMockServer.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .willReturn(aResponse()
                    .withFault(Fault.CONNECTION_RESET_BY_PEER))
        );
    }

    /**
     * Sets up a response that matches specific username in request.
     */
    public void setupResponseForUsername(String username, boolean success) {
        LOG.infof("Setting up response for username: %s, success: %s", username, success);
        String body = success ? SoapResponseFixtures.SUCCESS_RESPONSE : SoapResponseFixtures.FAILURE_RESPONSE;
        wireMockServer.stubFor(
            post(urlEqualTo("/mock-adaptive-auth"))
                .withRequestBody(new ContainsPattern(username))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(body))
        );
    }

    /**
     * Verifies that a SOAP request was made.
     */
    public void verifySoapRequest() {
        wireMockServer.verify(postRequestedFor(urlEqualTo("/mock-adaptive-auth")));
    }

    /**
     * Verifies that a SOAP request was made with specific username.
     */
    public void verifySoapRequestWithUsername(String username) {
        wireMockServer.verify(
            postRequestedFor(urlEqualTo("/mock-adaptive-auth"))
                .withRequestBody(new ContainsPattern(username))
        );
    }

    /**
     * Resets all stubs.
     */
    public void reset() {
        wireMockServer.resetAll();
    }
}

