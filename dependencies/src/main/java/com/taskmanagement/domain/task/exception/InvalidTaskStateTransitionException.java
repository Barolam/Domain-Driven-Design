package com.taskmanagement.domain.task.exception;

public class InvalidTaskStateTransitionException extends DomainException {
    public InvalidTaskStateTransitionException(String message) {
        super(message);
    }
}
