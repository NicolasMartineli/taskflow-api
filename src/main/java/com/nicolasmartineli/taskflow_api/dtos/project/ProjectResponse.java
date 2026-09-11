package com.nicolasmartineli.taskflow_api.dtos.project;


import com.nicolasmartineli.taskflow_api.dtos.team.TeamSummaryResponse;

import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        TeamSummaryResponse team) {
}
