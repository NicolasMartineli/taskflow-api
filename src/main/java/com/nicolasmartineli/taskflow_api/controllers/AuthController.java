package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.login.LoginRequest;
import com.nicolasmartineli.taskflow_api.dtos.login.LoginResponse;
import com.nicolasmartineli.taskflow_api.exceptions.ApiError;
import com.nicolasmartineli.taskflow_api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and token generation")
public class AuthController {

    private final AuthService authService;

    @SecurityRequirements
    @PostMapping("/login")
    @Operation(summary = "Authenticate user",
            description = "Validates user credentials (email and password) and returns a JWT access token")
         @ApiResponse(responseCode = "200", description = "Authentication successful, JWT token returned")
         @ApiResponse(responseCode = "422", description = "Validation errors",
                 content = @Content(schema = @Schema(implementation = ApiError.class)))
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
                content = @Content(schema = @Schema(implementation = ApiError.class )))
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
