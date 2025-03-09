package com.example.task_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Integer id;

    @NotBlank(message = "Comment should not be empty")
    private String text;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
