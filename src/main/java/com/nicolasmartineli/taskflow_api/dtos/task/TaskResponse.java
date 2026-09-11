package com.nicolasmartineli.taskflow_api.dtos.task;

import com.nicolasmartineli.taskflow_api.dtos.project.ProjectSummary;
import com.nicolasmartineli.taskflow_api.dtos.user.UserSummary;
import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        ProjectSummary project,
        UserSummary assignee
) {
}
