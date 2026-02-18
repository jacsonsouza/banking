package com.bank.banking_core.integration.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.account.AccountRepository;
import com.bank.banking_core.infrastructure.account.AccountRepositoryImpl;
import com.bank.banking_core.integration.AbstractRepositoryTest;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(AccountRepositoryImpl.class)
public class AccountRepositoryTest extends AbstractRepositoryTest {
  @Autowired private AccountRepository accountRepository;

  @Test
  public void testShouldPersistAndFindAccountByNumber() {
    Account account = Instancio.create(Account.class);
    accountRepository.create(account);

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
}
