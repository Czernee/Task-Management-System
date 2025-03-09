package com.example.task_management_system.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4;

    public UserNotFoundException(String message) {
        super(message);
    }
}
