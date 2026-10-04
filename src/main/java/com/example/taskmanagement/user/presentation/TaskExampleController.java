package com.example.taskmanagement.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Controller mẫu để minh họa cách Người 5 (API) & Người 3 (Use Cases)
 * sử dụng quyền hạn do Người 6 cung cấp để bảo vệ các endpoints của Task.
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskExampleController {

    @GetMapping
    @PreAuthorize("hasAuthority('view_task')")
    public ResponseEntity<?> viewTasks(Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "message", "Lấy danh sách tasks thành công",
            "caller", auth.getName(),
            "authorities", auth.getAuthorities()
        ));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create_task')")
    public ResponseEntity<?> createTask(Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "message", "Admin & Manager có quyền tạo Task!",
            "createdBy", auth.getName()
        ));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('delete_task')")
    public ResponseEntity<?> deleteTask(@PathVariable UUID taskId, Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "message", "Chỉ Admin mới có quyền xoá Task!",
            "deletedBy", auth.getName(),
            "taskId", taskId
        ));
    }

    @PostMapping("/{taskId}/assign")
    @PreAuthorize("hasAuthority('assign_task')")
    public ResponseEntity<?> assignTask(@PathVariable UUID taskId, @RequestParam UUID assigneeId, Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "message", "Admin & Manager có quyền phân công task!",
            "assignedBy", auth.getName(),
            "taskId", taskId,
            "assigneeId", assigneeId
        ));
    }
}
