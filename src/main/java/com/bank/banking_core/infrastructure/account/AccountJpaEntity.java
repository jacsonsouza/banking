package com.bank.banking_core.infrastructure.account;

import java.math.BigDecimal;
import java.util.UUID;

import com.bank.banking_core.domain.account.Account;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "account")
@Getter
public class AccountJpaEntity {
    @Id
    private UUID id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    protected AccountJpaEntity() {}

    public AccountJpaEntity(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.status = account.getStatus().name();
        this.createdAt = account.getCreatedAt().toString();
        this.updatedAt = account.getUpdatedAt().toString();
    }
}
