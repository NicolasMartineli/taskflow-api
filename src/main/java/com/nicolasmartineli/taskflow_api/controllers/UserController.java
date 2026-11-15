package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.user.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.user.UserResponse;
import com.nicolasmartineli.taskflow_api.dtos.user.UserUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.ApiError;
import com.nicolasmartineli.taskflow_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration and management")
public class UserController implements LocationHeaderUriBuilder {

    private final UserService service;

    @SecurityRequirements
    @PostMapping
    @Operation(summary = "Register a new user",
            description = "Creates a new user account with the provided name, email, and password")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "409", description = "Email already exists",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateRequest user) {
        UserResponse userResponse = service.create(user);
        URI location = buildLocationUri(userResponse.id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse user = service.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "Search users with pagination",
            description = "Returns a paginated list of users filtered optionally by name or email")
    @ApiResponse(responseCode = "200", description = "Users retrieved")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Page<UserResponse>> findByNameAndEmail(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        Page<UserResponse> byNameAndEmail = service.findByNameAndEmail(name, email, pageable);
        return ResponseEntity.ok(byNameAndEmail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "204", description = "User updated")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Email already exists",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest user) {
        service.update(id, user);
        return ResponseEntity.noContent().build();
    }
}