package com.example.twofa.integration;

import com.example.twofa.infrastructure.MockSoapServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Base class for integration tests with common setup.
 */
@QuarkusTest
@QuarkusTestResource(MockSoapServerTestResource.class)
public abstract class BaseIntegrationTest {
    // Common setup and utilities can be added here
}

