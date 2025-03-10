package com.example.task_management_system.service.impl;

import ch.qos.logback.core.util.Loader;
import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.dto.CommentResponse;
import com.example.task_management_system.exceptions.AccessDeniedException;
import com.example.task_management_system.exceptions.CommentNotFoundException;
import com.example.task_management_system.exceptions.TaskNotFoundException;
import com.example.task_management_system.exceptions.UserNotFoundException;
import com.example.task_management_system.models.Comment;
import com.example.task_management_system.models.Task;
import com.example.task_management_system.models.UserEntity;
import com.example.task_management_system.repository.CommentRepository;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.repository.UserRepository;
import com.example.task_management_system.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(int taskId, CommentDto commentDto) {
        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        if (!currentUser.getRole().getName().equals("ADMIN") && !task.getExecutor().equals(currentUser)) {
            throw new AccessDeniedException("You are not the executor of this task or admin");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setTask(task);
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);

        return mapToDto(savedComment);
    }

    @Override
    public CommentResponse getCommentsByTaskId(int taskId, String filter, Pageable pageable) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Page<Comment> pageResult;

        if (filter != null) {
            if (filter.equals("new")) {
                pageResult = commentRepository.findByTaskOrderByCreatedDateDesc(task, pageable);
            } else if (filter.equals("old")) {
                pageResult = commentRepository.findByTaskOrderByCreatedDateAsc(task, pageable);
            } else {
                pageResult = commentRepository.findByTask(task, pageable);
            }
        } else {
            pageResult = commentRepository.findByTask(task, pageable);
        }

        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setContent(pageResult.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        commentResponse.setPageNo(pageResult.getNumber());
        commentResponse.setPageSize(pageResult.getSize());
        commentResponse.setTotalElements(pageResult.getTotalElements());
        commentResponse.setTotalPages(pageResult.getTotalPages());
        commentResponse.setLast(pageResult.isLast());

        return commentResponse;
    }


    @Override
    public CommentDto getCommentById(int taskId, int commentId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this ID can not be found"));

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(int taskId, int commentId, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this ID can not be found"));

        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!comment.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("You are not the author of this comment");
        }

        comment.setText(commentDto.getText());
        comment.setUpdatedDate(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(int taskId, int commentId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this ID can not be found"));

        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN") && !comment.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("You are not the author of this comment or admin");
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
