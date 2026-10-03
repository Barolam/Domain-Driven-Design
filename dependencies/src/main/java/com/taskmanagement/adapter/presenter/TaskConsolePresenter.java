package com.taskmanagement.adapter.presenter;

import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.dto.TaskOutputDTO;

public class TaskConsolePresenter implements TaskShowing {
    @Override
    public void showResult(TaskOutputDTO outputData) {
        System.out.println("========================================");
        System.out.println("[CONSOLE PRESENTATION]");
        System.out.println("ID         : " + outputData.taskId());
        System.out.println("Tiêu đề    : " + outputData.title());
        System.out.println("Mô tả      : " + outputData.description());
        System.out.println("Trạng thái : " + outputData.statusDisplay());
        System.out.println("Ưu tiên    : " + outputData.priorityDisplay());
        System.out.println("Hạn chót   : " + outputData.dueDate());
        System.out.println("Quá hạn    : " + (outputData.isOverdue() ? "CÓ ⚠️" : "Không"));
        System.out.println("========================================\n");
    }
}
