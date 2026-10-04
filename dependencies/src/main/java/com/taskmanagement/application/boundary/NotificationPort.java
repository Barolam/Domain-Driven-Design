package com.taskmanagement.application.boundary;

import com.taskmanagement.domain.task.entity.Task;

public interface NotificationPort {
    void notifyTaskCreated(Task task);
    void notifyTaskCompleted(Task task);
    void notifyHighPriorityTask(Task task);
}
