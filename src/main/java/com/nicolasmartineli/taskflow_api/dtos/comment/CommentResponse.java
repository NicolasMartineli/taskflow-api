package com.nicolasmartineli.taskflow_api.dtos.comment;

import com.nicolasmartineli.taskflow_api.dtos.user.UserSummary;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String text,
        UserSummary author,
        LocalDateTime createdAt
) {
}
