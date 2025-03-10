package com.example.task_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @Schema(description = "Email пользователя.")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Пароль пользователя.")
    @NotBlank(message = "Password is required")
    private String password;
}
