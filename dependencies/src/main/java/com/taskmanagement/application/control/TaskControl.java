package com.taskmanagement.application.control;

import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.boundary.TaskRepository;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.application.dto.TaskOutputDTO;
import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.application.exception.TaskNotFoundException;
import com.taskmanagement.domain.task.valueobject.Deadline;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskControl {
    private final TaskShowing taskShowing;
    private final TaskSaving taskSaving;
    private final TaskRepository taskRepository;
    private final Map<UUID, Task> inMemoryStore = new HashMap<>();

    public TaskControl(TaskShowing taskShowing, TaskSaving taskSaving) {
        this(taskShowing, taskSaving,
            taskSaving instanceof TaskRepository repository ? repository : null);
    }

    public TaskControl(TaskShowing taskShowing, TaskSaving taskSaving, TaskRepository taskRepository) {
        this.taskShowing = taskShowing;
        this.taskSaving = taskSaving;
        this.taskRepository = taskRepository;
    }

    public TaskOutputDTO createTask(TaskInputDTO input) {
        Deadline deadline = input.dueDate() != null ? new Deadline(input.dueDate()) : null;
        UUID projId = input.projectId() != null ? input.projectId() : UUID.randomUUID();

        Task task = new Task(
            input.title(),
            input.description(),
            deadline,
            input.priority(),
            projId
        );

        inMemoryStore.put(task.getId(), task);
        if (taskSaving != null) {
            taskSaving.saveTask(task);
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
        if (taskSaving != null) {
            taskSaving.saveTask(task);
        }
        TaskOutputDTO output = mapToOutputDTO(task);
        if (taskShowing != null) {
            taskShowing.showResult(output);
        }
        return output;
    }

    public TaskOutputDTO completeTask(UUID taskId) {
        Task task = getTaskOrThrow(taskId);
        task.complete();
        if (taskSaving != null) {
            taskSaving.saveTask(task);
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
        if (taskSaving != null) {
            taskSaving.saveTask(task);
        }
        TaskOutputDTO output = mapToOutputDTO(task);
        if (taskShowing != null) {
            taskShowing.showResult(output);
        }
        return output;
    }

    public TaskOutputDTO getTask(UUID taskId) {
        return mapToOutputDTO(getTaskOrThrow(taskId));
    }

    public java.util.List<TaskOutputDTO> getTasks() {
        java.util.Collection<Task> tasks = taskRepository != null
            ? taskRepository.findAll()
            : inMemoryStore.values();
        return tasks.stream().map(this::mapToOutputDTO).toList();
    }

    private Task getTaskOrThrow(UUID taskId) {
        Task task = taskRepository != null
            ? taskRepository.findById(taskId).orElse(null)
            : inMemoryStore.get(taskId);
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
