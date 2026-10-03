package com.taskmanagement.application.boundary;

import com.taskmanagement.domain.task.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findAll();
}
