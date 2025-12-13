package com.example.twofa.exception;

import com.example.twofa.model.error.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception mapper that handles all exceptions and maps them to appropriate HTTP responses.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        ErrorResponse errorResponse;

        if (exception instanceof ValidationException) {
            ValidationException validationException = (ValidationException) exception;
            List<String> details = validationException.hasValidationErrors() 
                ? validationException.getValidationErrors() 
                : null;
            errorResponse = new ErrorResponse(
                400,
                "Validation Error",
                validationException.getMessage(),
                uriInfo.getPath(),
                details
            );
            LOG.warnf("Validation error: %s - Path: %s", validationException.getMessage(), uriInfo.getPath());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } else if (exception instanceof ClientException) {
            ClientException clientException = (ClientException) exception;
            int status = clientException.getHttpStatus();
            String errorType = status == 503 ? "Service Unavailable" : "Bad Gateway";
            errorResponse = new ErrorResponse(
                status,
                errorType,
                clientException.getMessage(),
                uriInfo.getPath()
            );
            LOG.errorf("Client error: %s - Path: %s", clientException.getMessage(), uriInfo.getPath(), clientException);
            return Response.status(status)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } else if (exception instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) exception;
            errorResponse = new ErrorResponse(
                500,
                "Internal Server Error",
                "An internal server error occurred",
                uriInfo.getPath()
            );
            LOG.errorf("Service error: %s - Path: %s", serviceException.getMessage(), uriInfo.getPath(), serviceException);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } else if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintException = (ConstraintViolationException) exception;
            List<String> violations = constraintException.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            errorResponse = new ErrorResponse(
                400,
                "Validation Error",
                "Request validation failed",
                uriInfo.getPath(),
                violations
            );
            LOG.warnf("Constraint violation: %s - Path: %s", violations, uriInfo.getPath());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } else if (exception instanceof WebApplicationException) {
            WebApplicationException webException = (WebApplicationException) exception;
            Response response = webException.getResponse();
            errorResponse = new ErrorResponse(
                response.getStatus(),
                "HTTP " + response.getStatus(),
                exception.getMessage() != null ? exception.getMessage() : "An error occurred",
                uriInfo.getPath()
            );
            LOG.warnf("Web application exception: %s - Path: %s", exception.getMessage(), uriInfo.getPath());
            return Response.status(response.getStatus())
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } else {
            // Generic exception handler
            errorResponse = new ErrorResponse(
                500,
                "Internal Server Error",
                "An unexpected error occurred",
                uriInfo.getPath()
            );
            LOG.errorf("Unexpected error: %s - Path: %s", exception.getMessage(), uriInfo.getPath(), exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}

