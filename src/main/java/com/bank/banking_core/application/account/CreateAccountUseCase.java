package com.bank.banking_core.application.account;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountAlreadyExistsException;

public class CreateAccountUseCase {
    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(String accountNumber) {
        validateAccount(accountNumber);

        Account account = Account.create(accountNumber);
        return accountRepository.save(account);
    }

    private void validateAccount(String accountNumber) {
        accountRepository.findByNumber(accountNumber).ifPresent(a -> {
            throw new AccountAlreadyExistsException();
        });
    }
}
