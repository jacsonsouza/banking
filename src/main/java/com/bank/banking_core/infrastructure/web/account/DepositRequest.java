package com.bank.banking_core.infrastructure.web.account;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DepositRequest {
  @Positive private BigDecimal amount;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
