package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.dto.TaskFilter;
import com.example.task_management_system.dto.TaskResponse;
import com.example.task_management_system.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task Controller", description = "Управление задачами")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Создать новую задачу",
            description = "Метод для создания новой задачи. Принимает JSON-объект с данными задачи. Создавать может только пользователь с ролью администратора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача создана успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDto> createTask(@RequestParam(required = false, defaultValue = "0") int executorId,
                                              @Valid @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(executorId, taskDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Получить все задачи",
            description = "Метод для получения списка всех задач с возможностью фильтрации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач получен успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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

    @Operation(summary = "Получить задачу по ID",
            description = "Метод для получения задачи по ее ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача получена успешно"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> taskDetail(@PathVariable("id") int taskId) {
        return new ResponseEntity<>(taskService.getTaskById(taskId), HttpStatus.OK);
    }

    @Operation(summary = "Обновить задачу",
            description = "Метод для обновления существующей задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача обновлена успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") int taskId, @Valid @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.updateTask(taskId, taskDto), HttpStatus.OK);
    }

    @Operation(summary = "Удалить задачу",
            description = "Метод для удаления задачи по ее ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача удалена успешно"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") int taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }
}
