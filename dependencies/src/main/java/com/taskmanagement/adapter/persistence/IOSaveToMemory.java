package com.taskmanagement.adapter.persistence;

import com.taskmanagement.application.boundary.TaskRepository;
import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.domain.task.entity.Task;

import java.util.*;

public class IOSaveToMemory implements TaskSaving, TaskRepository {
    private final Map<UUID, Task> db = new HashMap<>();

    @Override
    public void saveTask(Task task) {
        save(task);
        System.out.println("[MEMORY DB] Saved task: " + task.getTitle().value() + " (ID: " + task.getId() + ")");
    }

    @Override
    public void save(Task task) {
        db.put(task.getId(), task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Task> findAll() {
        return List.copyOf(db.values());
    }
}
