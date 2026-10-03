package com.taskmanagement.domain.task.valueobject;

import com.taskmanagement.domain.task.exception.TaskValidationException;

import java.time.Duration;
import java.time.Instant;

public record Deadline(Instant dueDate) {
    public Deadline {
        if (dueDate == null) {
            throw new TaskValidationException("Due date cannot be null.");
        }
    }

    public boolean isOverdue() {
        return Instant.now().isAfter(dueDate);
    }

    public Duration timeRemaining() {
        return Duration.between(Instant.now(), dueDate);
    }

    public boolean isApproaching(Duration threshold) {
        Duration remaining = timeRemaining();
        return !remaining.isNegative() && remaining.compareTo(threshold) <= 0;
    }
}
