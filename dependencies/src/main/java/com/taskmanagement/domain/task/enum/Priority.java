package com.taskmanagement.domain.task.enums;

public enum Priority {
    LOW("Thap"),
    MEDIUM("Trung binh"),
    HIGH("Cao");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
