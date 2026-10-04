package com.taskmanagement.application.boundary;

import com.taskmanagement.domain.project.entity.Project;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    void save(Project project);
    Optional<Project> findById(UUID id);
    Project getInbox();
}
