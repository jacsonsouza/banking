package com.bank.banking_core.domain.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bank.banking_core.domain.exception.InsufficientFundsException;
import com.bank.banking_core.domain.exception.InvalidAmountException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AccountTest {
  @Test
  void shouldCreateAccountWithZeroBalanceAndActiveStatus() {
    Account account = Account.create("A123");

    assertNotNull(account);
    assertEquals(BigDecimal.ZERO, account.getBalance());
    assertEquals(AccountStatus.ACTIVE, account.getStatus());
  }

  @Test
  void shouldDepositMoneyIntoAccount() {
    Account account = Account.create("A123");
    account.deposit(BigDecimal.valueOf(100));

    assertEquals(BigDecimal.valueOf(100), account.getBalance());
  }

  @Test
  void shouldWithdrawMoneyFromAccount() {
    Account account = Account.create("A123");
    account.deposit(BigDecimal.valueOf(100));
    account.withdraw(BigDecimal.valueOf(40));

    assertEquals(BigDecimal.valueOf(60), account.getBalance());
  }

  @Test
  void shouldNotAllowWithdrawWithInsufficientFunds() {
    Account account = Account.create("A123");

    assertThrows(InsufficientFundsException.class, () -> account.withdraw(BigDecimal.valueOf(100)));
  }

  @Test
  void shouldNotAllowNegativeDeposit() {
    Account account = Account.create("A123");
    assertThrows(InvalidAmountException.class, () -> account.deposit(BigDecimal.valueOf(-50)));
  }
}
