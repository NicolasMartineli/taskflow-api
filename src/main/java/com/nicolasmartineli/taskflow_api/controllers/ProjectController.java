package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.project.ProjectCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectResponse;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.ApiError;
import com.nicolasmartineli.taskflow_api.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("projects")
@Tag(name = "Projects", description = "Projects that belong to a team")
@ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
        content = @Content(schema = @Schema(implementation = ApiError.class)))
public class ProjectController implements LocationHeaderUriBuilder {

    private final ProjectService service;

    @PostMapping
    @Operation(summary = "Create a project")
    @ApiResponse(responseCode = "201", description = "Project created")
    @ApiResponse(responseCode = "404", description = "Team not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Project name already exists in this team",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> create(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse project = service.create(request);

        URI location = buildLocationUri(project.id());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find project by ID")
    @ApiResponse(responseCode = "200", description = "Project found")
    @ApiResponse(responseCode = "404", description = "Project not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ProjectResponse> findByID(@PathVariable UUID id) {
        ProjectResponse project = service.findById(id);

        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project")
    @ApiResponse(responseCode = "204", description = "Project updated")
    @ApiResponse(responseCode = "404", description = "Project not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Project name already exists in this team",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectUpdateRequest request) {

        service.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project")
    @ApiResponse(responseCode = "204", description = "Project deleted")
    @ApiResponse(responseCode = "404", description = "Project not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}