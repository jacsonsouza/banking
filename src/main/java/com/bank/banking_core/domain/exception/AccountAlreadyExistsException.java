package com.bank.banking_core.domain.exception;

public class AccountAlreadyExistsException extends DomainException {
    private static final String ERROR_CODE = "ACCOUNT_ALREADY_EXISTS";

    public AccountAlreadyExistsException() {
        super(
            ERROR_CODE,
            "Account already exists."
        );
    }
}
