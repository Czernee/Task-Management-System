package com.example.task_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    @Schema(description = "Список комментариев.")
    private List<CommentDto> content;

    @Schema(description = "Номер текущей страницы.")
    private int pageNo;

    @Schema(description = "Количество элементов на странице.")
    private int pageSize;

    @Schema(description = "Количество элементов на странице.")
    private long totalElements;

    @Schema(description = "Общее количество страниц.")
    private int totalPages;

    @Schema(description = "Флаг, указывающий, является ли текущая страница последней.")
    private boolean last;
}
