package com.example.task_management_system.specifications;

import com.example.task_management_system.dto.TaskFilter;
import com.example.task_management_system.models.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> getSpecification(TaskFilter filter) {
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

    private static Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    private static Specification<Task> statusEquals(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    private static Specification<Task> priorityEquals(String priority) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
    }

    private static Specification<Task> authorEquals(Long authorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("author").get("id"), authorId);
    }

    private static Specification<Task> executorEquals(Long executorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("executor").get("id"), executorId);
    }
}

