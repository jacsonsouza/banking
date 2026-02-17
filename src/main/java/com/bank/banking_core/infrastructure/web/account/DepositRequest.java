package com.bank.banking_core.infrastructure.web.account;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public class DepositRequest {
    @Positive
    @DecimalMin(value = "0.01", message = "Deposit amount must be at least 0.01")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
