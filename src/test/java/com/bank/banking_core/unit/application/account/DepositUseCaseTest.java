package com.bank.banking_core.unit.application.account;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bank.banking_core.application.account.DepositUseCase;
import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountNotFoundException;
import com.bank.banking_core.domain.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DepositUseCaseTest {
  private AccountRepository accountRepository;
  private DepositUseCase depositUseCase;

  private Account account;

  @BeforeEach
  public void setUp() {
    accountRepository = Mockito.mock(AccountRepository.class);
    depositUseCase = new DepositUseCase(accountRepository);

    account = Account.create("A123456");
  }

  @Test
  public void testShouldDepositCorrectlyToAccount() {
    BigDecimal amountToDeposit = BigDecimal.valueOf(1000.50);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    depositUseCase.execute(this.account.getId(), amountToDeposit);

    assert account.getBalance().equals(amountToDeposit);
  }

  @Test
  public void testShouldThrowExceptionWhenAccountNotFound() {
    BigDecimal amountToDeposit = BigDecimal.valueOf(1000.50);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

    assertThrows(
        AccountNotFoundException.class,
        () -> depositUseCase.execute(account.getId(), amountToDeposit));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsNegative() {
    BigDecimal amountToDeposit = BigDecimal.valueOf(-1000.50);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InvalidAmountException.class,
        () -> depositUseCase.execute(account.getId(), amountToDeposit));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsZero() {
    BigDecimal amountToDeposit = BigDecimal.ZERO;

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InvalidAmountException.class,
        () -> depositUseCase.execute(account.getId(), amountToDeposit));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsNull() {
    BigDecimal amountToDeposit = null;

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InvalidAmountException.class,
        () -> depositUseCase.execute(account.getId(), amountToDeposit));
  }

  @Test
  public void testShouldAccumulateDepositToExistingBalance() {
    BigDecimal initialBalance = BigDecimal.valueOf(500.00);
    BigDecimal amountToDeposit = BigDecimal.valueOf(1000.50);

    account.deposit(initialBalance);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    depositUseCase.execute(account.getId(), amountToDeposit);

    assert account.getBalance().equals(initialBalance.add(amountToDeposit));
  }
}
