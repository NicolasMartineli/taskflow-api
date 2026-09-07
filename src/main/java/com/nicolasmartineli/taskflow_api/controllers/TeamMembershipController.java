package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.TeamMembershipCreateRequest;
import com.nicolasmartineli.taskflow_api.services.TeamMembershipService;
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
public class TeamMembershipController {

    private final TeamMembershipService service;

    @PostMapping("/{teamId}/members")
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
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID teamId,
            @PathVariable UUID userId) {

        service.removeMember(teamId, userId);

        return ResponseEntity.noContent().build();

    }


}
