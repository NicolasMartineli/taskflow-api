package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.comment.CommentCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.comment.CommentResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.ApiError;
import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;
import com.nicolasmartineli.taskflow_api.services.CommentService;
import com.nicolasmartineli.taskflow_api.services.TaskService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management and filtering")
@ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",
        content = @Content(schema = @Schema(implementation = ApiError.class)))
public class TaskController implements LocationHeaderUriBuilder {

    private final TaskService taskService;
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @ApiResponse(responseCode = "404", description = "Project or assignee not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Assignee is not a member of the project's team",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> create(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse task = taskService.create(request);

        URI location = buildLocationUri(task.id());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find task by ID")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<TaskResponse> findById(@PathVariable UUID id) {
        TaskResponse task = taskService.findById(id);

        return ResponseEntity.ok(task);
    }

    @GetMapping
    @Operation(summary = "Search tasks with filters and pagination",
            description = "Returns a paginated list of tasks filtered optionally by projectId, status, priority, or assigneeId")
    @ApiResponse(responseCode = "200", description = "Tasks retrieved")
    public ResponseEntity<Page<TaskResponse>> findAll(
            @RequestParam(value = "projectId", required = false) UUID projectId,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "assigneeId", required = false) UUID assigneeId,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable
    ) {
        Page<TaskResponse> all = taskService.findAll(projectId, status, priority, assigneeId, pageable);

        return ResponseEntity.ok(all);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    @ApiResponse(responseCode = "204", description = "Task updated")
    @ApiResponse(responseCode = "404", description = "Task or assignee not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Assignee is not a member of the project's team",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody TaskUpdateRequest request) {
        taskService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @ApiResponse(responseCode = "204", description = "Task deleted")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/comments")
    @Tag(name = "Comments")
    @Operation(summary = "Add a comment to a task")
    @ApiResponse(responseCode = "201", description = "Comment created")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> addComment(@PathVariable UUID taskId, @Valid @RequestBody CommentCreateRequest request) {
        commentService.create(taskId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/tasks/{taskId}/comments")
                .buildAndExpand(taskId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{taskId}/comments")
    @Tag(name = "Comments")
    @Operation(summary = "List comments by task ID")
    @ApiResponse(responseCode = "200", description = "Comments retrieved")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<List<CommentResponse>> findCommentsByTaskId(@PathVariable UUID taskId) {
        List<CommentResponse> comments = commentService.findByTaskId(taskId);

        return ResponseEntity.ok(comments);
    }
}