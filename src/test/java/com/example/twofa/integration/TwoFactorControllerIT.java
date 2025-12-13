package com.example.twofa.integration;

import com.example.twofa.infrastructure.MockSoapServerTestResource;
import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import com.example.twofa.model.error.ErrorResponse;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(MockSoapServerTestResource.class)
class TwoFactorControllerIT extends BaseIntegrationTest {

    private TwoFactorRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new TwoFactorRequest();
        validRequest.setUsername("testuser");
        validRequest.setIpAddress("192.168.1.100");
    }

    @Test
    void testSuccessfulAuthentication() {
        // Given - valid request
        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(validRequest)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("success", equalTo(true))
            .body("message", notNullValue());
    }

    @Test
    void testFailedAuthentication() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("otheruser");
        request.setIpAddress("192.168.1.100");

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("success", equalTo(false))
            .body("message", notNullValue());
    }

    @Test
    void testValidationErrorNullUsername() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(null);
        request.setIpAddress("192.168.1.100");

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400))
            .body("error", equalTo("Validation Error"))
            .body("path", equalTo("/2fa/authenticate"));
    }

    @Test
    void testValidationErrorBlankUsername() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("");
        request.setIpAddress("192.168.1.100");

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400))
            .body("error", equalTo("Validation Error"));
    }

    @Test
    void testValidationErrorNullIpAddress() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress(null);

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400))
            .body("error", equalTo("Validation Error"));
    }

    @Test
    void testValidationErrorInvalidIpAddress() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("testuser");
        request.setIpAddress("invalid-ip");

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400))
            .body("error", equalTo("Validation Error"));
    }

    @Test
    void testValidationErrorWithDetails() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("");
        request.setIpAddress("invalid-ip");

        // When/Then
        ErrorResponse errorResponse = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .extract()
            .as(ErrorResponse.class);

        assertThat(errorResponse.getStatus()).isEqualTo(400);
        assertThat(errorResponse.getDetails()).isNotEmpty();
    }

    @Test
    void testNetworkErrorScenario() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("networkerror");
        request.setIpAddress("192.168.1.100");

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(503)
            .contentType(ContentType.JSON)
            .body("status", equalTo(503))
            .body("error", equalTo("Service Unavailable"));
    }

    @Test
    void testTimeoutScenario() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername("timeoutuser");
        request.setIpAddress("192.168.1.100");

        // When/Then
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(503)
            .contentType(ContentType.JSON)
            .body("status", equalTo(503))
            .body("error", equalTo("Service Unavailable"));
    }

    @Test
    void testResponseBodyStructure() {
        // Given - valid request
        // When
        TwoFactorResponse response = given()
            .contentType(ContentType.JSON)
            .body(validRequest)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(200)
            .extract()
            .as(TwoFactorResponse.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isNotNull();
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    void testErrorResponseStructure() {
        // Given
        TwoFactorRequest request = new TwoFactorRequest();
        request.setUsername(null);
        request.setIpAddress("192.168.1.100");

        // When
        ErrorResponse errorResponse = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/2fa/authenticate")
        .then()
            .statusCode(400)
            .extract()
            .as(ErrorResponse.class);

        // Then
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(400);
        assertThat(errorResponse.getError()).isNotNull();
        assertThat(errorResponse.getMessage()).isNotNull();
        assertThat(errorResponse.getPath()).isEqualTo("/2fa/authenticate");
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }
}

