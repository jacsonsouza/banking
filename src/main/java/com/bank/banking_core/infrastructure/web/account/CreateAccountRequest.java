package com.bank.banking_core.infrastructure.web.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(@NotBlank @Size(min = 4, max = 20) String number) {}
