package com.example.task_management_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterDto {

    @Schema(description = "Email пользователя.")
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Schema(description = "Пароль пользователя.")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "Флаг, указывающий, является ли пользователь администратором.")
    @NotNull
    @JsonProperty("isAdmin")
    private boolean isAdmin;
}
