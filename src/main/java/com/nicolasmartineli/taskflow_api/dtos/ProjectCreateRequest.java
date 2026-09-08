package com.nicolasmartineli.taskflow_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProjectCreateRequest(
        @NotBlank(message = "Project name is required")
        @Size(min = 2, max = 100)
        String name,

        String description,

        @NotNull(message = "Team id is required")
        UUID teamId
) {
}
