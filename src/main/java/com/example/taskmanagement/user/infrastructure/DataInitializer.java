package com.example.taskmanagement.user.infrastructure;

import com.example.taskmanagement.user.application.port.PasswordEncoderPort;
import com.example.taskmanagement.user.domain.model.Role;
import com.example.taskmanagement.user.domain.model.User;
import com.example.taskmanagement.user.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Tự động khởi tạo 3 tài khoản mặc định đại diện cho 3 Role:
 * - admin / admin123 (Role: ADMIN)
 * - manager / manager123 (Role: MANAGER)
 * - member / member123 (Role: MEMBER)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createIfNotExists("admin", "admin@taskmanagement.com", "admin123", Role.ADMIN);
        createIfNotExists("manager", "manager@taskmanagement.com", "manager123", Role.MANAGER);
        createIfNotExists("member", "member@taskmanagement.com", "member123", Role.MEMBER);
        log.info("==> Đã khởi tạo xong dữ liệu mẫu (admin/manager/member)!");
    }

    private void createIfNotExists(String username, String email, String rawPassword, Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.createNew(
                username,
                email,
                passwordEncoder.encode(rawPassword),
                role
            );
            userRepository.save(user);
            log.info("Khởi tạo tài khoản: [{}] - Role: [{}]", username, role);
        }
    }
}
