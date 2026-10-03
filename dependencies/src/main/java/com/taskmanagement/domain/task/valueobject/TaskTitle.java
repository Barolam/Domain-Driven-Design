package com.taskmanagement.domain.task.valueobject;

import com.taskmanagement.domain.task.exception.TaskValidationException;

public record TaskTitle(String value) {
    public TaskTitle {
        if (value == null || value.trim().isEmpty()) {
            throw new TaskValidationException("Task title cannot be empty.");
        }
        String trimmed = value.trim();
        if (trimmed.length() < 3 || trimmed.length() > 100) {
            throw new TaskValidationException("Task title must be between 3 and 100 characters.");
        }
        value = trimmed;
    }
}
