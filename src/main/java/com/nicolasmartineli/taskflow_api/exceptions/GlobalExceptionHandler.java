package com.nicolasmartineli.taskflow_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEmailAlreadyExistsException(DuplicateResourceException e) {
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

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleBusinessRuleException(BusinessRuleException e) {

        return ApiError.conflict(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentials(BadCredentialsException e) {
        return ApiError.unauthorized("Invalid  credentials");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthentication(AuthenticationException e) {
        return ApiError.unauthorized("Authentication is required or the token is invalid");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDenied(AccessDeniedException e) {
        return ApiError.forbidden("You do not have permission to perform this action");
    }

}
