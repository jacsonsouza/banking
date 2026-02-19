package com.bank.banking_core.infrastructure.web.account;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponse {
    private UUID id;
    private String number;
    private BigDecimal balance;
    private String status;

    public AccountResponse(UUID id, String number, BigDecimal balance, String status) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.status = status;
    }

    public UUID getId() { return id; }
    public String getNumber() { return number; }
    public BigDecimal getBalance() { return balance; }
    public String getStatus() { return status; }
}
