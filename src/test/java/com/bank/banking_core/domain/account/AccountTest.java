package com.bank.banking_core.domain.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertThrows(
            IllegalArgumentException.class,
            () -> account.withdraw(BigDecimal.valueOf(100))
        ).getMessage().equals("Insufficient funds");
    }

    @Test
    void shouldNotAllowNegativeDeposit() {
        Account account = Account.create("A123");
        assertThrows(
            IllegalArgumentException.class,
            () -> account.deposit(BigDecimal.valueOf(-50))
        ).getMessage().equals("Amount must be non-negative");
    }
}
