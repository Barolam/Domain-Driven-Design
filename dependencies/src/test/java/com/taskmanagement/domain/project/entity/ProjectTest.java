package com.taskmanagement.domain.project.entity;

import com.taskmanagement.domain.project.enums.ProjectType;
import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.exception.TaskValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    @DisplayName("Should create INBOX project correctly as factory method")
    void shouldCreateInboxProject() {
        Project inbox = Project.createInbox();
        assertNotNull(inbox.getId());
        assertEquals("INBOX", inbox.getName());
        assertEquals(ProjectType.INBOX, inbox.getProjectType());
    }

    @Test
    @DisplayName("Should add task into project aggregate successfully")
    void shouldAddTaskToProject() {
        Project project = new Project(UUID.randomUUID(), "Du an A", "Mo ta A", ProjectType.REGULAR);
        Task task = new Task("Task 1", "Desc", null, Priority.MEDIUM, project.getId());

        project.addTask(task);

        assertEquals(1, project.getTasks().size());
        assertTrue(project.getTask(task.getId()).isPresent());
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate task title into the same project")
    void shouldPreventDuplicateTaskTitleInSameProject() {
        Project project = new Project(UUID.randomUUID(), "Du an A", "Mo ta A", ProjectType.REGULAR);
        Task task1 = new Task("Task 1", "Desc 1", null, Priority.MEDIUM, project.getId());
        Task task2 = new Task("Task 1", "Desc 2", null, Priority.LOW, project.getId());

        project.addTask(task1);

        assertThrows(TaskValidationException.class, () -> project.addTask(task2));
    }
}
