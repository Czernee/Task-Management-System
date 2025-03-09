package com.example.task_management_system.repository;

import com.example.task_management_system.dto.CommentResponse;
import com.example.task_management_system.models.Comment;
import com.example.task_management_system.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByTask(Task task, Pageable pageable);

    Page<Comment> findByTaskOrderByCreatedDateDesc(Task task, Pageable pageable);

    Page<Comment> findByTaskOrderByCreatedDateAsc(Task task, Pageable pageable);
}
