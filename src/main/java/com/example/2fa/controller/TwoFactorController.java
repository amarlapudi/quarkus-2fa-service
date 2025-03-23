package com.example.2fa.controller;

import com.example.2fa.model.TwoFactorRequest;
import com.example.2fa.model.TwoFactorResponse;
import com.example.2fa.service.TwoFactorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/2fa")
@Tag(name = "2FA Service", description = "Performs 2FA via RSA Adaptive Authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TwoFactorController {

    @Inject
    TwoFactorService twoFactorService;

    @POST
    @Path("/authenticate")
    @Operation(summary = "Authenticate user via 2FA", description = "Invokes RSA Adaptive Auth backend")
    @APIResponse(responseCode = "200", description = "2FA authentication result")
    public Response authenticate(@RequestBody(description = "2FA input", required = true) TwoFactorRequest request) {
        TwoFactorResponse result = twoFactorService.authenticate(request);
        return Response.ok(result).build();
    }
}