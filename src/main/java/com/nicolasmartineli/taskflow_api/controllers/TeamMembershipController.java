package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.teammembership.TeamMembershipCreateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.ApiError;
import com.nicolasmartineli.taskflow_api.services.TeamMembershipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Team Members", description = "Add and remove team members")
@ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
        content = @Content(schema = @Schema(implementation = ApiError.class)))
public class TeamMembershipController {

    private final TeamMembershipService service;

    @PostMapping("/{teamId}/members")
    @Operation(summary = "Add a member to a team")
    @ApiResponse(responseCode = "201", description = "Member added to team")
    @ApiResponse(responseCode = "404", description = "Team or user not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "User is already a member of this team",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> addMember(
            @PathVariable UUID teamId,
            @Valid @RequestBody TeamMembershipCreateRequest request) {

        service.addMember(teamId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/teams/{teamId}")
                .buildAndExpand(teamId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    @Operation(summary = "Remove a member from a team")
    @ApiResponse(responseCode = "204", description = "Member removed from team")
    @ApiResponse(responseCode = "404", description = "Team, user or membership not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Cannot remove the last ADMIN from the team",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID teamId,
            @PathVariable UUID userId) {

        service.removeMember(teamId, userId);

        return ResponseEntity.noContent().build();
    }
}