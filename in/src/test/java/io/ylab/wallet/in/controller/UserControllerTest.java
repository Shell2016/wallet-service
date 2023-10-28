package io.ylab.wallet.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.wallet.domain.dto.*;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.AccountService;
import io.ylab.wallet.domain.service.TransactionService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final long USER_ID1 = 1L;

    private static final String UUID = "d8d723f8-4dcf-4ca4-9ef0-6f5b1b7215a4";
    private static final String AMOUNT_1000 = "1000";
    private static final TransactionDto TRANSACTION_DTO = TransactionDto.builder()
            .userId(USER_ID1)
            .id(java.util.UUID.fromString(UUID))
            .type(TransactionType.DEPOSIT)
            .createdAt(LocalDateTime.of(2023, 11, 1, 12, 0))
            .amount(new BigDecimal(AMOUNT_1000))
            .build();
    private static final String TRANSACTION_DTO_JSON = """
            {"id":"d8d723f8-4dcf-4ca4-9ef0-6f5b1b7215a4","user_id":1,"amount":1000,"type":"DEPOSIT","created_at":"2023-11-01 12:00:00"}
            """;
    private static final String TRANSACTION_DTO_LIST_JSON = """
            [{"id":"d8d723f8-4dcf-4ca4-9ef0-6f5b1b7215a4","user_id":1,"amount":1000,"type":"DEPOSIT","created_at":"2023-11-01 12:00:00"}]
            """;
    private static final TransactionRequest TRANSACTION_REQUEST =
            new TransactionRequest(UUID, "deposit", AMOUNT_1000);
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_TYPE =
            new TransactionRequest(UUID, "depo", AMOUNT_1000);
    private static final BalanceResponse BALANCE_RESPONSE = new BalanceResponse(AMOUNT_1000);

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private UserController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .alwaysExpect(content().contentType(APPLICATION_JSON))
                .alwaysExpect(status().isOk())
                .build();
    }

    @Test
    @DisplayName("getBalance success")
    void getBalance() throws Exception {
        when(accountService.getBalance(1L)).thenReturn(BALANCE_RESPONSE);

        mockMvc.perform(get("/users/1/balance"))
                .andExpect(jsonPath("$.balance").value("1000"));
    }

    @Test
    @DisplayName("getBalance throws exception")
    void getBalanceThrowsException() {
        when(accountService.getBalance(1L)).thenThrow(new RuntimeException());

        assertThatException().isThrownBy(() -> mockMvc.perform(get("/users/1/balance")));
    }

    @Test
    @DisplayName("getTransactions success")
    void getTransactions() throws Exception {
        when(transactionService.getUserTransactions(USER_ID1)).thenReturn(Collections.singletonList(TRANSACTION_DTO));

        mockMvc.perform(get("/users/1/transactions"))
                .andExpect(content().json(TRANSACTION_DTO_LIST_JSON));
    }

    @Test
    @DisplayName("getTransactions should throw exception")
    void getTransactionThrowsException() {
        when(transactionService.getUserTransactions(USER_ID1)).thenThrow(new RuntimeException());

        assertThatException().isThrownBy(() -> mockMvc.perform(get("/users/1/transactions")));
    }

    @Test
    @DisplayName("makeTransaction should throw exception if invalid input")
    void makeTransactionInvalidInput() {
        assertThatException().isThrownBy(
                () -> mockMvc.perform(put("/users/1/transaction")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TRANSACTION_REQUEST_INVALID_TYPE)))
        );
    }


    @Test
    @DisplayName("makeTransaction success")
    void makeTransactionSuccess() throws Exception {
        when(transactionService.processTransaction(TRANSACTION_REQUEST, USER_ID1))
                .thenReturn(TRANSACTION_DTO);

        mockMvc.perform(put("/users/1/transaction")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TRANSACTION_REQUEST)))
                .andExpect(content().json(TRANSACTION_DTO_JSON));
    }
}
