package com.bank.banking_core.application.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bank.banking_core.domain.account.*;
import com.bank.banking_core.domain.exception.*;
import java.math.BigDecimal;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class WithdrawUseCaseTest {
  private AccountRepository accountRepository;
  private WithdrawUseCase withdrawUseCase;

  private Account account;

  @BeforeEach
  public void setUp() {
    accountRepository = Mockito.mock(AccountRepository.class);
    withdrawUseCase = new WithdrawUseCase(accountRepository);

    account =
        Instancio.of(Account.class)
            .set(field(Account::getBalance), BigDecimal.valueOf(1000.00))
            .set(field(Account::getStatus), AccountStatus.ACTIVE)
            .create();
  }

  @Test
  public void testShouldWithdrawCorrectlyToAccount() {
    BigDecimal withdrawAmount = BigDecimal.valueOf(800.00);
    BigDecimal expectedBalance = BigDecimal.valueOf(200.00);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    withdrawUseCase.execute(account.getId(), withdrawAmount);

    assertThat(account.getBalance()).isEqualByComparingTo(expectedBalance);

    verify(accountRepository, times(1)).update(account);
  }

  @Test
  public void testShouldBeAbleToWithdrawAllBalance() {
    BigDecimal withdrawAmount = account.getBalance();
    BigDecimal expectedBalance = BigDecimal.ZERO;

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    withdrawUseCase.execute(account.getId(), withdrawAmount);

    assertThat(account.getBalance()).isEqualByComparingTo(expectedBalance);

    verify(accountRepository, times(1)).update(account);
  }

  @Test
  public void testShouldHandleConcurrencyFailure() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    when(accountRepository.update(account)).thenThrow(new RuntimeException());

    assertThrows(
        RuntimeException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.valueOf(800.00)));
  }

  @Test
  public void testShouldThrowExceptionWhenAccountNotFound() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

    assertThrows(
        AccountNotFoundException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.valueOf(800.00)));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsNegative() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InvalidAmountException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.valueOf(-800.00)));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsZero() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InvalidAmountException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.ZERO));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsNull() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InvalidAmountException.class, () -> withdrawUseCase.execute(account.getId(), null));
  }

  @Test
  public void testShouldThrowExceptionWhenAccountIsInactive() {
    Account inactiveAccount =
        Instancio.of(Account.class).set(field(Account::getStatus), AccountStatus.INACTIVE).create();

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(inactiveAccount));

    assertThrows(
        InactiveAccountException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.valueOf(800.00)));
  }

  @Test
  public void testShouldThrowExceptionWhenAmountIsGreaterThanBalance() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(
        InsufficientFundsException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.valueOf(2000.00)));
  }

  @Test
  public void testShouldThrowExceptionWhenAccountIsClosed() {
    Account closedAccount =
        Instancio.of(Account.class).set(field(Account::getStatus), AccountStatus.CLOSED).create();

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(closedAccount));

    assertThrows(
        InactiveAccountException.class,
        () -> withdrawUseCase.execute(account.getId(), BigDecimal.valueOf(800.00)));
  }
}
