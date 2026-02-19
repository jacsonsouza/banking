package com.bank.banking_core.infrastructure.account;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.account.AccountStatus;
import com.bank.banking_core.domain.exception.AccountNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

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
    AccountJpaEntity entity =
        jpaRepository.findById(account.getId()).orElseThrow(() -> new AccountNotFoundException());

    if (!entity.getVersion().equals(account.getVersion())) {
      throw new ObjectOptimisticLockingFailureException(AccountJpaEntity.class, account.getId());
    }

    entity.applyChanges(account);
    return toDomain(jpaRepository.save(entity));
  }

  @Override
  public Optional<Account> findById(UUID id) {
    return jpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Account> findByNumber(String number) {
    return jpaRepository.findByNumber(number).map(this::toDomain);
  }

  private Account toDomain(AccountJpaEntity entity) {
    return Account.restore(
        entity.getId(),
        entity.getNumber(),
        entity.getVersion(),
        AccountStatus.valueOf(entity.getStatus()),
        entity.getBalance(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
