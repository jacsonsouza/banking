package com.bank.banking_core.infrastructure.web.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record WithdrawRequest(
    @NotNull(message = "{withdraw.amount.not_null}")
        @DecimalMin(value = "0.01", message = "{withdraw.amount.positive}")
        BigDecimal amount) {}
