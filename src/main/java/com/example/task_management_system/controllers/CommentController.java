package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.dto.CommentResponse;
import com.example.task_management_system.service.CommentService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable("taskId") int taskId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(taskId, commentDto), HttpStatus.CREATED);
    }

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

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("taskId") int taskId,
                                                     @PathVariable("commentId") int commentId) {
        return new ResponseEntity<>(commentService.getCommentById(taskId, commentId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("taskId") int taskId,
                                                    @PathVariable("commentId") int commentId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateComment(taskId, commentId, commentDto), HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("taskId") int taskId,
                                                @PathVariable("commentId") int commentId) {
        commentService.deleteComment(taskId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
