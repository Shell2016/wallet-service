package io.ylab.wallet.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.wallet.domain.dto.*;
import io.ylab.wallet.domain.service.AccountService;
import io.ylab.wallet.domain.service.TransactionService;
import io.ylab.wallet.in.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for user requests.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User management")
public class UserController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    /**
     * Get balance of user with given id.
     *
     * @param id of the user
     * @return balance response with current balance
     */
    @Operation(summary = "Get user balance")
    @GetMapping(path = "/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable long id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    /**
     * Get list of all user's transactions.
     *
     * @param id of the user
     * @return list of user transactions
     */
    @Operation(summary = "Get list of user's transactions")
    @GetMapping(path = "/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable long id) {
        return ResponseEntity.ok(transactionService.getUserTransactions(id));
    }

    /**
     * Makes transaction. Transaction id should be in uuid format and unique.
     *
     * @param id      of the user
     * @param request with transaction parameters
     * @return TransactionDto
     */
    @Operation(summary = "Make new transaction")
    @PutMapping(path = "/{id}/transaction")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionDto> makeTransaction(@PathVariable long id,
                                                          @RequestBody TransactionRequest request) {
        ValidationUtils.validateTransactionRequest(request);
        return ResponseEntity.ok(transactionService.processTransaction(request, id));
    }
}
