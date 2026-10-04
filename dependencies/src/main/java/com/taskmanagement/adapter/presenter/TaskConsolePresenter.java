package com.taskmanagement.adapter.presenter;

import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.dto.TaskOutputDTO;

public class TaskConsolePresenter implements TaskShowing {
    @Override
    public void showResult(TaskOutputDTO outputData) {
        System.out.println("========================================");
        System.out.println("[CONSOLE PRESENTATION]");
        System.out.println("ID         : " + outputData.taskId());
        System.out.println("Tieu de    : " + outputData.title());
        System.out.println("Mo ta      : " + outputData.description());
        System.out.println("Trang thai : " + outputData.statusDisplay());
        System.out.println("Uu tien    : " + outputData.priorityDisplay());
        System.out.println("Han chot   : " + outputData.dueDate());
        System.out.println("Qua han    : " + (outputData.isOverdue() ? "CO ⚠️" : "Khong"));
        System.out.println("========================================\n");
    }
}
