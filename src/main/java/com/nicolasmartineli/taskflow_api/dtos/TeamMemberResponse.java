package com.nicolasmartineli.taskflow_api.dtos;

import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;

import java.util.UUID;

public record TeamMemberResponse(
        UUID userId,
        String name,
        TeamRole roleInTeam
) {
}
