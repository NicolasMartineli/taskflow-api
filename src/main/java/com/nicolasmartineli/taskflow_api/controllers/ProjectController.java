package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.project.ProjectCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectResponse;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectUpdateRequest;
import com.nicolasmartineli.taskflow_api.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("projects")
public class ProjectController implements LocationHeaderUriBuilder {

    private final ProjectService service;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse project = service.create(request);

        URI location = buildLocationUri(project.id());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> findByID(@PathVariable UUID id) {
        ProjectResponse project = service.findById(id);

        return ResponseEntity.ok(project);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectUpdateRequest request) {

        service.update(id, request);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
