package org.yann.integerasiorderkuota.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yann.integerasiorderkuota.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ignoredEx) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ignoredEx) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundUser(UserNotFoundException userNotFoundException) {
        Map<String, String> data = new HashMap<>();
        data.put("error", userNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(data);
    }
}
