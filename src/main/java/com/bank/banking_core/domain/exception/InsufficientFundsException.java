package com.bank.banking_core.domain.exception;

public class InsufficientFundsException extends DomainException {
    private static final String ERROR_CODE = "INSUFFICIENT_FUNDS";

    public InsufficientFundsException() {
        super(
            ERROR_CODE,
            "Insufficient funds"
        );
    }
}
