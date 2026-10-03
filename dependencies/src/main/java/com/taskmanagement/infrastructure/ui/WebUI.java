package com.taskmanagement.infrastructure.ui;

import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.domain.task.enums.Priority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class WebUI {
    private final TaskControl taskControl;

    public WebUI(TaskControl taskControl) {
        this.taskControl = taskControl;
    }

    public void handleHttpRequest() {
        System.out.println("=== CHẠY MÔ PHỎNG WEB REST HTTP REQUEST ===");
        TaskInputDTO input = new TaskInputDTO(
            "Review mã nguồn Java của nhóm",
            "Kiểm tra tính tuân thủ quy tắc Clean Architecture",
            Instant.now().minus(1, ChronoUnit.DAYS), // Quá hạn
            Priority.HIGH,
            UUID.randomUUID()
        );
        taskControl.createTask(input);
    }
}
