package com.example.twofa.infrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit 5 extension for managing WireMock server lifecycle.
 */
public class WireMockExtension implements BeforeEachCallback, AfterEachCallback {

    private WireMockServer wireMockServer;
    private int port;

    public WireMockExtension() {
        this(0); // Use dynamic port
    }

    public WireMockExtension(int port) {
        this.port = port;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        WireMockConfiguration config = WireMockConfiguration.options()
                .port(port);
        
        wireMockServer = new WireMockServer(config);
        wireMockServer.start();
        
        // Store in extension context for access in tests
        context.getStore(ExtensionContext.Namespace.create(getClass()))
                .put("wireMockServer", wireMockServer);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }

    public int getPort() {
        return wireMockServer != null ? wireMockServer.port() : port;
    }

    public String getBaseUrl() {
        return wireMockServer != null ? wireMockServer.baseUrl() : "http://localhost:" + port;
    }
}

