package com.bank.banking_core.domain.exception;

public class InvalidAmountException extends DomainException {
    private static final String ERROR_CODE = "INVALID_AMOUNT";

    public InvalidAmountException() {
        super(
            ERROR_CODE,
            "Invalid amount"
        );
    }
}
