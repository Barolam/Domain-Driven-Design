package com.taskmanagement.domain.task.enums;

public enum TaskStatus {
    TODO("Cần làm"),
    IN_PROGRESS("Đang thực hiện"),
    DONE("Đã hoàn thành"),
    CANCELLED("Đã hủy");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
