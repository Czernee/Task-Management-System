package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.service.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestParam int authorId,
                                                    @PathVariable("taskId") int taskId,
                                                    @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(authorId, taskId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByTaskId(@PathVariable("taskId") int taskId,
                                                                @RequestParam(required = false) String filter) {
        return new ResponseEntity<>(commentService.getCommentsByTaskId(taskId, filter), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("taskId") int taskId,
                                                     @PathVariable("commentId") int commentId) {
        return new ResponseEntity<>(commentService.getCommentById(taskId, commentId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@RequestParam int authorId,
                                                    @PathVariable("taskId") int taskId,
                                                    @PathVariable("commentId") int commentId,
                                                    @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateComment(authorId, taskId, commentId, commentDto), HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@RequestParam int authorId,
                                                @PathVariable("taskId") int taskId,
                                                @PathVariable("commentId") int commentId) {
        commentService.deleteComment(authorId, taskId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
