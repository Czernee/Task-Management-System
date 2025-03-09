package com.example.task_management_system.service.impl;

import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.dto.TaskFilter;
import com.example.task_management_system.dto.TaskResponse;
import com.example.task_management_system.exceptions.AccessDeniedException;
import com.example.task_management_system.exceptions.TaskNotFoundException;
import com.example.task_management_system.exceptions.UserNotFoundException;
import com.example.task_management_system.models.Task;
import com.example.task_management_system.models.UserEntity;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.repository.UserRepository;
import com.example.task_management_system.service.TaskService;
import com.example.task_management_system.specifications.TaskSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDto createTask(int executorId, TaskDto taskDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Only admins can create tasks");
        }

        UserEntity executor = userRepository.findById(executorId).orElseThrow(() -> new UserNotFoundException("Executor with this ID can not be found"));

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAuthor(currentUser);
        task.setExecutor(executor);

        Task newTask = taskRepository.save(task);

        TaskDto taskResponse = new TaskDto();
        taskResponse.setId(newTask.getId());
        taskResponse.setTitle(newTask.getTitle());
        taskResponse.setDescription(newTask.getDescription());
        taskResponse.setStatus(newTask.getStatus());
        taskResponse.setPriority(newTask.getPriority());

        return taskResponse;
    }

    @Override
    public TaskResponse getAllTasks(int pageNo, int pageSize, TaskFilter taskFilter) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Specification<Task> spec = TaskSpecifications.getSpecification(taskFilter);

        Page<Task> pageResult = taskRepository.findAll(spec, pageable);

        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setContent(pageResult.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        taskResponse.setPageNo(pageResult.getNumber());
        taskResponse.setPageSize(pageResult.getSize());
        taskResponse.setTotalElements(pageResult.getTotalElements());
        taskResponse.setTotalPages(pageResult.getTotalPages());
        taskResponse.setLast(pageResult.isLast());

        return taskResponse;
    }

    @Override
    public TaskDto getTaskById(int taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));
        return mapToDto(task);
    }

    @Override
    public TaskDto updateTask(int taskId, TaskDto taskDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Only admins or executors can update tasks");
        }

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());

        Task updatedTask = taskRepository.save(task);

        return mapToDto(updatedTask);
    }

    @Override
    public void deleteTask(int taskId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Only admins can delete tasks");
        }

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with this ID can not be found"));
        taskRepository.delete(task);
    }

    private TaskDto mapToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setPriority(task.getPriority());

        return taskDto;
    }
}
