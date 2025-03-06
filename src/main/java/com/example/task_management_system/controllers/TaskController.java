package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.dto.TaskFilter;
import com.example.task_management_system.dto.TaskResponse;
import com.example.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDto> createTask(@RequestParam int authorId,
                                              @RequestParam(required = false) int executorId,
                                              @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(authorId, executorId, taskDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<TaskResponse> getAllTasks(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) Integer executorId) {

        TaskFilter filter = new TaskFilter(title, status, priority, authorId, executorId);
        TaskResponse response = taskService.getAllTasks(pageNo, pageSize, filter);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> taskDetail(@PathVariable("id") int taskId) {
        return new ResponseEntity<>(taskService.getTaskById(taskId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") int taskId, @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.updateTask(taskId, taskDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") int taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }
}
