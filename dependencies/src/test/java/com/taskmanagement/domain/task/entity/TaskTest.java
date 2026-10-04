package com.taskmanagement.domain.task.entity;

import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.enums.TaskStatus;
import com.taskmanagement.domain.task.exception.InvalidTaskStateTransitionException;
import com.taskmanagement.domain.task.exception.TaskValidationException;
import com.taskmanagement.domain.task.valueobject.Deadline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private UUID projectId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should create Task with valid parameters and default TODO status")
    void shouldCreateTaskSuccessfully() {
        Deadline deadline = new Deadline(Instant.now().plus(2, ChronoUnit.DAYS));
        Task task = new Task("Hoc Clean Architecture", "Doc cac chuong sach Clean Architecture", deadline, Priority.HIGH, projectId);

        assertNotNull(task.getId());
        assertEquals("Hoc Clean Architecture", task.getTitle().value());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(projectId, task.getProjectId());
        assertFalse(task.isOverdue());
    }

    @Test
    @DisplayName("Should transition from TODO to IN_PROGRESS")
    void shouldStartTaskSuccessfully() {
        Task task = new Task("Task 1", "Desc", null, Priority.MEDIUM, projectId);
        assertEquals(TaskStatus.TODO, task.getStatus());

        task.start();
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("Should transition from IN_PROGRESS to DONE")
    void shouldCompleteTaskSuccessfully() {
        Task task = new Task("Task 1", "Desc", null, Priority.MEDIUM, projectId);
        task.start();
        task.complete();

        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    @DisplayName("Should fail when completing a task directly from TODO without starting")
    void shouldThrowExceptionWhenCompletingDirectlyFromTodo() {
        Task task = new Task("Task 1", "Desc", null, Priority.MEDIUM, projectId);

        assertThrows(InvalidTaskStateTransitionException.class, task::complete);
    }

    @Test
    @DisplayName("Should fail when starting an already completed task")
    void shouldThrowExceptionWhenStartingCompletedTask() {
        Task task = new Task("Task 1", "Desc", null, Priority.MEDIUM, projectId);
        task.start();
        task.complete();

        assertThrows(InvalidTaskStateTransitionException.class, task::start);
    }

    @Test
    @DisplayName("Should validate empty title and throw TaskValidationException")
    void shouldThrowWhenTitleIsEmpty() {
        assertThrows(TaskValidationException.class, () ->
            new Task("", "Desc", null, Priority.LOW, projectId)
        );
    }
}
