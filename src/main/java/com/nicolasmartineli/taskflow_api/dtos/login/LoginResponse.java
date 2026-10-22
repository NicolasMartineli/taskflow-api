package com.nicolasmartineli.taskflow_api.dtos.login;

public record LoginResponse(

        String accessToken,
        String tokenType,
        long expiresIn
) {
}
