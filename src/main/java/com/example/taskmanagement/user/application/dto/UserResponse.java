package com.example.taskmanagement.user.application.dto;

import com.example.taskmanagement.user.domain.model.Permission;
import com.example.taskmanagement.user.domain.model.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponse(
    UUID id,
    String username,
    String email,
    String role,
    boolean active,
    Set<String> permissions
) {
    public static UserResponse fromDomain(User user) {
        Set<String> perms = user.getRole().getPermissions().stream()
            .map(Permission::getValue)
            .collect(Collectors.toSet());

        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            user.isActive(),
            perms
        );
    }
}
