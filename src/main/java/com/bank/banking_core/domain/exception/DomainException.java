package com.bank.banking_core.domain.exception;

import java.util.Map;

public abstract class DomainException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> details;

    protected DomainException(String errorCode, String message) {
        this(errorCode, message, Map.of());
    }

    protected DomainException(
        String code,
        String message,
        Map<String, Object> details
    ) {
        super(message);
        this.errorCode = code;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
