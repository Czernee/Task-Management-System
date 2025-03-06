package com.example.task_management_system.service.impl;

import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.dto.TaskFilter;
import com.example.task_management_system.dto.TaskResponse;
import com.example.task_management_system.exceptions.TaskNotFoundException;
import com.example.task_management_system.exceptions.UserNotFoundException;
import com.example.task_management_system.models.Task;
import com.example.task_management_system.models.User;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.repository.UserRepository;
import com.example.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public TaskDto createTask(int authorId, int executorId, TaskDto taskDto) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException("Author with this ID can not be found"));
        User executor = userRepository.findById(executorId).orElseThrow(() -> new UserNotFoundException("Executor with this ID can not be found"));

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAuthor(author);
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

        Specification<Task> spec = getSpecification(taskFilter);

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

    private Specification<Task> getSpecification(TaskFilter filter) {
        Specification<Task> spec = Specification.where(null);

        if (filter.getTitle() != null) {
            spec = spec.and(titleContains(filter.getTitle()));
        }

        if (filter.getStatus() != null) {
            spec = spec.and(statusEquals(filter.getStatus()));
        }

        if (filter.getPriority() != null) {
            spec = spec.and(priorityEquals(filter.getPriority()));
        }

        if (filter.getAuthorId() != null) {
            spec = spec.and(authorEquals(Long.valueOf(filter.getAuthorId())));
        }

        if (filter.getExecutorId() != null) {
            spec = spec.and(executorEquals(Long.valueOf(filter.getExecutorId())));
        }

        return spec;
    }

    private Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    private Specification<Task> statusEquals(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    private Specification<Task> priorityEquals(String priority) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
    }

    private Specification<Task> authorEquals(Long authorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("author").get("id"), authorId);
    }

    private Specification<Task> executorEquals(Long executorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("executor").get("id"), executorId);
    }
}
