package com.example.task_management_system.service.impl;

import ch.qos.logback.core.util.Loader;
import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.exceptions.CommentNotFoundException;
import com.example.task_management_system.exceptions.TaskNotFoundException;
import com.example.task_management_system.exceptions.UserNotFoundException;
import com.example.task_management_system.models.Comment;
import com.example.task_management_system.models.Task;
import com.example.task_management_system.models.User;
import com.example.task_management_system.repository.CommentRepository;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.repository.UserRepository;
import com.example.task_management_system.service.CommentService;
import com.example.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(int authorId, int taskId, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedDate(LocalDateTime.now());

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        User author = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException("User with this ID can not be found"));

        comment.setTask(task);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);

        return mapToDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByTaskId(int taskId, String filter) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        List<Comment> comments = commentRepository.findByTask(task);

        if (filter != null) {
            if (filter.equals("new")) {
                comments = comments.stream().sorted(Comparator.comparing(Comment::getCreatedDate).reversed())
                        .collect(Collectors.toList());
            } else if (filter.equals("old")) {
                comments = comments.stream().sorted(Comparator.comparing(Comment::getCreatedDate))
                        .collect(Collectors.toList());
            }
        }

        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    @Override
    public CommentDto getCommentById(int taskId, int commentId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this ID can not be found"));

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(int authorId, int taskId, int commentId, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this ID can not be found"));

        if (comment.getAuthor().getId() != authorId) {
            throw new CommentNotFoundException("This comment does not belong to this author");
        }

        comment.setText(commentDto.getText());
        comment.setUpdatedDate(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(int authorId,int taskId, int commentId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this ID can not be found"));

        if (comment.getAuthor().getId() != authorId) {
            throw new CommentNotFoundException("This comment does not belong to this author");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setCreatedDate(comment.getCreatedDate());
        commentDto.setUpdatedDate(comment.getUpdatedDate());
        return commentDto;
    }
}
