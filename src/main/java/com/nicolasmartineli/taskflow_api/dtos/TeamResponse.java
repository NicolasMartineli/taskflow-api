package com.nicolasmartineli.taskflow_api.dtos;

import java.util.List;
import java.util.UUID;

public record TeamResponse(
        UUID id,
        String name,
        List<TeamMemberResponse> members

) {
}
