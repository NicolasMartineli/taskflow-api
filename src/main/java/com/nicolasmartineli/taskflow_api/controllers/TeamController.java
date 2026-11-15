package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.project.ProjectResponse;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.ApiError;
import com.nicolasmartineli.taskflow_api.services.ProjectService;
import com.nicolasmartineli.taskflow_api.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Team management")
@ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
        content = @Content(schema = @Schema(implementation = ApiError.class)))
public class TeamController implements LocationHeaderUriBuilder {

    private final TeamService service;
    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a team")
    @ApiResponse(responseCode = "201", description = "Team created")
    @ApiResponse(responseCode = "409", description = "Team name already exists",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> create(@RequestBody @Valid TeamCreateRequest team) {
        TeamResponse teamResponse = service.create(team);

        URI location = buildLocationUri(teamResponse.id());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find team by ID")
    @ApiResponse(responseCode = "200", description = "Team found")
    @ApiResponse(responseCode = "404", description = "Team not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<TeamResponse> findById(@PathVariable UUID id) {
        TeamResponse teamResponse = service.findById(id);

        return ResponseEntity.ok(teamResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update team")
    @ApiResponse(responseCode = "204", description = "Team updated")
    @ApiResponse(responseCode = "404", description = "Team not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Team name already exists",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody @Valid TeamUpdateRequest team) {
        service.update(id, team);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete team")
    @ApiResponse(responseCode = "204", description = "Team deleted")
    @ApiResponse(responseCode = "404", description = "Team not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Cannot delete team with existing projects",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/projects")
    @Tag(name = "Projects")
    @Operation(summary = "List projects by team ID",
            description = "Returns a paginated list of projects belonging to the specified team")
    @ApiResponse(responseCode = "200", description = "Projects retrieved")
    @ApiResponse(responseCode = "404", description = "Team not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Page<ProjectResponse>> findProjectsByTeamId(
            @PathVariable UUID teamId,
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {

        Page<ProjectResponse> project = projectService.findByTeamId(teamId, pageable);

        return ResponseEntity.ok(project);
    }
}