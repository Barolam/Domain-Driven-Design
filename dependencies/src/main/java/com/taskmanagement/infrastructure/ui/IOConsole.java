package com.taskmanagement.infrastructure.ui;

import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.domain.task.enums.Priority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class IOConsole {
    private final TaskControl taskControl;

    public IOConsole(TaskControl taskControl) {
        this.taskControl = taskControl;
    }

    public void runDemo() {
        System.out.println("=== CHAY MO PHONG LUONG CONSOLE ===");
        TaskInputDTO input = new TaskInputDTO(
            "Hoan thanh bao cao Clean Architecture",
            "Viet phan phan tich DDD va Clean Architecture bang Java",
            Instant.now().plus(1, ChronoUnit.DAYS),
            Priority.HIGH,
            UUID.randomUUID()
        );

        System.out.println("1. Tao Task moi:");
        var created = taskControl.createTask(input);
        UUID taskId = UUID.fromString(created.taskId());

        System.out.println("2. Bat dau Task (TODO -> IN_PROGRESS):");
        taskControl.startTask(taskId);

        System.out.println("3. Hoan thanh Task (IN_PROGRESS -> DONE):");
        taskControl.completeTask(taskId);
    }
}
