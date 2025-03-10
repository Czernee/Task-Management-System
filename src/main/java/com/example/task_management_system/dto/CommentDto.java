package com.example.task_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    @Schema(description = "Уникальный идентификатор комментария.")
    private Integer id;

    @NotBlank(message = "Comment should not be empty")
    @Schema(description = "Текст комментария. Не может быть пустым.")
    private String text;

    @Schema(description = "Дата и время создания комментария.")
    private LocalDateTime createdDate;

    @Schema(description = "Дата и время последнего обновления комментария.")
    private LocalDateTime updatedDate;
}
