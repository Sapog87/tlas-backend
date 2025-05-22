package ru.axenix.exception;

public class SearchHistoryNotFoundException extends RuntimeException {
    public SearchHistoryNotFoundException() {
    }

    public SearchHistoryNotFoundException(String message) {
        super(message);
    }

    public SearchHistoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchHistoryNotFoundException(Throwable cause) {
        super(cause);
    }
}
