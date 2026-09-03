package com.nicolasmartineli.taskflow_api.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiError(Integer status, String message, List<ErrorField> errors) {

    public static ApiError notFound(String message) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

    public static ApiError conflict(String message) {
        return new ApiError(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ApiError unprocessableEntity(String message, List<ErrorField> errors) {
        return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), message, errors);

    }
}
