package com.nicolasmartineli.taskflow_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamUpdateRequest(
        @NotBlank(message = "Team name is required")
        @Size(min = 2, max = 100, message = "Team must be between 2 and 100 characters ")
        String name) {
}
