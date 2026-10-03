package com.taskmanagement.application.boundary;

import com.taskmanagement.domain.task.entity.Task;

public interface TaskSaving {
    void saveTask(Task task);
}
