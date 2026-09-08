package com.nicolasmartineli.taskflow_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectUpdateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Project name must be between 2 and 100 characters")
        String name,

        String description
) {
}
