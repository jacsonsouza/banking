package com.bank.banking_core.infrastructure.account;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAccountRepository 
    extends JpaRepository<AccountJpaEntity, UUID> {
    
    Optional<AccountJpaEntity> findByNumber(String number);
}
