package org.yann.integerasiorderkuota.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yann.integerasiorderkuota.dto.RegisterDTO;
import org.yann.integerasiorderkuota.exception.DuplicateUsername;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RegisterAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        RegisterDTO<Map<String, String>> response = RegisterDTO.<Map<String, String>>builder()
                .status("Error")
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(DuplicateUsername.class)
    public ResponseEntity<RegisterDTO<?>> handleDuplicateUsername(DuplicateUsername duplicateUsername) {
        Map<String, String> errors = new HashMap<>();

        errors.put("username", duplicateUsername.getMessage());

        RegisterDTO<Map<String, String>> response = RegisterDTO.<Map<String, String>>builder()
                .status("Error")
                .data(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
