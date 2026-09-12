package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.task.TaskCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.Task;
import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;
import com.nicolasmartineli.taskflow_api.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController implements LocationHeaderUriBuilder {

    private final TaskService service;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse task = service.create(request);

        URI location = buildLocationUri(task.id());

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable UUID id) {
        TaskResponse task = service.findById(id);

        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findAll(
            @RequestParam(value = "projectId", required = false) UUID projectId,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "assigneeId", required = false) UUID assigneeId,
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable
    ) {
        Page<TaskResponse> all = service.findAll(projectId, status, priority, assigneeId, pageable);

        return ResponseEntity.ok(all);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody TaskUpdateRequest request) {

        service.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
