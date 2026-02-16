package com.bank.application.account;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;

public class CreateAccountUseCase {
    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(String accountNumber) {
        accountRepository.findByNumber(accountNumber).ifPresent(a -> {
            throw new IllegalArgumentException("Account already exists!");
        });

        Account account = Account.create(accountNumber);
        return accountRepository.save(account);
    }
}
