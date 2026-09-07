package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.TeamMembershipCreateRequest;
import com.nicolasmartineli.taskflow_api.services.TeamMembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamMembersipController implements LocationHeaderUriBuilder {
    private final TeamMembershipService service;

    @PostMapping("/{teamId}/members")
    public ResponseEntity<Void> addMember(
            @PathVariable UUID teamId,
            @Valid @RequestBody TeamMembershipCreateRequest request) {

        service.addMember(teamId, request);

        URI location = buildLocationUri(teamId);

        return ResponseEntity.created(location).build();

    }


}
