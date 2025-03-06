package com.example.task_management_system.exceptions;

public class CommentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public CommentNotFoundException(String message) {
        super(message);
    }
}
