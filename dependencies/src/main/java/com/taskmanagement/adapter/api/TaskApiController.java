package com.taskmanagement.adapter.api;

import com.taskmanagement.adapter.api.dto.ApiResponse;
import com.taskmanagement.adapter.api.dto.TaskCreateRequest;
import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.application.dto.TaskOutputDTO;
import com.taskmanagement.domain.task.enums.Priority;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskApiController {
    private final TaskControl taskControl;

    public TaskApiController(TaskControl taskControl) {
        this.taskControl = taskControl;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskOutputDTO>> createTask(
        @Valid @RequestBody TaskCreateRequest request
    ) {
        Priority priority = request.priority() == null ? Priority.MEDIUM : request.priority();
        TaskInputDTO input = new TaskInputDTO(
            request.title(),
            request.description(),
            request.dueDate(),
            priority,
            request.projectId()
        );
        TaskOutputDTO output = taskControl.createTask(input);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Task created successfully", output));
    }

    @GetMapping
    public ApiResponse<List<TaskOutputDTO>> getTasks() {
        return ApiResponse.success("Tasks retrieved successfully", taskControl.getTasks());
    }

    @GetMapping("/{taskId}")
    public ApiResponse<TaskOutputDTO> getTask(@PathVariable UUID taskId) {
        return ApiResponse.success("Task retrieved successfully", taskControl.getTask(taskId));
    }

    @PostMapping("/{taskId}/start")
    public ApiResponse<TaskOutputDTO> startTask(@PathVariable UUID taskId) {
        return ApiResponse.success("Task started successfully", taskControl.startTask(taskId));
    }

    @PostMapping("/{taskId}/complete")
    public ApiResponse<TaskOutputDTO> completeTask(@PathVariable UUID taskId) {
        return ApiResponse.success("Task completed successfully", taskControl.completeTask(taskId));
    }

    @PostMapping("/{taskId}/cancel")
    public ApiResponse<TaskOutputDTO> cancelTask(@PathVariable UUID taskId) {
        return ApiResponse.success("Task cancelled successfully", taskControl.cancelTask(taskId));
    }
}
