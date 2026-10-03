package com.taskmanagement.application.dto;

import java.time.Instant;

public record TaskOutputDTO(
    String taskId,
    String title,
    String description,
    String statusDisplay,
    String priorityDisplay,
    Instant dueDate,
    boolean isOverdue
) {}
