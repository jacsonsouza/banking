package com.bank.banking_core.domain.exception;

public class AccountNotFoundException extends DomainException {
    private static final String ERROR_CODE = "ACCOUNT_NOT_FOUND";

    public AccountNotFoundException() {
        super(
            ERROR_CODE,
            "Account not found."
        );
    }
}
