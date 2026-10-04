package com.taskmanagement.application.control;

import com.taskmanagement.application.boundary.ProjectRepository;
import com.taskmanagement.application.dto.ProjectInputDTO;
import com.taskmanagement.application.dto.ProjectOutputDTO;
import com.taskmanagement.domain.project.entity.Project;
import com.taskmanagement.domain.project.enums.ProjectType;

import java.util.UUID;

public class ProjectControl {
    private final ProjectRepository projectRepository;

    public ProjectControl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectOutputDTO createProject(ProjectInputDTO input) {
        ProjectType type = input.projectType() != null ? input.projectType() : ProjectType.REGULAR;
        Project project = new Project(UUID.randomUUID(), input.name(), input.description(), type);
        projectRepository.save(project);
        return mapToDTO(project);
    }

    public ProjectOutputDTO getInboxProject() {
        Project inbox = projectRepository.getInbox();
        return mapToDTO(inbox);
    }

    public ProjectOutputDTO getProject(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        return mapToDTO(project);
    }

    private ProjectOutputDTO mapToDTO(Project project) {
        return new ProjectOutputDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getProjectType(),
            project.getTasks().size()
        );
    }
}
