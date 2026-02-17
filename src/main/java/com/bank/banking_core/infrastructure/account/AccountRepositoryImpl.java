package com.bank.banking_core.infrastructure.account;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.account.AccountStatus;
import com.bank.banking_core.domain.exception.AccountNotFoundException;

@Repository
public class AccountRepositoryImpl
    implements AccountRepository {

    private final SpringDataAccountRepository jpaRepository;

    public AccountRepositoryImpl(SpringDataAccountRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account create(Account account) {
        AccountJpaEntity entity = new AccountJpaEntity(account);
        AccountJpaEntity savedEntity = jpaRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Account update(Account account) {
        AccountJpaEntity entity = jpaRepository.findById(account.getId())
            .orElseThrow(() -> new AccountNotFoundException());

        entity.applyChanges(account);
        AccountJpaEntity updatedEntity = jpaRepository.save(entity);

        return toDomain(updatedEntity);
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
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}   
