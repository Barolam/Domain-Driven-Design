package com.taskmanagement.domain.project.entity;

import com.taskmanagement.domain.project.enums.ProjectType;
import com.taskmanagement.domain.task.entity.Entity;
import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.domain.task.exception.TaskValidationException;

import java.util.*;

public class Project extends Entity<UUID> {
    private String name;
    private String description;
    private ProjectType projectType;
    private final Map<UUID, Task> tasks = new HashMap<>();

    public Project(UUID id, String name, String description, ProjectType projectType) {
        super(id != null ? id : UUID.randomUUID());
        if (name == null || name.trim().isEmpty()) {
            throw new TaskValidationException("Project name cannot be empty.");
        }
        this.name = name.trim();
        this.description = description;
        this.projectType = projectType != null ? projectType : ProjectType.REGULAR;
    }

    public static Project createInbox() {
        return new Project(UUID.randomUUID(), "INBOX", "Default Inbox Project", ProjectType.INBOX);
    }

    public void addTask(Task task) {
        if (task == null) {
            throw new TaskValidationException("Cannot add null task.");
        }
        // Check for duplicate title within project
        boolean titleExists = tasks.values().stream()
            .anyMatch(t -> t.getTitle().value().equalsIgnoreCase(task.getTitle().value()));
        if (titleExists) {
            throw new TaskValidationException("A task with title '" + task.getTitle().value() + "' already exists in this project.");
        }
        tasks.put(task.getId(), task);
    }

    public void removeTask(UUID taskId) {
        tasks.remove(taskId);
    }

    public Optional<Task> getTask(UUID taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ProjectType getProjectType() { return projectType; }
}
