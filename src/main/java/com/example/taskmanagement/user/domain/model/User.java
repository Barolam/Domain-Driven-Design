package com.example.taskmanagement.user.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * User Entity - Tầng Domain Lõi (Pure Java, không phụ thuộc Framework hay Database)
 * Chứa logic nghiệp vụ cốt lõi: kiểm tra quyền, trạng thái kích hoạt, điều kiện phân công.
 */
public class User {
    private final UUID id;
    private final String username;
    private final String email;
    private String passwordHash;
    private Role role;
    private boolean active;

    public User(UUID id, String username, String email, String passwordHash, Role role, boolean active) {
        this.id = id != null ? id : UUID.randomUUID();
        this.username = Objects.requireNonNull(username, "Username không được để trống");
        this.email = Objects.requireNonNull(email, "Email không được để trống");
        this.passwordHash = Objects.requireNonNull(passwordHash, "PasswordHash không được để trống");
        this.role = Objects.requireNonNull(role, "Role không được để trống");
        this.active = active;
    }

    public static User createNew(String username, String email, String passwordHash, Role role) {
        return new User(UUID.randomUUID(), username, email, passwordHash, role, true);
    }

    // --- Domain Business Rules ---

    /**
     * Kiểm tra xem user có quyền thực hiện một hành động cụ thể không.
     * Quy tắc nghiệp vụ: Chỉ user đang hoạt động (active = true) mới có quyền.
     */
    public boolean hasPermission(Permission permission) {
        if (!this.active) {
            return false;
        }
        return this.role.getPermissions().contains(permission);
    }

    /**
     * Quy tắc nghiệp vụ phân công (Assignment):
     * Chỉ những user đang active mới được nhận task.
     */
    public boolean canBeAssignedTask() {
        return this.active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void changeRole(Role newRole) {
        this.role = Objects.requireNonNull(newRole, "Role mới không được để trống");
    }

    // --- Getters ---
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
