package com.taskmanagement.infrastructure.ui;

import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.application.dto.TaskOutputDTO;
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
        System.out.println("=== CHẠY MÔ PHỎNG LUỒNG CONSOLE ===");
        TaskInputDTO input = new TaskInputDTO(
            "Hoàn thành báo cáo Clean Architecture",
            "Viết phần phân tích DDD và Clean Architecture bằng Java",
            Instant.now().plus(1, ChronoUnit.DAYS),
            Priority.HIGH,
            UUID.randomUUID()
        );

        System.out.println("1. Tạo Task mới:");
        TaskOutputDTO created = taskControl.createTask(input);
        UUID taskId = UUID.fromString(created.taskId());

        System.out.println("2. Bắt đầu Task (TODO -> IN_PROGRESS):");
        taskControl.startTask(taskId);

        System.out.println("3. Hoàn thành Task (IN_PROGRESS -> DONE):");
        taskControl.completeTask(taskId);
    }
}
