package com.bank.banking_core.application.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountAlreadyExistsException;

class CreateAccountUseCaseTest {
    private AccountRepository accountRepository;
    private CreateAccountUseCase createAccountUseCase;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        createAccountUseCase = new CreateAccountUseCase(accountRepository);
    }
    
    @Test
    void shouldCreateAccountWhenNumberDoesNotExist() {
        String accountNumber = "A12345";

        when(accountRepository.findByNumber(accountNumber))
            .thenReturn(Optional.empty());

        when(accountRepository.save(any(Account.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Account account = createAccountUseCase.execute(accountNumber);

        assertNotNull(account);
        assertEquals(accountNumber, account.getNumber());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountAlreadyExists() {
        String accountNumber = "A12345";

        when(accountRepository.findByNumber(accountNumber))
            .thenReturn(Optional.of(Account.create(accountNumber)));

        assertThrows(
            AccountAlreadyExistsException.class,
            () -> createAccountUseCase.execute(accountNumber)
        ).getMessage().equals("Account already exists");
    }
}
