package com.example.minions.exception;

import com.example.minions.model.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MinionsException.class)
    public ResponseEntity<CustomError> handleException(MinionsException exception) {
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<CustomError> handleAccessDeniedException() {
        CustomError error = CustomError.builder()
                .code("403")
                .message("Blocked ehe")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);

    }
}