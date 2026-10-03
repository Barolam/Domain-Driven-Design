package com.taskmanagement.domain.task.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
