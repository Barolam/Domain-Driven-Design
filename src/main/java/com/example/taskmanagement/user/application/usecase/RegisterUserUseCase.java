package com.example.taskmanagement.user.application.usecase;

import com.example.taskmanagement.user.application.dto.RegisterUserCommand;
import com.example.taskmanagement.user.application.dto.UserResponse;
import com.example.taskmanagement.user.application.port.PasswordEncoderPort;
import com.example.taskmanagement.user.domain.model.User;
import com.example.taskmanagement.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse execute(RegisterUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("Username '" + command.username() + "' đã tồn tại!");
        }
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email '" + command.email() + "' đã tồn tại!");
        }

        String hashedPassword = passwordEncoder.encode(command.password());
        User newUser = User.createNew(command.username(), command.email(), hashedPassword, command.role());

        User savedUser = userRepository.save(newUser);
        return UserResponse.fromDomain(savedUser);
    }
}
