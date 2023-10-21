package io.ylab.wallet.in.servlet;

import io.ylab.wallet.domain.dto.*;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.*;
import io.ylab.wallet.in.controller.UserServlet;
import io.ylab.wallet.in.util.JsonHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UserServletTest {

    private static final long USER1_ID = 1L;
    private static final String TRANSACTION_ID = "28aade72-45fe-49f6-a4bf-101f7cd89941";
    private static final String TYPE_WITHDRAW = "withdraw";
    private static final String AMOUNT = "1000";
    private static final TransactionRequest TRANSACTION_REQUEST = new TransactionRequest(TRANSACTION_ID, TYPE_WITHDRAW, AMOUNT);
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_TYPE =
            new TransactionRequest(TRANSACTION_ID, "type", AMOUNT);
    private static final TransactionDto TRANSACTION_DTO = TransactionDto.builder()
            .id(UUID.fromString(TRANSACTION_ID))
            .userId(USER1_ID)
            .amount(new BigDecimal(AMOUNT))
            .type(TransactionType.WITHDRAW)
            .createdAt(LocalDateTime.now())
            .build();
    private static final String MOCK_RESPONSE = "mock response";
    private static final String INVALID_PATH_FORMAT_ERROR_MESSAGE = "Неверный формат запроса!";
    private static final String RESOURCE_NOT_FOUND_ERROR_MESSAGE = "Ресурс не найден!";
    private static final String TRANSACTION_TYPE_ERROR_MESSAGE = "Поле тип транзакции должно иметь вид 'withdraw' или 'deposit'";
    public static final BalanceResponse BALANCE_RESPONSE = new BalanceResponse("1000");
    public static final long USER2_ID = 2L;
    public static final ArrayList<TransactionDto> MOCK_LIST = new ArrayList<>();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final PrintWriter writer = mock(PrintWriter.class);
    private final JsonHelper jsonHelper = mock(JsonHelper.class);
    private final AccountService accountService = mock(AccountService.class);
    private final TransactionService transactionService = mock(TransactionService.class);

    private final UserServlet userServlet = new UserServlet(accountService, transactionService, jsonHelper);

    @Test
    @DisplayName("doGet not valid path format")
    void doGetNotValidPath() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/test");
        when(jsonHelper.buildErrorResponse(INVALID_PATH_FORMAT_ERROR_MESSAGE, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doGet not valid resource path")
    void doGetNotValidResource() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/2/resource");
        when(jsonHelper.buildErrorResponse(RESOURCE_NOT_FOUND_ERROR_MESSAGE, HttpServletResponse.SC_NOT_FOUND))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doGet balance valid format")
    void doGetBalance() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/2/balance");
        when(accountService.getBalance(anyLong())).thenReturn(BALANCE_RESPONSE);
        when(jsonHelper.buildJsonFromObject(BALANCE_RESPONSE)).thenReturn(MOCK_RESPONSE);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doGet balance wrong user id")
    void doGetBalanceWrongUserId() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/2/balance");
        when(accountService.getBalance(anyLong())).thenThrow(new RuntimeException());
        when(jsonHelper.buildErrorResponse(null, HttpServletResponse.SC_NOT_FOUND))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doGet transactions")
    void doGetTransactions() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/2/transactions");
        when(transactionService.getUserTransactions(anyLong())).thenReturn(MOCK_LIST);
        when(jsonHelper.buildJsonFromObject(MOCK_LIST)).thenReturn(MOCK_RESPONSE);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doGet transactions wrong user id")
    void doGetTransactionsWrongUserId() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/2/transactions");
        when(transactionService.getUserTransactions(anyLong())).thenThrow(new RuntimeException());
        when(jsonHelper.buildErrorResponse(null, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doPut not valid path")
    void doPutNotValidPath() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/test");
        when(jsonHelper.buildErrorResponse(INVALID_PATH_FORMAT_ERROR_MESSAGE, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doPut not valid resource")
    void doPutNotValidResource() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/1/resource");
        when(jsonHelper.buildErrorResponse(RESOURCE_NOT_FOUND_ERROR_MESSAGE, HttpServletResponse.SC_NOT_FOUND))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doPut valid transaction request success")
    void doPutValidTransactionRequestSuccess() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/1/transaction");
        when(jsonHelper.getRequestBody(request, TransactionRequest.class))
                .thenReturn(TRANSACTION_REQUEST);
        when(transactionService.processTransaction(TRANSACTION_REQUEST, USER1_ID))
                .thenReturn(TRANSACTION_DTO);
        when(jsonHelper.buildJsonFromObject(TRANSACTION_DTO)).thenReturn(MOCK_RESPONSE);

        userServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doPut invalid transaction type")
    void doPutInvalidTransactionType() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/1/transaction");
        when(jsonHelper.getRequestBody(request, TransactionRequest.class))
                .thenReturn(TRANSACTION_REQUEST_INVALID_TYPE);
        when(jsonHelper.buildErrorResponse(TRANSACTION_TYPE_ERROR_MESSAGE, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE);
    }

    @Test
    @DisplayName("doPut valid transaction request error")
    void doPutValidTransactionRequestError() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn("/1/transaction");
        when(jsonHelper.getRequestBody(request, TransactionRequest.class))
                .thenReturn(TRANSACTION_REQUEST);
        when(transactionService.processTransaction(TRANSACTION_REQUEST, USER1_ID))
                .thenThrow(new RuntimeException());
        when(jsonHelper.buildErrorResponse(null, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE);

        userServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE);
    }
}
