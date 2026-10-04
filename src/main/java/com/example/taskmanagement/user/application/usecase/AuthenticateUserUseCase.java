package com.example.taskmanagement.user.application.usecase;

import com.example.taskmanagement.user.application.dto.AuthTokenResponse;
import com.example.taskmanagement.user.application.dto.LoginCommand;
import com.example.taskmanagement.user.application.port.PasswordEncoderPort;
import com.example.taskmanagement.user.application.port.TokenProviderPort;
import com.example.taskmanagement.user.domain.model.User;
import com.example.taskmanagement.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;

    public AuthenticateUserUseCase(
        UserRepository userRepository,
        PasswordEncoderPort passwordEncoder,
        TokenProviderPort tokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthTokenResponse execute(LoginCommand command) {
        User user = userRepository.findByUsername(command.username())
            .orElseThrow(() -> new IllegalArgumentException("Tài khoản hoặc mật khẩu không chính xác"));

        if (!user.isActive()) {
            throw new IllegalStateException("Tài khoản đã bị vô hiệu hóa");
        }

        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Tài khoản hoặc mật khẩu không chính xác");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthTokenResponse(token, user.getId(), user.getUsername(), user.getRole().name());
    }
}
