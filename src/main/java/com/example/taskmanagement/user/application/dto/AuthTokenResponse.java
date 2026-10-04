package com.example.taskmanagement.user.application.dto;

import java.util.UUID;

public record AuthTokenResponse(
    String accessToken,
    String tokenType,
    UUID userId,
    String username,
    String role
) {
    public AuthTokenResponse(String accessToken, UUID userId, String username, String role) {
        this(accessToken, "Bearer", userId, username, role);
    }
}
