package com.example.task_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    @Schema(description = "Список задач.")
    private List<TaskDto> content;

    @Schema(description = "Номер текущей страницы.")
    private int pageNo;

    @Schema(description = "Количество элементов на странице.")
    private int pageSize;

    @Schema(description = "Общее количество задач.")
    private long totalElements;

    @Schema(description = "Общее количество страниц.")
    private int totalPages;

    @Schema(description = "Флаг, указывающий, является ли текущая страница последней.")
    private boolean last;
}
