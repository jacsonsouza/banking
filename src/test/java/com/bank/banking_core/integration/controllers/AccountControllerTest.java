package com.bank.banking_core.integration.controllers;

import static org.instancio.Select.field;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bank.banking_core.application.account.*;
import com.bank.banking_core.domain.account.Account;
import com.bank.banking_core.domain.exception.AccountAlreadyExistsException;
import com.bank.banking_core.infrastructure.web.account.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
@Import(ObjectMapper.class)
public class AccountControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CreateAccountUseCase createAccountUseCase;
  @MockitoBean private GetAccountByIdUseCase getAccountByIdUseCase;
  @MockitoBean private DepositUseCase depositUseCase;
  @MockitoBean private WithdrawUseCase withdrawUseCase;

  private Account account;

  @BeforeEach
  public void setUp() {
    BigDecimal initialBalance = BigDecimal.valueOf(1000.00);

    account = Instancio.of(Account.class).set(field(Account::getBalance), initialBalance).create();
  }

  @Test
  @WithMockUser
  public void testShouldCreateAcountSuccessfully() throws Exception {
    CreateAccountRequest request =
        Instancio.of(CreateAccountRequest.class)
            .set(field(CreateAccountRequest::number), "A123456")
            .create();

    when(createAccountUseCase.execute(request.number())).thenReturn(account);

    mockMvc
        .perform(
            post("/accounts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser
  public void testShouldNotCreateAcountIfAlreadyExists() throws Exception {
    CreateAccountRequest request =
        Instancio.of(CreateAccountRequest.class)
            .set(field(CreateAccountRequest::number), "A123456")
            .create();

    when(createAccountUseCase.execute(request.number()))
        .thenThrow(new AccountAlreadyExistsException());

    mockMvc
        .perform(
            post("/accounts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser
  public void testShouldNotCreateAcountIfNumberIsInvalid() throws Exception {
    CreateAccountRequest request =
        Instancio.of(CreateAccountRequest.class)
            .set(field(CreateAccountRequest::number), "A12")
            .create();

    mockMvc
        .perform(
            post("/accounts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  public void testShouldNotCreateAcountIfNumberIsBlank() throws Exception {
    CreateAccountRequest request =
        Instancio.of(CreateAccountRequest.class)
            .set(field(CreateAccountRequest::number), "")
            .create();

    mockMvc
        .perform(
            post("/accounts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  public void testShouldGetAccountSuccessfully() throws Exception {
    when(getAccountByIdUseCase.execute(account.getId())).thenReturn(account);

    mockMvc
        .perform(get("/accounts/{id}", account.getId()).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(account.getId().toString()))
        .andExpect(jsonPath("$.number").value(account.getNumber()))
        .andExpect(jsonPath("$.balance").value(account.getBalance()));
  }

  @Test
  @WithMockUser
  public void testShouldDepositSuccessfully() throws Exception {
    BigDecimal amountToDeposit = BigDecimal.valueOf(1000.00);

    DepositRequest request =
        Instancio.of(DepositRequest.class)
            .set(field(DepositRequest::amount), amountToDeposit)
            .create();

    when(depositUseCase.execute(account.getId(), amountToDeposit)).thenReturn(account);

    mockMvc
        .perform(
            patch("/accounts/{id}/deposit", account.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  public void testShouldNotDepositIfAmountIsNegative() throws Exception {
    BigDecimal amountToDeposit = BigDecimal.valueOf(-1000.00);

    DepositRequest request =
        Instancio.of(DepositRequest.class)
            .set(field(DepositRequest::amount), amountToDeposit)
            .create();

    mockMvc
        .perform(
            patch("/accounts/{id}/deposit", account.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  public void testShouldWithdrawSuccessfully() throws Exception {
    BigDecimal amountToWithdraw = BigDecimal.valueOf(500.00);

    WithdrawRequest request =
        Instancio.of(WithdrawRequest.class)
            .set(field(WithdrawRequest::amount), amountToWithdraw)
            .create();

    when(withdrawUseCase.execute(account.getId(), amountToWithdraw)).thenReturn(account);

    mockMvc
        .perform(
            patch("/accounts/{id}/withdraw", account.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  public void testShouldNotWithdrawIfAmountIsNegative() throws Exception {
    BigDecimal amountToWithdraw = BigDecimal.valueOf(-500.00);

    WithdrawRequest request =
        Instancio.of(WithdrawRequest.class)
            .set(field(WithdrawRequest::amount), amountToWithdraw)
            .create();

    mockMvc
        .perform(
            patch("/accounts/{id}/withdraw", account.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
