package com.nicolasmartineli.taskflow_api.dtos.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CommentCreateRequest(
        @NotBlank(message = "Comment text is required")
        String text,

        // TODO: remove authorId from request once Security is implemented — get from authenticated user instead
        @NotNull(message = "Author id is required")
        UUID authorId
) {
}
