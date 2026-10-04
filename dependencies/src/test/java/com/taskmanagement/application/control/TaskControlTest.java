package com.taskmanagement.application.control;

import com.taskmanagement.adapter.notification.ConsoleNotificationAdapter;
import com.taskmanagement.adapter.persistence.InMemoryProjectRepository;
import com.taskmanagement.adapter.persistence.IOSaveToMemory;
import com.taskmanagement.adapter.presenter.TaskConsolePresenter;
import com.taskmanagement.application.boundary.NotificationPort;
import com.taskmanagement.application.boundary.ProjectRepository;
import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.application.dto.TaskOutputDTO;
import com.taskmanagement.domain.task.enums.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskControlTest {
    private TaskControl taskControl;
    private IOSaveToMemory taskRepository;
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        TaskShowing presenter = new TaskConsolePresenter();
        taskRepository = new IOSaveToMemory();
        projectRepository = new InMemoryProjectRepository();
        NotificationPort notificationPort = new ConsoleNotificationAdapter();

        taskControl = new TaskControl(
            presenter,
            taskRepository,
            taskRepository,
            projectRepository,
            notificationPort
        );
    }

    @Test
    @DisplayName("Should assign task to default INBOX project when projectId is omitted")
    void shouldAssignToDefaultInboxWhenNoProjectIdProvided() {
        TaskInputDTO input = new TaskInputDTO("Tao task khong co project", "Mo ta", null, Priority.HIGH, null);
        TaskOutputDTO output = taskControl.createTask(input);

        assertNotNull(output);
        assertNotNull(output.taskId());
        assertEquals("Tao task khong co project", output.title());
        assertEquals(1, taskRepository.findAll().size());
        assertEquals(1, projectRepository.getInbox().getTasks().size());
    }

    @Test
    @DisplayName("Should orchestrate start and complete task lifecycle")
    void shouldOrchestrateTaskLifecycle() {
        TaskInputDTO input = new TaskInputDTO("Task chu ky", "Mo ta", null, Priority.MEDIUM, null);
        TaskOutputDTO created = taskControl.createTask(input);
        UUID taskId = UUID.fromString(created.taskId());

        TaskOutputDTO started = taskControl.startTask(taskId);
        assertEquals("Dang thuc hien", started.statusDisplay());

        TaskOutputDTO completed = taskControl.completeTask(taskId);
        assertEquals("Da hoan thanh", completed.statusDisplay());
    }
}
