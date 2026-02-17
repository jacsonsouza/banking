package com.bank.banking_core.infrastructure.web.error;

public record ApiError(
    String timestamp,
    int status,
    String error,
    String message,
    String path
) {}