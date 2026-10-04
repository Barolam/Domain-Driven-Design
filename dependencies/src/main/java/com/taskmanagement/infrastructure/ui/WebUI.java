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
        System.out.println("=== CHAY MO PHONG WEB REST HTTP REQUEST ===");
        TaskInputDTO input = new TaskInputDTO(
            "Review ma nguon Java cua nhom",
            "Kiem tra tinh tuan thu quy tac Clean Architecture",
            Instant.now().minus(1, ChronoUnit.DAYS),
            Priority.HIGH,
            UUID.randomUUID()
        );
        taskControl.createTask(input);
    }
}
