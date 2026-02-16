package com.bank.banking_core.infrastructure.account;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.account.AccountStatus;

public class AccountRepositoryImpl
    implements AccountRepository {

    private final SpringDataAccountRepository jpaRepository;

    public AccountRepositoryImpl(SpringDataAccountRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = new AccountJpaEntity(account);
        AccountJpaEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(this::toDomain);
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        return jpaRepository.findByNumber(number)
            .map(this::toDomain);
    }
    

    private Account toDomain(AccountJpaEntity entity) {
        return Account.restore(
            entity.getId(),
            entity.getNumber(),
            AccountStatus.valueOf(entity.getStatus()),
            entity.getBalance(),
            LocalDateTime.parse(entity.getCreatedAt()),
            LocalDateTime.parse(entity.getUpdatedAt())
        );
    }
}   
