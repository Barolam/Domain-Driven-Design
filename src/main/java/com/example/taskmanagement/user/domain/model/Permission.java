package com.example.taskmanagement.user.domain.model;

/**
 * Danh sách các quyền (Permissions) trong hệ thống Task Management
 * Phù hợp với ma trận phân quyền:
 * - Admin: create_task, update_task, delete_task, assign_task
 * - Manager: create_task, update_task, assign_task
 * - Member: view_task, update_own_task
 */
public enum Permission {
    CREATE_TASK("create_task"),
    UPDATE_TASK("update_task"),
    DELETE_TASK("delete_task"),
    ASSIGN_TASK("assign_task"),
    VIEW_TASK("view_task"),
    UPDATE_OWN_TASK("update_own_task");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
