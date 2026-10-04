package com.example.taskmanagement.user.domain.repository;

import com.example.taskmanagement.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain UserRepository Interface (Không dính Spring Data JPA ở đây)
 * Tuân thủ quy tắc Inversion of Control của Clean Architecture.
 */
public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
