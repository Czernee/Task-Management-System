package com.example.task_management_system.exceptions;

public class TaskNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public TaskNotFoundException(String message) {
        super(message);
    }
}
