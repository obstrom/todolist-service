package com.obstrom.todolistservice.error.exception;

public class UniqueFieldConstraintException extends RuntimeException {

    public UniqueFieldConstraintException(String message) {
        super(message);
    }

    public UniqueFieldConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

}
