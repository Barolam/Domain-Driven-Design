package com.taskmanagement.domain.service;

import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.valueobject.Deadline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskPriorityCalculatorTest {
    private TaskPriorityCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TaskPriorityCalculator();
    }

    @Test
    @DisplayName("Should return HIGH priority for overdue tasks")
    void shouldReturnHighForOverdueTask() {
        Deadline overdueDeadline = new Deadline(Instant.now().minus(2, ChronoUnit.DAYS));
        Task task = new Task("Overdue Task", "Desc", overdueDeadline, Priority.LOW, UUID.randomUUID());

        Priority calculated = calculator.calculatePriority(task);
        assertEquals(Priority.HIGH, calculated);
    }

    @Test
    @DisplayName("Should return MEDIUM priority for tasks due within 2 days")
    void shouldReturnMediumForUrgentTask() {
        Deadline urgentDeadline = new Deadline(Instant.now().plus(1, ChronoUnit.DAYS));
        Task task = new Task("Urgent Task", "Desc", urgentDeadline, Priority.LOW, UUID.randomUUID());

        Priority calculated = calculator.calculatePriority(task);
        assertEquals(Priority.MEDIUM, calculated);
    }

    @Test
    @DisplayName("Should return LOW priority for tasks with far deadline")
    void shouldReturnLowForFarDeadlineTask() {
        Deadline farDeadline = new Deadline(Instant.now().plus(10, ChronoUnit.DAYS));
        Task task = new Task("Far Task", "Desc", farDeadline, Priority.MEDIUM, UUID.randomUUID());

        Priority calculated = calculator.calculatePriority(task);
        assertEquals(Priority.LOW, calculated);
    }
}
