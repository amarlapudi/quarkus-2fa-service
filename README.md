# Quarkus 2FA Service

A Quarkus RESTful microservice for Two-Factor Authentication (2FA) using RSA Adaptive Authentication.

## Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **Git** (for cloning the repository)

## Running the Service

### Development Mode (Recommended)

The easiest way to run the service during development is using Quarkus Dev Mode, which provides:
- Hot reload on code changes
- Continuous testing
- Dev UI at `/q/dev`

```bash
mvn quarkus:dev
```

The service will start on `http://localhost:8080` by default.

### Production Mode

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Run the JAR:**
   ```bash
   java -jar target/quarkus-2fa-service-1.0.0-runner.jar
   ```

### Custom Port Configuration

To run on a different port, set the `quarkus.http.port` property:

**Development mode:**
```bash
mvn quarkus:dev -Dquarkus.http.port=8081
```

**Production mode:**
Add to `src/main/resources/application.properties`:
```properties
quarkus.http.port=8081
```

## API Endpoints

### Main Endpoint

- **POST** `/2fa/authenticate` - Authenticate user via 2FA

### API Documentation

- **Swagger UI**: http://localhost:8080/q/swagger-ui
- **OpenAPI Spec**: http://localhost:8080/q/openapi

## Testing the Service

### Run All Tests

```bash
mvn test
```

### Run Tests with Coverage

```bash
mvn clean test jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

### Integration Tests

```bash
mvn verify
```

## Example API Request

### Using cURL

```bash
curl -X POST http://localhost:8080/2fa/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "ipAddress": "192.168.1.100"
  }'
```

### Expected Response (Success)

```json
{
  "success": true,
  "message": "Authentication successful"
}
```

### Expected Response (Failure)

```json
{
  "success": false,
  "message": "Authentication failed"
}
```

## Configuration

### Application Properties

Main configuration file: `src/main/resources/application.properties`

Key properties:
- `quarkus.http.port` - HTTP server port (default: 8080)
- `quarkus.log.level` - Logging level (default: INFO)
- `rsa.soap.endpoint.url` - RSA SOAP service endpoint URL (default: http://localhost:8081/mock-adaptive-auth)
- `rsa.soap.connection.timeout` - Connection timeout in milliseconds (default: 5000)
- `rsa.soap.read.timeout` - Read timeout in milliseconds (default: 10000)

### Environment Variables

You can override properties using environment variables:

```bash
export QUARKUS_HTTP_PORT=8081
export QUARKUS_LOG_LEVEL=DEBUG
mvn quarkus:dev
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/twofa/
│   │   ├── controller/     # REST endpoints
│   │   ├── service/         # Business logic
│   │   ├── client/          # SOAP client
│   │   ├── model/           # Data models
│   │   └── exception/       # Exception handling
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/example/twofa/
    │   ├── controller/      # Controller tests
    │   ├── service/         # Service tests
    │   ├── client/          # Client tests
    │   ├── integration/     # Integration tests
    │   └── util/            # Test utilities
    └── resources/
        └── application.properties
```

## Features

- ✅ RESTful API with OpenAPI/Swagger documentation
- ✅ Comprehensive error handling with custom exceptions
- ✅ Bean Validation for request validation
- ✅ Global exception mapper for consistent error responses
- ✅ 100% code coverage with unit and integration tests
- ✅ WireMock integration for SOAP service mocking
- ✅ JaCoCo code coverage reporting
- ✅ Java 17 compatibility

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, change it:

```bash
mvn quarkus:dev -Dquarkus.http.port=8081
```

### Build Failures

Clean and rebuild:

```bash
mvn clean install
```

### Test Failures

Run tests with verbose output:

```bash
mvn test -X
```

## License

This project is licensed under the MIT License.


http://localhost:8080/q/swagger-ui
