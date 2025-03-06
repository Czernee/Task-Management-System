package com.example.task_management_system.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilter {
    private String title;
    private String status;
    private String priority;
    private Integer authorId;
    private Integer executorId;
}
