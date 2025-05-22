package ru.axenix.exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExists(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExists() {
    }
}
