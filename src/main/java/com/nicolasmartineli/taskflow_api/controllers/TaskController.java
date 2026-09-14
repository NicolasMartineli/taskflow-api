package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.comment.CommentCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.comment.CommentResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;
import com.nicolasmartineli.taskflow_api.services.CommentService;
import com.nicolasmartineli.taskflow_api.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController implements LocationHeaderUriBuilder {

    private final TaskService taskService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse task = taskService.create(request);

        URI location = buildLocationUri(task.id());

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable UUID id) {
        TaskResponse task = taskService.findById(id);

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
        Page<TaskResponse> all = taskService.findAll(projectId, status, priority, assigneeId, pageable);

        return ResponseEntity.ok(all);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody TaskUpdateRequest request) {

        taskService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable UUID taskId, @Valid @RequestBody CommentCreateRequest request) {

        CommentResponse comment = commentService.create(taskId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/comments/{id}")
                .buildAndExpand(comment.id())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponse>> findCommentsByTaskId(@PathVariable UUID taskId) {
        List<CommentResponse> comments = commentService.findByTaskId(taskId);

        return ResponseEntity.ok(comments);
    }

}
