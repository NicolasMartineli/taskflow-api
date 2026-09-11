package com.nicolasmartineli.taskflow_api.dtos.user;

import java.util.UUID;

public record UserSummary(
        UUID id,
        String name
) {
}
