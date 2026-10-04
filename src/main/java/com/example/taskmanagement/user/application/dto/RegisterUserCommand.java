package com.example.taskmanagement.user.application.dto;

import com.example.taskmanagement.user.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserCommand(
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự")
    String username,

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email,

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải tối thiểu 6 ký tự")
    String password,

    @NotNull(message = "Role không được để trống")
    Role role
) {}
