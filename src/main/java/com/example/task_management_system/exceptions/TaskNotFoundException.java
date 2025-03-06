<<<<<<< HEAD
package com.example.task_management_system.exceptions;public class TaskNotFoundException {
=======
package com.example.task_management_system.exceptions;

public class TaskNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public TaskNotFoundException(String message) {
        super(message);
    }
>>>>>>> ae658b6423c024ac97b8400fa519d537ebb0be1b
}
