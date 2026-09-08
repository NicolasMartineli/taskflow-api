package com.nicolasmartineli.taskflow_api.dtos;

import java.util.UUID;

public record TeamSummaryResponse(
        UUID id,
        String name

) {
}
