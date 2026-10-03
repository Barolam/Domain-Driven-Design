package com.taskmanagement.adapter.presenter;

import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.dto.TaskOutputDTO;

public class TaskGUIPresenter implements TaskShowing {
    @Override
    public void showResult(TaskOutputDTO outputData) {
        // Simulates GUI window rendering or dialog box
        String dialogBox = String.format(
            "┌────────────────────────────────────────┐\n" +
            "│           SWING GUI WINDOW             │\n" +
            "├────────────────────────────────────────┤\n" +
            "│ Title: %-31s │\n" +
            "│ Status: %-30s │\n" +
            "│ Priority: %-28s │\n" +
            "└────────────────────────────────────────┘",
            outputData.title(), outputData.statusDisplay(), outputData.priorityDisplay()
        );
        System.out.println(dialogBox);
    }
}
