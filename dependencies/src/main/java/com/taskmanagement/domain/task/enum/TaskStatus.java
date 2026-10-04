package com.taskmanagement.domain.task.enums;

public enum TaskStatus {
    TODO("Can lam"),
    IN_PROGRESS("Dang thuc hien"),
    DONE("Da hoan thanh"),
    CANCELLED("Da huy");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
