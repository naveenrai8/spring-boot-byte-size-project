package com.nr.authservice.exception;

public class InvalidUserPasswordException extends Exception {
    public InvalidUserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserPasswordException(String message) {
        super(message);
    }
}
