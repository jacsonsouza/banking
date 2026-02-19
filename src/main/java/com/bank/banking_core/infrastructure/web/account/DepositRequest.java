package com.bank.banking_core.infrastructure.web.account;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DepositRequest(
    @NotNull(message = "{deposit.amount.not_null}")
        @DecimalMin(value = "0.01", message = "{deposit.amount.positive}")
        BigDecimal amount) {}
