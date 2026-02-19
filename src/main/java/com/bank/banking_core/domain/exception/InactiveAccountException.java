package com.bank.banking_core.domain.exception;

public class InactiveAccountException extends DomainException {
    private static final String ERROR_CODE = "INACTIVE_ACCOUNT";

    public InactiveAccountException() {
        super(
            ERROR_CODE,
            "Account is inactive"
        );
    }
}
