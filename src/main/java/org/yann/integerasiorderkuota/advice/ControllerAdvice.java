package org.yann.integerasiorderkuota.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yann.integerasiorderkuota.exception.*;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Endpoint tidak ditemukan di path: " + ex.getRequestURL(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String supportedMethods = null;
        if (ex.getSupportedMethods() != null) {
            supportedMethods = String.join(", ", ex.getSupportedMethods());
        }
        String message = "Metode " + ex.getMethod() + " tidak didukung. Coba gunakan: " + supportedMethods;

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler({UserNotFoundException.class, BankNotFound.class, InquiryNotFound.class, InquiryNotFound.class})
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(Exception ex, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestParamNotFullFilled.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RequestParamNotFullFilled ex, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserBalanceUnderMinimum.class)
    public ResponseEntity<Map<String, Object>> handleUserBalanceUnderMinimum(
            UserBalanceUnderMinimum ex,
            HttpServletRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String errorMessage = "Parameter wajib '" + parameterName + "' tidak ada dalam permintaan.";


        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", errorMessage);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    public static class ApiErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }
}
