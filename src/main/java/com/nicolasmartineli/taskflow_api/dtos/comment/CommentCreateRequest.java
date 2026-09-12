package com.nicolasmartineli.taskflow_api.dtos.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "Comment text is required")
        String text
) {
}
