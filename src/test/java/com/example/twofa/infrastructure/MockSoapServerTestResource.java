package com.example.twofa.infrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Quarkus test resource for managing WireMock server lifecycle.
 */
public class MockSoapServerTestResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = Logger.getLogger(MockSoapServerTestResource.class);
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        LOG.info("Starting WireMock server for testing");
        wireMockServer = new WireMockServer(
            WireMockConfiguration.options()
                .dynamicPort()
        );
        wireMockServer.start();

        int port = wireMockServer.port();
        LOG.infof("WireMock server started on port: %d", port);

        Map<String, String> config = new HashMap<>();
        config.put("rsa.soap.endpoint.url", "http://localhost:" + port + "/mock-adaptive-auth");
        config.put("wiremock.server.port", String.valueOf(port));
        return config;
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            LOG.info("Stopping WireMock server");
            wireMockServer.stop();
        }
    }

    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }
}

