package com.nicolasmartineli.taskflow_api.dtos;

import com.nicolasmartineli.taskflow_api.models.enums.UserRole;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UserRole role
) {
}
