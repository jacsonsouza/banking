package com.bank.banking_core.domain.account;

import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    
    Optional<Account> findById(String id);
    Optional<Account> findByNumber(String number);
}
