package com.bank.banking_core.unit.application.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bank.banking_core.application.account.GetAccountByIdUseCase;
import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GetAccountByIdUseCaseTest {
  private AccountRepository accountRepository;
  private GetAccountByIdUseCase getAccountByIdUseCase;

  @BeforeEach
  void setUp() {
    accountRepository = Mockito.mock(AccountRepository.class);
    getAccountByIdUseCase = new GetAccountByIdUseCase(accountRepository);
  }

  @Test
  void shouldReturnAccountWhenAccountExists() {
    Account account = Account.create("123456789");

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    Account findedAccount = getAccountByIdUseCase.execute(account.getId());

    assertNotNull(findedAccount);
    assertEquals(account.getId(), findedAccount.getId());
    assertEquals(account.getNumber(), findedAccount.getNumber());
  }

  @Test
  void shouldThrowExceptionWhenAccountDoesNotExist() {
    UUID accountId = UUID.randomUUID();

    when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

    AccountNotFoundException ex =
        assertThrows(
            AccountNotFoundException.class, () -> getAccountByIdUseCase.execute(accountId));

    assertEquals("Account not found.", ex.getMessage());
  }
}
