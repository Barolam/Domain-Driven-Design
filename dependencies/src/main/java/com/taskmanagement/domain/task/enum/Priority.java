package com.taskmanagement.domain.task.enums;

public enum Priority {
    LOW("Thấp"),
    MEDIUM("Trung bình"),
    HIGH("Cao");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
