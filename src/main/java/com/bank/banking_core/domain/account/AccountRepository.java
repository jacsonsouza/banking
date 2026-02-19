package com.bank.banking_core.domain.account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account create(Account account);
    Account update(Account account);
    
    Optional<Account> findById(UUID id);
    Optional<Account> findByNumber(String number);
}
