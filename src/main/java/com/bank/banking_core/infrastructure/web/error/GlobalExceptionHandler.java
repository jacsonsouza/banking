package com.bank.banking_core.infrastructure.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.banking_core.domain.exception.DomainException;

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
            ex.getBindingResult().getAllErrors().toString(),
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

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(
        DomainException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = toHttpStatus(ex.getErrorCode());

        ApiError apiError = new ApiError(
            String.valueOf(System.currentTimeMillis()),
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLockingFailure(
        ObjectOptimisticLockingFailureException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            String.valueOf(System.currentTimeMillis()),
            409,
            "Conflict",
            "The account has been modified by another process.",
            request.getRequestURI()
        );

        return ResponseEntity.status(409).body(apiError);
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
            "An unexpected error occurred.",
            request.getRequestURI()
        );

        return ResponseEntity.status(500).body(apiError);
    }

    private HttpStatus toHttpStatus(String errorCode) {
        try {
            return ErrorCode.valueOf(errorCode).getStatus();
        } catch (IllegalArgumentException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
