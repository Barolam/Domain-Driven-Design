package com.taskmanagement.infrastructure.ui;

import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.domain.task.enums.Priority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class GUISwing {
    private final TaskControl taskControl;

    public GUISwing(TaskControl taskControl) {
        this.taskControl = taskControl;
    }

    public void simulateUserClick() {
        System.out.println("=== CHẠY MÔ PHỎNG SWING GUI ===");
        TaskInputDTO input = new TaskInputDTO(
            "Thiết kế Sơ đồ UML trên Draw.io",
            "Vẽ mô hình Class Diagram theo chuẩn DIP và ECB",
            Instant.now().plus(3, ChronoUnit.DAYS),
            Priority.MEDIUM,
            UUID.randomUUID()
        );
        taskControl.createTask(input);
    }
}
