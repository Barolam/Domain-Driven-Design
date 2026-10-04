package com.example.taskmanagement.user.application.port;

import com.example.taskmanagement.user.domain.model.User;

import java.util.UUID;

public interface TokenProviderPort {
    String generateToken(User user);
    UUID extractUserId(String token);
    boolean validateToken(String token);
}
