package com.taskmanagement.adapter.presenter;

import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.dto.TaskOutputDTO;

public class TaskWebPresenter implements TaskShowing {
    private String jsonOutput;

    @Override
    public void showResult(TaskOutputDTO outputData) {
        this.jsonOutput = String.format(
            "{\n" +
            "  \"taskId\": \"%s\",\n" +
            "  \"title\": \"%s\",\n" +
            "  \"status\": \"%s\",\n" +
            "  \"priority\": \"%s\",\n" +
            "  \"isOverdue\": %b\n" +
            "}",
            outputData.taskId(), outputData.title(), outputData.statusDisplay(),
            outputData.priorityDisplay(), outputData.isOverdue()
        );
        System.out.println("[WEB JSON RESPONSE SENT TO BROWSER]:\n" + this.jsonOutput);
    }

    public String getJsonOutput() {
        return jsonOutput;
    }
}
