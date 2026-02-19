package com.bank.banking_core.domain.account;

import com.bank.banking_core.domain.exception.InactiveAccountException;
import com.bank.banking_core.domain.exception.InsufficientFundsException;
import com.bank.banking_core.domain.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Account {
  private UUID id;
  private String number;
  private Long version;
  private AccountStatus status;
  private BigDecimal balance;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Account(UUID id, String number) {
    this.id = id;
    this.number = number;
    this.status = AccountStatus.ACTIVE;
    this.balance = BigDecimal.ZERO;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public static Account create(String number) {
    if (number == null || number.isBlank()) {
      throw new IllegalArgumentException("Account number cannot be null or empty");
    }

    return new Account(UUID.randomUUID(), number);
  }

  public static Account restore(
      UUID id,
      String number,
      Long version,
      AccountStatus status,
      BigDecimal balance,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    Account account = new Account(id, number);
    account.version = version;
    account.status = status;
    account.balance = balance;
    account.createdAt = createdAt;
    account.updatedAt = updatedAt;
    return account;
  }

  public void deposit(BigDecimal amount) {
    validateCanOperate();
    validatePositiveAmount(amount);

    this.balance = this.balance.add(amount);
    touch();
  }

  public void withdraw(BigDecimal amount) {
    validateCanOperate();
    validatePositiveAmount(amount);
    validateSufficientFunds(amount);

    this.balance = this.balance.subtract(amount);
    touch();
  }

  private void validateCanOperate() {
    if (!this.status.canOperate()) throw new InactiveAccountException();
  }

  private void validatePositiveAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException();
    }
  }

  private void validateSufficientFunds(BigDecimal amount) {
    if (this.balance.compareTo(amount) < 0) {
      throw new InsufficientFundsException();
    }
  }

  private void touch() {
    this.updatedAt = LocalDateTime.now();
  }
}
