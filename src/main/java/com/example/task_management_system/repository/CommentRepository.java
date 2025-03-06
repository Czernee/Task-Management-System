package com.example.task_management_system.repository;

import com.example.task_management_system.models.Comment;
import com.example.task_management_system.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTaskId(int taskId);
    List<Comment> findByTask(Task task);
}
