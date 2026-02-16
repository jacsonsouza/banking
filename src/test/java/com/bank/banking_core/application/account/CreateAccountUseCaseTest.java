package com.bank.banking_core.application.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;

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
        when(accountRepository.findById(UUID.fromString("A123")))
            .thenReturn(Optional.empty());

        when(accountRepository.save(any(Account.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Account account = createAccountUseCase.execute("A123");

        assertNotNull(account);
        assertEquals("A123", account.getNumber());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountAlreadyExists() {
        when(accountRepository.findByNumber("A123"))
            .thenReturn(Optional.of(Account.create("A123")));

        assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute("A123")
        ).getMessage().equals("Account number already exists");
    }
}
