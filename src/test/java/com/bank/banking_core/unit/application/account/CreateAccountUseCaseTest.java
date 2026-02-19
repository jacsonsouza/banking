package com.bank.banking_core.unit.application.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bank.banking_core.application.account.CreateAccountUseCase;
import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountAlreadyExistsException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CreateAccountUseCaseTest {
  private AccountRepository accountRepository;
  private CreateAccountUseCase createAccountUseCase;

  @BeforeEach
  public void setUp() {
    accountRepository = Mockito.mock(AccountRepository.class);
    createAccountUseCase = new CreateAccountUseCase(accountRepository);
  }

  @Test
  public void shouldCreateAccountWhenNumberDoesNotExist() {
    String accountNumber = "A12345";

    when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.empty());

    when(accountRepository.create(any(Account.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Account account = createAccountUseCase.execute(accountNumber);

    assertNotNull(account);
    assertEquals(accountNumber, account.getNumber());

    verify(accountRepository).create(any(Account.class));
  }

  @Test
  public void shouldThrowExceptionWhenAccountAlreadyExists() {
    String accountNumber = "A12345";

    when(accountRepository.findByNumber(accountNumber))
        .thenReturn(Optional.of(Account.create(accountNumber)));

    assertThrows(
            AccountAlreadyExistsException.class, () -> createAccountUseCase.execute(accountNumber))
        .getMessage()
        .equals("Account already exists");
  }
}
