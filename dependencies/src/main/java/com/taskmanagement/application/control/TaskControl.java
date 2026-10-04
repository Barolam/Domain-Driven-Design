package com.taskmanagement.application.control;

import com.taskmanagement.application.boundary.NotificationPort;
import com.taskmanagement.application.boundary.ProjectRepository;
import com.taskmanagement.application.boundary.TaskRepository;
import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.application.dto.TaskOutputDTO;
import com.taskmanagement.application.exception.TaskNotFoundException;
import com.taskmanagement.domain.project.entity.Project;
import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.valueobject.Deadline;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskControl {
    private final TaskShowing taskShowing;
    private final TaskSaving taskSaving;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final NotificationPort notificationPort;
    private final Map<UUID, Task> inMemoryFallbackStore = new HashMap<>();

    public TaskControl(TaskShowing taskShowing, TaskSaving taskSaving) {
        this(taskShowing, taskSaving,
            taskSaving instanceof TaskRepository repository ? repository : null, null, null);
    }

    public TaskControl(TaskShowing taskShowing, TaskSaving taskSaving, TaskRepository taskRepository) {
        this(taskShowing, taskSaving, taskRepository, null, null);
    }

    public TaskControl(
        TaskShowing taskShowing,
        TaskSaving taskSaving,
        TaskRepository taskRepository,
        ProjectRepository projectRepository,
        NotificationPort notificationPort
    ) {
        this.taskShowing = taskShowing;
        this.taskSaving = taskSaving;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.notificationPort = notificationPort;
    }

    public TaskOutputDTO createTask(TaskInputDTO input) {
        Deadline deadline = input.dueDate() != null ? new Deadline(input.dueDate()) : null;

        // Domain rule: If no projectId provided, default to INBOX project
        UUID projId = input.projectId();
        if (projId == null && projectRepository != null) {
            Project inbox = projectRepository.getInbox();
            if (inbox != null) {
                projId = inbox.getId();
            }
        }
        if (projId == null) {
            projId = UUID.randomUUID();
        }

        Task task = new Task(
            input.title(),
            input.description(),
            deadline,
            input.priority(),
            projId
        );

        // Save into repository first (source of truth)
        if (taskRepository != null) {
            taskRepository.save(task);
        } else if (taskSaving != null) {
            taskSaving.saveTask(task);
        } else {
            inMemoryFallbackStore.put(task.getId(), task);
        }

        // Attach to Project aggregate root
        if (projectRepository != null) {
            projectRepository.findById(projId).ifPresent(p -> {
                p.addTask(task);
                projectRepository.save(p);
            });
        }

        // Trigger notifications via NotificationPort
        if (notificationPort != null) {
            notificationPort.notifyTaskCreated(task);
            if (task.getPriority() == Priority.HIGH) {
                notificationPort.notifyHighPriorityTask(task);
            }
        }

        TaskOutputDTO output = mapToOutputDTO(task);
        if (taskShowing != null) {
            taskShowing.showResult(output);
        }
        return output;
    }

    public TaskOutputDTO startTask(UUID taskId) {
        Task task = getTaskOrThrow(taskId);
        task.start();
        saveTaskState(task);

        TaskOutputDTO output = mapToOutputDTO(task);
        if (taskShowing != null) {
            taskShowing.showResult(output);
        }
        return output;
    }

    public TaskOutputDTO completeTask(UUID taskId) {
        Task task = getTaskOrThrow(taskId);
        task.complete();
        saveTaskState(task);

        if (notificationPort != null) {
            notificationPort.notifyTaskCompleted(task);
        }

        TaskOutputDTO output = mapToOutputDTO(task);
        if (taskShowing != null) {
            taskShowing.showResult(output);
        }
        return output;
    }

    public TaskOutputDTO cancelTask(UUID taskId) {
        Task task = getTaskOrThrow(taskId);
        task.cancel();
        saveTaskState(task);

        TaskOutputDTO output = mapToOutputDTO(task);
        if (taskShowing != null) {
            taskShowing.showResult(output);
        }
        return output;
    }

    public TaskOutputDTO getTask(UUID taskId) {
        return mapToOutputDTO(getTaskOrThrow(taskId));
    }

    public List<TaskOutputDTO> getTasks() {
        Collection<Task> tasks = taskRepository != null
            ? taskRepository.findAll()
            : inMemoryFallbackStore.values();
        return tasks.stream().map(this::mapToOutputDTO).toList();
    }

    private void saveTaskState(Task task) {
        if (taskRepository != null) {
            taskRepository.save(task);
        } else if (taskSaving != null) {
            taskSaving.saveTask(task);
        } else {
            inMemoryFallbackStore.put(task.getId(), task);
        }
    }

    private Task getTaskOrThrow(UUID taskId) {
        Task task = taskRepository != null
            ? taskRepository.findById(taskId).orElse(null)
            : inMemoryFallbackStore.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task not found with ID: " + taskId);
        }
        return task;
    }

    private TaskOutputDTO mapToOutputDTO(Task task) {
        return new TaskOutputDTO(
            task.getId().toString(),
            task.getTitle().value(),
            task.getDescription(),
            task.getStatus().getDescription(),
            task.getPriority().getLabel(),
            task.getDueDate() != null ? task.getDueDate().dueDate() : null,
            task.isOverdue()
        );
    }
}
