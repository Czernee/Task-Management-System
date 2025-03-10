package com.example.task_management_system.dto;

import com.example.task_management_system.models.TaskPriority;
import com.example.task_management_system.models.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    @Schema(description = "Уникальный идентификатор задания.")
    private Integer id;

    @NotBlank(message = "Title is required")
    @Schema(description = "Заголовок задания. Не может быть пустым")
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "Описание задания. Не может быть пустым")
    private String description;

    @NotNull(message = "Status is required")
    @Schema(description = "Статус задания. Не может быть пустым")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NotNull(message = "Priority is required")
    @Schema(description = "Приоритет задания. Не может быть пустым")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
}
