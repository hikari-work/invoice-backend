package org.yann.integerasiorderkuota.orderkuota.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.Map;

@RestControllerAdvice
public class ParserAdvice {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> handleParseException(DateTimeParseException ex) {
        Map<String, String> errorResponse = Map.of(
                "timestamp", java.time.LocalDateTime.now().toString(),
                "status", "400",
                "error", "Bad Request",
                "message", "Invalid date format: " + ex.getParsedString(),
                "path", "/api/v2/statements/report"
        );
        return ResponseEntity.badRequest().body(errorResponse);

    }
}
