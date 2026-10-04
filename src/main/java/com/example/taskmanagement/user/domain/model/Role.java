package com.example.taskmanagement.user.domain.model;

import java.util.Collections;
import java.util.Set;

/**
 * Enum Role và ánh xạ quyền (Permissions) theo ma trận phân quyền:
 * - Admin: create task, update task, delete task, assign task, view task
 * - Manager: create task, update task, assign task, view task
 * - Member: view task, update own task
 */
public enum Role {
    ADMIN(Set.of(
        Permission.CREATE_TASK,
        Permission.UPDATE_TASK,
        Permission.DELETE_TASK,
        Permission.ASSIGN_TASK,
        Permission.VIEW_TASK
    )),
    MANAGER(Set.of(
        Permission.CREATE_TASK,
        Permission.UPDATE_TASK,
        Permission.ASSIGN_TASK,
        Permission.VIEW_TASK
    )),
    MEMBER(Set.of(
        Permission.VIEW_TASK,
        Permission.UPDATE_OWN_TASK
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }
}
