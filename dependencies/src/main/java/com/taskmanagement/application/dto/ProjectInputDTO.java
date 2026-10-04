package com.taskmanagement.application.dto;

import com.taskmanagement.domain.project.enums.ProjectType;

public record ProjectInputDTO(
    String name,
    String description,
    ProjectType projectType
) {}
