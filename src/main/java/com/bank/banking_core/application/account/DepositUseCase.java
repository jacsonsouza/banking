package com.bank.banking_core.application.account;

import java.math.BigDecimal;
import java.util.UUID;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountNotFoundException;

public class DepositUseCase {
    private final AccountRepository accountRepository;

    public DepositUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(UUID accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException());

        account.deposit(amount);
        
        return accountRepository.update(account);
    }
}
