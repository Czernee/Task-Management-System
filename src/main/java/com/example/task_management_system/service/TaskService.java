package com.example.task_management_system.service;

import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.dto.TaskFilter;
import com.example.task_management_system.dto.TaskResponse;

public interface TaskService {
    TaskDto createTask(int executorId, TaskDto taskDto);
    TaskResponse getAllTasks(int pageNo, int pageSize, TaskFilter taskFilter);
    TaskDto getTaskById(int taskId);
    TaskDto updateTask(int taskId, TaskDto taskDto);
    void deleteTask(int taskId);
}
