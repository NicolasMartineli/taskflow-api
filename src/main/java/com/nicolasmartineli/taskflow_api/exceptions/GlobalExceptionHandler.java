package com.nicolasmartineli.taskflow_api.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEmailAlreadyExistsException(DuplicateResourceException e) {
        log.warn("Duplicate resource: reason={}", e.getMessage());
        return ApiError.conflict(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerResourceNotFoundException(ResourceNotFoundException e) {
        log.debug("Resource not found: reason={}", e.getMessage());
        return ApiError.notFound(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorField> errors = e.getFieldErrors().stream()
                .map(error -> new ErrorField(error.getField(), error.getDefaultMessage())).toList();

        log.debug("Validation error: fieldsCount={}", errors.size());
        return ApiError.unprocessableEntity("Validation error", errors);
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleBusinessRuleException(BusinessRuleException e) {
        log.warn("Business rule violation: reason={}", e.getMessage());
        return ApiError.conflict(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentials(BadCredentialsException e) {
        log.warn("Authentication failed: invalid credentials");
        return ApiError.unauthorized("Invalid credentials");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthentication(AuthenticationException e) {
        log.warn("Authentication failed: reason={}", e.getMessage());
        return ApiError.unauthorized("Authentication is required or the token is invalid");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: reason={}", e.getMessage());
        return ApiError.forbidden("You do not have permission to perform this action");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.debug("Malformed JSON request: reason={}", e.getMessage());
        return ApiError.badRequest("Malformed JSON request");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.debug("Parameter type mismatch: parameter={}, value={}", e.getName(), e.getValue());
        return ApiError.badRequest(String.format("Invalid value '%s' for parameter '%s'", e.getValue(), e.getName()));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiError> handleSpringErrorResponse(ErrorResponseException e) {
        HttpStatusCode status = e.getStatusCode();
        String detail = e.getBody().getDetail() != null ? e.getBody().getDetail() : e.getMessage();
        return ResponseEntity.status(status)
                .body(new ApiError(status.value(), detail, List.of()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpectedException(Exception e) {
        log.error("Unexpected internal error", e);
        return ApiError.internalServerError("An unexpected error occurred");
    }

}