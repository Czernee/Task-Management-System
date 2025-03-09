package com.example.task_management_system.service;

import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.dto.CommentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentDto createComment(int taskId, CommentDto commentDto);
    CommentResponse getCommentsByTaskId(int taskId, String filter, Pageable pageable);
    CommentDto getCommentById(int taskId, int commentId);
    CommentDto updateComment(int taskId, int commentId, CommentDto commentDto);
    void deleteComment(int taskId, int commentId);
}
