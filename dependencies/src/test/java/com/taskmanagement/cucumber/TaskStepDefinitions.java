package com.taskmanagement.cucumber;

import com.taskmanagement.adapter.notification.ConsoleNotificationAdapter;
import com.taskmanagement.adapter.persistence.InMemoryProjectRepository;
import com.taskmanagement.adapter.persistence.IOSaveToMemory;
import com.taskmanagement.adapter.presenter.TaskConsolePresenter;
import com.taskmanagement.application.boundary.NotificationPort;
import com.taskmanagement.application.boundary.ProjectRepository;
import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.application.dto.TaskOutputDTO;
import com.taskmanagement.domain.project.entity.Project;
import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.exception.InvalidTaskStateTransitionException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TaskStepDefinitions {
    private TaskControl taskControl;
    private IOSaveToMemory taskRepository;
    private ProjectRepository projectRepository;
    private TaskOutputDTO currentTaskOutput;
    private Exception thrownException;

    @Given("the task management system is ready")
    public void theTaskManagementSystemIsReady() {
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
        thrownException = null;
        currentTaskOutput = null;
    }

    @When("I create a task with title {string} and description {string} with priority {string}")
    public void iCreateATask(String title, String description, String priorityStr) {
        Priority priority = Priority.valueOf(priorityStr.toUpperCase());
        TaskInputDTO input = new TaskInputDTO(title, description, null, priority, null);
        currentTaskOutput = taskControl.createTask(input);
    }

    @Then("the task should be created successfully")
    public void theTaskShouldBeCreatedSuccessfully() {
        assertNotNull(currentTaskOutput);
        assertNotNull(currentTaskOutput.taskId());
    }

    @Then("the task status should be {string}")
    public void theTaskStatusShouldBe(String expectedStatus) {
        assertNotNull(currentTaskOutput);
        assertEquals(expectedStatus, currentTaskOutput.statusDisplay());
    }

    @Then("the task should belong to the {string} project")
    public void theTaskShouldBelongToTheProject(String projectName) {
        Project inbox = projectRepository.getInbox();
        assertEquals(projectName, inbox.getName());
        UUID taskId = UUID.fromString(currentTaskOutput.taskId());
        assertTrue(inbox.getTask(taskId).isPresent());
    }

    @Given("a task exists with title {string}")
    public void aTaskExistsWithTitle(String title) {
        TaskInputDTO input = new TaskInputDTO(title, "Default description", null, Priority.MEDIUM, null);
        currentTaskOutput = taskControl.createTask(input);
        assertNotNull(currentTaskOutput);
    }

    @When("I start the task")
    public void iStartTheTask() {
        UUID taskId = UUID.fromString(currentTaskOutput.taskId());
        currentTaskOutput = taskControl.startTask(taskId);
    }

    @When("I complete the task")
    public void iCompleteTheTask() {
        UUID taskId = UUID.fromString(currentTaskOutput.taskId());
        currentTaskOutput = taskControl.completeTask(taskId);
    }

    @When("I attempt to complete the task directly")
    public void iAttemptToCompleteTheTaskDirectly() {
        try {
            UUID taskId = UUID.fromString(currentTaskOutput.taskId());
            taskControl.completeTask(taskId);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the operation should fail with an invalid transition error")
    public void theOperationShouldFailWithAnInvalidTransitionError() {
        assertNotNull(thrownException);
        assertTrue(thrownException instanceof InvalidTaskStateTransitionException);
    }
}
