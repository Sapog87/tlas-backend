package ru.axenix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.axenix.dto.ErrorResponse;
import ru.axenix.exception.AuthException;
import ru.axenix.exception.UserAlreadyExists;
import ru.axenix.exception.UserNotFoundException;
import ru.axenix.exception.YandexCodeNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(UserAlreadyExists e) {
        log.warn(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(UserNotFoundException e) {
        log.warn(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(AuthException e) {
        log.warn(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @ExceptionHandler(YandexCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(YandexCodeNotFoundException e) {
        log.warn(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
