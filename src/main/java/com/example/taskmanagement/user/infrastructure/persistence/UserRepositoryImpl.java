package com.example.taskmanagement.user.infrastructure.persistence;

import com.example.taskmanagement.user.domain.model.User;
import com.example.taskmanagement.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataJpaUserRepository jpaRepository;

    public UserRepositoryImpl(SpringDataJpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    // Mapping helper methods
    private User toDomain(UserJpaEntity entity) {
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPasswordHash(),
            entity.getRole(),
            entity.isActive()
        );
    }

    private UserJpaEntity toEntity(User domain) {
        return new UserJpaEntity(
            domain.getId(),
            domain.getUsername(),
            domain.getEmail(),
            domain.getPasswordHash(),
            domain.getRole(),
            domain.isActive()
        );
    }
}
