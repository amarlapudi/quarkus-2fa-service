package com.example.twofa.controller;

import com.example.twofa.model.TwoFactorRequest;
import com.example.twofa.model.TwoFactorResponse;
import com.example.twofa.service.TwoFactorService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.logging.Logger;

@Path("/2fa")
@Tag(name = "2FA Service", description = "Performs 2FA via RSA Adaptive Authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TwoFactorController {

    private static final Logger LOG = Logger.getLogger(TwoFactorController.class);

    @Inject
    TwoFactorService twoFactorService;

    @POST
    @Path("/authenticate")
    @Operation(summary = "Authenticate user via 2FA", description = "Invokes RSA Adaptive Authentication SOAP service and returns a success/failure result")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "2FA authentication result",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = TwoFactorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Bad Request - Validation error or invalid request format",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = com.example.twofa.model.error.ErrorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "502",
            description = "Bad Gateway - Error communicating with RSA SOAP service",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = com.example.twofa.model.error.ErrorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "503",
            description = "Service Unavailable - RSA SOAP service is unavailable",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = com.example.twofa.model.error.ErrorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = com.example.twofa.model.error.ErrorResponse.class)
            )
        )
    })
    public Response authenticate(
            @RequestBody(description = "2FA authentication request", required = true) 
            @Valid TwoFactorRequest request) {
        
        LOG.infof("Received 2FA authentication request for user: %s from IP: %s", 
                  request.getUsername(), request.getIpAddress());
        
        try {
            TwoFactorResponse result = twoFactorService.authenticate(request);
            LOG.infof("2FA authentication completed for user: %s - Success: %s", 
                     request.getUsername(), result.isSuccess());
            return Response.ok(result).build();
        } catch (Exception e) {
            LOG.errorf(e, "Error processing 2FA authentication request for user: %s", request.getUsername());
            throw e; // Let GlobalExceptionMapper handle it
        }
    }
}

