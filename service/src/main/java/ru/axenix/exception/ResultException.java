package ru.axenix.exception;

public class ResultException extends RuntimeException {
    public ResultException(String message) {
        super(message);
    }

    public ResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultException(Throwable cause) {
        super(cause);
    }

    public ResultException() {
    }
}
