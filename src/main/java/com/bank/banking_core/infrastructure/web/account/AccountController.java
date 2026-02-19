package com.bank.banking_core.infrastructure.web.account;

import com.bank.banking_core.application.account.CreateAccountUseCase;
import com.bank.banking_core.application.account.DepositUseCase;
import com.bank.banking_core.application.account.GetAccountByIdUseCase;
import com.bank.banking_core.application.account.WithdrawUseCase;
import com.bank.banking_core.domain.account.Account;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/accounts")
public class AccountController {
  private final CreateAccountUseCase createAccountUseCase;
  private final GetAccountByIdUseCase getAccountByIdUseCase;
  private final DepositUseCase depositUseCase;
  private final WithdrawUseCase withdrawUseCase;

  public AccountController(
      CreateAccountUseCase createAccountUseCase,
      GetAccountByIdUseCase getAccountByIdUseCase,
      DepositUseCase depositUseCase,
      WithdrawUseCase withdrawUseCase) {
    this.createAccountUseCase = createAccountUseCase;
    this.getAccountByIdUseCase = getAccountByIdUseCase;
    this.depositUseCase = depositUseCase;
    this.withdrawUseCase = withdrawUseCase;
  }

  @PostMapping
  public ResponseEntity<AccountResponse> create(
      @Valid @RequestBody CreateAccountRequest request, UriComponentsBuilder uriBuilder) {
    Account account = createAccountUseCase.execute(request.number());

    URI location = uriBuilder.path("/accounts/{id}").buildAndExpand(account.getId()).toUri();

    return ResponseEntity.created(location).body(toResponse(account));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
    Account account = getAccountByIdUseCase.execute(id);

    return ResponseEntity.ok(toResponse(account));
  }

  @PatchMapping("/{id}/deposit")
  public ResponseEntity<AccountResponse> deposit(
      @PathVariable UUID id, @Valid @RequestBody DepositRequest request) {
    Account account = depositUseCase.execute(id, request.amount());

    return ResponseEntity.ok(toResponse(account));
  }

  @PatchMapping("/{id}/withdraw")
  public ResponseEntity<AccountResponse> withdraw(
      @PathVariable UUID id, @Valid @RequestBody WithdrawRequest request) {
    Account account = withdrawUseCase.execute(id, request.amount());

    return ResponseEntity.ok(toResponse(account));
  }

  private AccountResponse toResponse(Account account) {
    return new AccountResponse(
        account.getId(), account.getNumber(), account.getBalance(), account.getStatus().name());
  }
}
