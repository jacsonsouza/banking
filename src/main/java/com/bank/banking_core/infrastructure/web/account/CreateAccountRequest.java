package com.bank.banking_core.infrastructure.web.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAccountRequest {
    @NotBlank
    @Size(min = 4, max = 20, message = "Account number must be between 4 and 20 characters")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
