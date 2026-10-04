package com.taskmanagement.application.dto;

import com.taskmanagement.domain.project.enums.ProjectType;

import java.util.UUID;

public record ProjectOutputDTO(
    UUID id,
    String name,
    String description,
    ProjectType projectType,
    int taskCount
) {}
