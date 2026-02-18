package com.bank.banking_core.infrastructure.config;

import com.bank.banking_core.application.account.CreateAccountUseCase;
import com.bank.banking_core.application.account.DepositUseCase;
import com.bank.banking_core.application.account.GetAccountByIdUseCase;
import com.bank.banking_core.application.account.WithdrawUseCase;
import com.bank.banking_core.domain.account.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {
  @Bean
  public CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository) {
    return new CreateAccountUseCase(accountRepository);
  }

  @Bean
  public GetAccountByIdUseCase getAccountByIdUseCase(AccountRepository accountRepository) {
    return new GetAccountByIdUseCase(accountRepository);
  }

  @Bean
  public DepositUseCase depositUseCase(AccountRepository accountRepository) {
    return new DepositUseCase(accountRepository);
  }

  @Bean
  public WithdrawUseCase withdrawUseCase(AccountRepository accountRepository) {
    return new WithdrawUseCase(accountRepository);
  }
}
