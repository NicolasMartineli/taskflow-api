package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
public class TeamController implements LocationHeaderUriBuilder {

    private final TeamService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid TeamCreateRequest team) {
        TeamResponse teamResponse = service.create(team);

        URI location = buildLocationUri(teamResponse.id());

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> findById(@PathVariable UUID id) {
        TeamResponse teamResponse = service.findById(id);

        return ResponseEntity.ok(teamResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody @Valid TeamUpdateRequest team) {
        service.update(id, team);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
