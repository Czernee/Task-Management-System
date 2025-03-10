package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.dto.CommentResponse;
import com.example.task_management_system.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment Controller", description = "API для управления комментариями")
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Создать новый комментарий",
            description = "Метод для создания нового комментария к задаче.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Комментарий создан успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable("taskId") int taskId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(taskId, commentDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Получить комментарии к задаче. Доступно и администраторам, и обычным пользователям.",
            description = "Метод для получения комментариев к задаче с возможностью фильтрации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии получены успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<CommentResponse> getCommentsByTaskId(
                                                    @PathVariable int taskId,
                                                    @RequestParam(required = false) String filter,
                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        CommentResponse response = commentService.getCommentsByTaskId(taskId, filter, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Получить комментарий по ID",
            description = "Метод для получения комментария по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий получен успешно"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("taskId") int taskId,
                                                     @PathVariable("commentId") int commentId) {
        return new ResponseEntity<>(commentService.getCommentById(taskId, commentId), HttpStatus.OK);
    }

    @Operation(summary = "Обновить комментарий. Доступно для обновления только автору комментария.",
            description = "Метод для обновления существующего комментария.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий обновлен успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("taskId") int taskId,
                                                    @PathVariable("commentId") int commentId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateComment(taskId, commentId, commentDto), HttpStatus.OK);
    }

    @Operation(summary = "Удалить комментарий. Доступно для удаления только администратору и автору комментария.",
            description = "Метод для удаления комментария по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий удален успешно"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("taskId") int taskId,
                                                @PathVariable("commentId") int commentId) {
        commentService.deleteComment(taskId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}