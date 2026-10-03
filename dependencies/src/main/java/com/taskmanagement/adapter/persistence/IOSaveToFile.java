package com.taskmanagement.adapter.persistence;

import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.domain.task.entity.Task;

public class IOSaveToFile implements TaskSaving {
    private final String filePath;

    public IOSaveToFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveTask(Task task) {
        System.out.println("[FILE STORAGE] Writing task '" + task.getTitle().value() + "' to file: " + filePath);
    }
}
