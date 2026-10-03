package com.taskmanagement.adapter.api.dto;

import com.taskmanagement.domain.task.enums.Priority;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record TaskCreateRequest(
    @jakarta.validation.constraints.NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title,

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description,

    Instant dueDate,

    Priority priority,

    UUID projectId
) {
}
