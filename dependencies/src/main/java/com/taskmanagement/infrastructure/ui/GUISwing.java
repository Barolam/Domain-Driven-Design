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
        System.out.println("=== CHAY MO PHONG SWING GUI ===");
        TaskInputDTO input = new TaskInputDTO(
            "Thiet ke So do UML tren Draw.io",
            "Ve mo hinh Class Diagram theo chuan DIP va ECB",
            Instant.now().plus(3, ChronoUnit.DAYS),
            Priority.MEDIUM,
            UUID.randomUUID()
        );
        taskControl.createTask(input);
    }
}
