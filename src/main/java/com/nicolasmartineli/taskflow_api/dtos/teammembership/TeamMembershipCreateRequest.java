package com.nicolasmartineli.taskflow_api.dtos.teammembership;

import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record TeamMembershipCreateRequest(

        @NotNull(message = "User id is required")
        UUID userId,

        @NotNull(message = "Role in team is required")
        TeamRole roleInTeam
) {
}
