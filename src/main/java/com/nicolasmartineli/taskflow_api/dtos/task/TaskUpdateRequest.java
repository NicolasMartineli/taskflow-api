package com.nicolasmartineli.taskflow_api.dtos.task;

import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record TaskUpdateRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 2, max = 100)
        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        @FutureOrPresent(message = "The due date must be in the future or present")
        LocalDate dueDate,

        UUID assigneeId
) {
}
