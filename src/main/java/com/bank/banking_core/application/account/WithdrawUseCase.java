package com.bank.banking_core.application.account;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.exception.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.UUID;

public class WithdrawUseCase {
  private final AccountRepository accountRepository;

  public WithdrawUseCase(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account execute(UUID accountId, BigDecimal amount) {
    Account account =
        accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException());

    account.withdraw(amount);

    return accountRepository.update(account);
  }
}
