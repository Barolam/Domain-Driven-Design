package com.example.taskmanagement.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginCommand(
    @NotBlank(message = "Username không được để trống")
    String username,

    @NotBlank(message = "Password không được để trống")
    String password
) {}
