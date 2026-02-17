package com.bank.banking_core.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bank.banking_core.application.account.CreateAccountUseCase;
import com.bank.banking_core.application.account.GetAccountByIdUseCase;
import com.bank.banking_core.domain.account.AccountRepository;

@Configuration
public class AccountConfig {
    @Bean
    public CreateAccountUseCase createAccountUseCase(
        AccountRepository accountRepository
    ) {
        return new CreateAccountUseCase(accountRepository);
    }

    @Bean
    public GetAccountByIdUseCase getAccountByIdUseCase(
        AccountRepository accountRepository
    ) {
        return new GetAccountByIdUseCase(accountRepository);
    }
}
