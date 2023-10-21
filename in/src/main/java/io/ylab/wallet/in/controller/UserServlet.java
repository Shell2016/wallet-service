package io.ylab.wallet.in.controller;

import io.ylab.wallet.domain.dto.*;
import io.ylab.wallet.domain.service.AccountService;
import io.ylab.wallet.domain.service.TransactionService;
import io.ylab.wallet.in.util.DependencyContainer;
import io.ylab.wallet.in.util.JsonHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

import static io.ylab.wallet.in.validation.ValidationUtils.*;
import static jakarta.servlet.http.HttpServletResponse.*;

/**
 * Servlet that processes user requests in format /users/{id}/* :<br>
 * 'GET /users/35/balance'<br>
 * 'GET /users/10/transactions'<br>
 * 'PUT /users/10/transaction'<br>
 */
@WebServlet("/users/*")
@AllArgsConstructor
@NoArgsConstructor
public class UserServlet extends HttpServlet {

    private AccountService accountService;
    private TransactionService transactionService;
    private JsonHelper jsonHelper;

    @Override
    public void init() {
        accountService = DependencyContainer.getAccountService();
        transactionService = DependencyContainer.getTransactionService();
        jsonHelper = DependencyContainer.getJsonHelper();
    }

    /**
     * Processes requests: GET /users/{id}/balance, /users/{id}/transactions
     *
     * @param req  an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            validatePath(req);
        } catch (Exception e) {
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_BAD_REQUEST));
            return;
        }
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String idString = pathParts[1];
        String resource = pathParts[2];
        Long id = Long.parseLong(idString);
        switch (resource) {
            case "balance" -> getBalance(resp, id);
            case "transactions" -> getTransactions(resp, id);
            default -> {
                resp.setStatus(SC_NOT_FOUND);
                resp.getWriter().write(jsonHelper.buildErrorResponse("Ресурс не найден!", SC_NOT_FOUND));
            }
        }
    }

    /**
     * Processes request: PUT /users/{id}/transaction (deposit or withdraw to/from account)
     *
     * @param req  the {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException
     */
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            validatePath(req);
        } catch (Exception e) {
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_BAD_REQUEST));
            return;
        }
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String idString = pathParts[1];
        String transaction = pathParts[2];
        long id = Long.parseLong(idString);
        if (!"transaction".equals(transaction)) {
            resp.setStatus(SC_NOT_FOUND);
            resp.getWriter().write(jsonHelper.buildErrorResponse("Ресурс не найден!", SC_NOT_FOUND));
        } else {
            TransactionRequest transactionRequest = jsonHelper.getRequestBody(req, TransactionRequest.class);
            makeTransaction(transactionRequest, id, resp);
        }
    }

    /**
     * Processes transaction request.
     *
     * @param transactionRequest TransactionRequest
     * @param userId
     * @param resp               the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException
     */
    private void makeTransaction(TransactionRequest transactionRequest, long userId, HttpServletResponse resp)
            throws IOException {
        TransactionDto transaction;
        try {
            validateTransactionRequest(transactionRequest);
            transaction = transactionService.processTransaction(transactionRequest, userId);
        } catch (Exception e) {
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_BAD_REQUEST));
            return;
        }
        resp.setStatus(SC_OK);
        resp.getWriter().write(jsonHelper.buildJsonFromObject(transaction));
    }

    /**
     * Processes get user transactions request.
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @param id   userId
     * @throws IOException
     */
    private void getTransactions(HttpServletResponse resp, Long id) throws IOException {
        List<TransactionDto> transactions;
        try {
            transactions = transactionService.getUserTransactions(id);
        } catch (Exception e) {
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_BAD_REQUEST));
            return;
        }
        resp.setStatus(SC_OK);
        resp.getWriter().write(jsonHelper.buildJsonFromObject(transactions));
    }

    /**
     * Processes request for getting user's balance.
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @param id   userId
     * @throws IOException
     */
    private void getBalance(HttpServletResponse resp, Long id) throws IOException {
        BalanceResponse balance;
        try {
            balance = accountService.getBalance(id);
        } catch (Exception e) {
            resp.setStatus(SC_NOT_FOUND);
            resp.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_NOT_FOUND));
            return;
        }
        resp.setStatus(SC_OK);
        resp.getWriter().write(jsonHelper.buildJsonFromObject(balance));
    }
}
