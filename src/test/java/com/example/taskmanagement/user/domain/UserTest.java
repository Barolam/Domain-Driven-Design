package com.example.taskmanagement.user.domain;

import com.example.taskmanagement.user.domain.model.Permission;
import com.example.taskmanagement.user.domain.model.Role;
import com.example.taskmanagement.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Domain & Business Rules Tests")
class UserTest {

    @Test
    @DisplayName("Admin phải có đầy đủ quyền: create, update, delete, assign, view task")
    void adminShouldHaveAllRequiredPermissions() {
        User admin = new User(UUID.randomUUID(), "admin", "admin@test.com", "hash", Role.ADMIN, true);

        assertTrue(admin.hasPermission(Permission.CREATE_TASK));
        assertTrue(admin.hasPermission(Permission.UPDATE_TASK));
        assertTrue(admin.hasPermission(Permission.DELETE_TASK));
        assertTrue(admin.hasPermission(Permission.ASSIGN_TASK));
        assertTrue(admin.hasPermission(Permission.VIEW_TASK));
        assertFalse(admin.hasPermission(Permission.UPDATE_OWN_TASK));
    }

    @Test
    @DisplayName("Manager có quyền create, update, assign, view task nhưng KHÔNG ĐƯỢC delete task")
    void managerShouldHavePermissionsExceptDelete() {
        User manager = new User(UUID.randomUUID(), "manager", "manager@test.com", "hash", Role.MANAGER, true);

        assertTrue(manager.hasPermission(Permission.CREATE_TASK));
        assertTrue(manager.hasPermission(Permission.UPDATE_TASK));
        assertTrue(manager.hasPermission(Permission.ASSIGN_TASK));
        assertTrue(manager.hasPermission(Permission.VIEW_TASK));

        // Manager không có quyền xoá task
        assertFalse(manager.hasPermission(Permission.DELETE_TASK));
    }

    @Test
    @DisplayName("Member chỉ có quyền view task và update own task")
    void memberShouldOnlyHaveViewAndUpdateOwnTaskPermissions() {
        User member = new User(UUID.randomUUID(), "member", "member@test.com", "hash", Role.MEMBER, true);

        assertTrue(member.hasPermission(Permission.VIEW_TASK));
        assertTrue(member.hasPermission(Permission.UPDATE_OWN_TASK));

        // Member không có quyền tạo, xoá, hoặc assign task
        assertFalse(member.hasPermission(Permission.CREATE_TASK));
        assertFalse(member.hasPermission(Permission.UPDATE_TASK));
        assertFalse(member.hasPermission(Permission.DELETE_TASK));
        assertFalse(member.hasPermission(Permission.ASSIGN_TASK));
    }

    @Test
    @DisplayName("User bị vô hiệu hóa (active = false) thì không có bất kỳ quyền nào")
    void deactivatedUserShouldNotHaveAnyPermission() {
        User inactiveAdmin = new User(UUID.randomUUID(), "admin", "admin@test.com", "hash", Role.ADMIN, false);

        assertFalse(inactiveAdmin.hasPermission(Permission.CREATE_TASK));
        assertFalse(inactiveAdmin.hasPermission(Permission.DELETE_TASK));
        assertFalse(inactiveAdmin.canBeAssignedTask());
    }

    @Test
    @DisplayName("Chỉ User đang active mới có thể được phân công nhận Task")
    void onlyActiveUserCanBeAssignedTask() {
        User activeUser = new User(UUID.randomUUID(), "user1", "u1@test.com", "hash", Role.MEMBER, true);
        User inactiveUser = new User(UUID.randomUUID(), "user2", "u2@test.com", "hash", Role.MEMBER, false);

        assertTrue(activeUser.canBeAssignedTask());
        assertFalse(inactiveUser.canBeAssignedTask());
    }
}
