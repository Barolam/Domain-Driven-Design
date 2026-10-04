package com.taskmanagement.adapter.notification;

import com.taskmanagement.application.boundary.NotificationPort;
import com.taskmanagement.domain.task.entity.Task;

public class ConsoleNotificationAdapter implements NotificationPort {
    @Override
    public void notifyTaskCreated(Task task) {
        System.out.println("[NOTIFICATION] Task created: '" + task.getTitle().value() + "' (ID: " + task.getId() + ")");
    }

    @Override
    public void notifyTaskCompleted(Task task) {
        System.out.println("[NOTIFICATION] Task completed: '" + task.getTitle().value() + "'!");
    }

    @Override
    public void notifyHighPriorityTask(Task task) {
        System.out.println("[NOTIFICATION ALERT] High priority task assigned: '" + task.getTitle().value() + "'!");
    }
}
