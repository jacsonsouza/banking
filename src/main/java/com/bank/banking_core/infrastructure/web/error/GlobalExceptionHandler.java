package com.bank.banking_core.infrastructure.web.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            String.valueOf(System.currentTimeMillis()),
            400,
            "Bad Request",
            ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
        IllegalArgumentException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            String.valueOf(System.currentTimeMillis()),
            400,
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
        Exception ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            String.valueOf(System.currentTimeMillis()),
            500,
            "Internal Server Error",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(500).body(apiError);
    }
}
