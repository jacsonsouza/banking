package com.bank.banking_core.domain.account;

public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    CLOSED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
