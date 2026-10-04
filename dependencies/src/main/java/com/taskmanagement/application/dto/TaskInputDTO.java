package com.taskmanagement.application.dto;

import com.taskmanagement.domain.task.enums.Priority;

import java.time.Instant;
import java.util.UUID;

public record TaskInputDTO(
    String title,
    String description,
    Instant dueDate,
    Priority priority,
    UUID projectId
) {}
