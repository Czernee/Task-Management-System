package com.example.task_management_system.exceptions;

public class TaskNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3;

    public TaskNotFoundException(String message) {
        super(message);
    }
}
