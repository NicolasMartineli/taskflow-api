package com.nicolasmartineli.taskflow_api.dtos;


import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        TeamSummaryResponse team) {
}
