package com.taskmanagement.domain.task.entity;

import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.enums.TaskStatus;
import com.taskmanagement.domain.task.exception.InvalidTaskStateTransitionException;
import com.taskmanagement.domain.task.exception.TaskValidationException;
import com.taskmanagement.domain.task.valueobject.Deadline;
import com.taskmanagement.domain.task.valueobject.TaskTitle;

import java.util.UUID;

public class Task extends Entity<UUID> {
    private TaskTitle title;
    private String description;
    private Deadline dueDate;
    private Priority priority;
    private TaskStatus status;
    private UUID projectId;

    public Task(UUID id, TaskTitle title, String description, Deadline dueDate, Priority priority, UUID projectId) {
        super(id != null ? id : UUID.randomUUID());
        validateDescription(description);
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.status = TaskStatus.TODO;
        this.projectId = projectId;
    }

    public Task(String title, String description, Deadline dueDate, Priority priority, UUID projectId) {
        this(UUID.randomUUID(), new TaskTitle(title), description, dueDate, priority, projectId);
    }

    public static Task createUrgentTask(String title, String description, Deadline dueDate, UUID projectId) {
        return new Task(UUID.randomUUID(), new TaskTitle(title), description, dueDate, Priority.HIGH, projectId);
    }

    public void start() {
        if (this.status != TaskStatus.TODO) {
            throw new InvalidTaskStateTransitionException(
                "Cannot start task from status " + this.status + ". Only 'TODO' tasks can be started."
            );
        }
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void complete() {
        if (this.status == TaskStatus.DONE) {
            throw new InvalidTaskStateTransitionException("Task is already completed.");
        }
        if (this.status == TaskStatus.CANCELLED) {
            throw new InvalidTaskStateTransitionException("Cannot complete a cancelled task.");
        }
        if (this.status == TaskStatus.TODO) {
            throw new InvalidTaskStateTransitionException("Task must be started (IN_PROGRESS) before completing.");
        }
        this.status = TaskStatus.DONE;
    }

    public void cancel() {
        if (this.status == TaskStatus.DONE) {
            throw new InvalidTaskStateTransitionException("Cannot cancel a completed task.");
        }
        this.status = TaskStatus.CANCELLED;
    }

    public boolean isOverdue() {
        return dueDate != null && dueDate.isOverdue();
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new TaskValidationException("Description cannot exceed 500 characters.");
        }
    }

    // Getters and Setters
    public TaskTitle getTitle() { return title; }
    public String getDescription() { return description; }
    public Deadline getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public UUID getProjectId() { return projectId; }

    public void setPriority(Priority priority) { this.priority = priority; }
}
