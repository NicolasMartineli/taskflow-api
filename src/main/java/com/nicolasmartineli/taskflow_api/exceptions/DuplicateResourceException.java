package com.nicolasmartineli.taskflow_api.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resource, String field, Object value) {
        super(resource + " already exists with " + field + ": " + value);
    }

}

