package com.bank.banking_core.integration.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.domain.account.AccountStatus;
import com.bank.banking_core.infrastructure.account.AccountRepositoryImpl;
import com.bank.banking_core.integration.AbstractRepositoryTest;
import java.math.BigDecimal;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Import(AccountRepositoryImpl.class)
public class AccountRepositoryTest extends AbstractRepositoryTest {
  @Autowired private AccountRepository accountRepository;

  @Autowired private TestEntityManager entityManager;

  private Account account;

  @BeforeEach
  public void setUp() {
    account =
        Instancio.of(Account.class)
            .set(field(Account::getStatus), AccountStatus.ACTIVE)
            .set(field(Account::getBalance), BigDecimal.valueOf(1000.00))
            .create();

    accountRepository.create(account);
  }

  @Test
  public void testShouldPersistAndFindAccountByNumber() {
    Optional<Account> foundAccount = accountRepository.findByNumber(account.getNumber());

    assertThat(foundAccount).isPresent();
    assertThat(foundAccount.get().getId()).isEqualTo(account.getId());
    assertThat(foundAccount.get().getNumber()).isEqualTo(account.getNumber());
  }

  @Test
  public void testShouldReturnEmptyWhenAccountNotFound() {
    Optional<Account> foundAccount = accountRepository.findByNumber("123456789");

    assertThat(foundAccount).isEmpty();
  }

  @Test
  public void testShouldThrowExceptionWhenConcurrentUpdateOccursInWithdraw() {
    Account accountA = accountRepository.findByNumber(account.getNumber()).get();
    Account accountB = accountRepository.findByNumber(account.getNumber()).get();

    accountA.withdraw(BigDecimal.valueOf(10));
    accountRepository.update(accountA);

    entityManager.flush();

    accountB.withdraw(BigDecimal.valueOf(20));

    assertThrows(
        ObjectOptimisticLockingFailureException.class, () -> accountRepository.update(accountB));
  }

  @Test
  public void testShouldThrowExceptionWhenConcurrentUpdateOccursInDeposit() {
    Account accountA = accountRepository.findByNumber(account.getNumber()).get();
    Account accountB = accountRepository.findByNumber(account.getNumber()).get();

    accountA.deposit(BigDecimal.valueOf(10));
    accountRepository.update(accountA);

    entityManager.flush();

    accountB.deposit(BigDecimal.valueOf(20));

    assertThrows(
        ObjectOptimisticLockingFailureException.class, () -> accountRepository.update(accountB));
  }
}
