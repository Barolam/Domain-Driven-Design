package com.taskmanagement.domain.service;

import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.domain.task.enums.Priority;

import java.time.Duration;

public class TaskPriorityCalculator {
    public Priority calculatePriority(Task task) {
        if (task == null) return Priority.LOW;

        if (task.isOverdue()) {
            return Priority.HIGH;
        }

        if (task.getDueDate() != null && task.getDueDate().isApproaching(Duration.ofDays(2))) {
            return Priority.MEDIUM;
        }

        return Priority.LOW;
    }
}
