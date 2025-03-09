package com.example.task_management_system.exceptions;

public class AccessDeniedException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public AccessDeniedException(String message) {
        super(message);
    }
}
