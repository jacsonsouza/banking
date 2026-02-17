package com.bank.banking_core.infrastructure.web.account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bank.banking_core.application.account.CreateAccountUseCase;
import com.bank.banking_core.application.account.GetAccountByIdUseCase;
import com.bank.banking_core.domain.account.Account;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;

    public AccountController(
        CreateAccountUseCase createAccountUseCase,
        GetAccountByIdUseCase getAccountByIdUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(
        @Valid
        @RequestBody CreateAccountRequest request,
        UriComponentsBuilder uriBuilder
    ) {
        Account account = createAccountUseCase.execute(request.getNumber());

        AccountResponse response = new AccountResponse(
            account.getId(),
            account.getNumber(),
            account.getBalance(),
            account.getStatus().name()
        );

        URI location = uriBuilder.path("/accounts/{id}").buildAndExpand(account.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        Account account = getAccountByIdUseCase.execute(id);

        AccountResponse response = new AccountResponse(
            account.getId(),
            account.getNumber(),
            account.getBalance(),
            account.getStatus().name()
        );
    
        return ResponseEntity.ok(response);
    }
    
}
