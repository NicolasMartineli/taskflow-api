package com.nicolasmartineli.taskflow_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ApiError.conflict(e.getMessage());

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerResourceNotFoundException(ResourceNotFoundException e) {
        return ApiError.notFound(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<ErrorField> errors = e.getFieldErrors().stream()
                .map(error -> new ErrorField(error.getField(), error.getDefaultMessage())).toList();

        return ApiError.unprocessableEntity("Validation error", errors);


    }

}
