package com.nicolasmartineli.taskflow_api.dtos;

import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record TeamMembershipCreateRequest(
        @UUID(message = "Invalid format for id")
        @NotNull(message = "User id is required")
        UUID userId,

        @NotNull(message = "Role in team is required")
        TeamRole roleInTeam
) {
}
