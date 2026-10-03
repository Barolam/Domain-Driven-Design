package com.taskmanagement.adapter.persistence;

import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.domain.task.entity.Task;

public class IOSaveToSQLite implements TaskSaving {
    @Override
    public void saveTask(Task task) {
        System.out.println("[SQLITE DB] Executing INSERT/UPDATE for task: " + task.getId());
    }
}
