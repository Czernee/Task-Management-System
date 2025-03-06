package com.example.task_management_system.service;

import com.example.task_management_system.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(int authorId, int taskId, CommentDto commentDto);
    List<CommentDto> getCommentsByTaskId(int taskId, String filter);
    CommentDto getCommentById(int taskId, int commentId);
    CommentDto updateComment(int authorId, int taskId, int commentId, CommentDto commentDto);
    void deleteComment(int authorId, int taskId, int commentId);
}
