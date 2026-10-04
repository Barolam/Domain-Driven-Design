package com.example.taskmanagement.user.presentation;

import com.example.taskmanagement.user.application.dto.AuthTokenResponse;
import com.example.taskmanagement.user.application.dto.LoginCommand;
import com.example.taskmanagement.user.application.dto.RegisterUserCommand;
import com.example.taskmanagement.user.application.dto.UserResponse;
import com.example.taskmanagement.user.application.usecase.AuthenticateUserUseCase;
import com.example.taskmanagement.user.application.usecase.RegisterUserUseCase;
import com.example.taskmanagement.user.domain.model.User;
import com.example.taskmanagement.user.domain.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserRepository userRepository;

    public AuthController(
        RegisterUserUseCase registerUserUseCase,
        AuthenticateUserUseCase authenticateUserUseCase,
        UserRepository userRepository
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserCommand command) {
        UserResponse response = registerUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginCommand command) {
        AuthTokenResponse response = authenticateUserUseCase.execute(command);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUserInfo(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin user"));
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }
}
