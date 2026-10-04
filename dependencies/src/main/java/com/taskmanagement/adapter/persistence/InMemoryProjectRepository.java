package com.taskmanagement.adapter.persistence;

import com.taskmanagement.application.boundary.ProjectRepository;
import com.taskmanagement.domain.project.entity.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProjectRepository implements ProjectRepository {
    private final Map<UUID, Project> db = new HashMap<>();
    private final Project inboxProject;

    public InMemoryProjectRepository() {
        this.inboxProject = Project.createInbox();
        this.db.put(inboxProject.getId(), inboxProject);
    }

    @Override
    public void save(Project project) {
        if (project != null) {
            db.put(project.getId(), project);
        }
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Project getInbox() {
        return inboxProject;
    }

    public List<Project> findAll() {
        return List.copyOf(db.values());
    }
}
