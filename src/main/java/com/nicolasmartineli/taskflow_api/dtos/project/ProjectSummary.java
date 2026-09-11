package com.nicolasmartineli.taskflow_api.dtos.project;

import java.util.UUID;

public record ProjectSummary(
        UUID id,
        String name
) {
}
