package com.nicolasmartineli.taskflow_api.dtos.team;

import java.util.UUID;

public record TeamSummaryResponse(
        UUID id,
        String name

) {
}
