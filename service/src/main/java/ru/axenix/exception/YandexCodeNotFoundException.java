package ru.axenix.exception;

public class YandexCodeNotFoundException extends RuntimeException {
    public YandexCodeNotFoundException(String message) {
        super(message);
    }

    public YandexCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public YandexCodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public YandexCodeNotFoundException() {
    }
}
