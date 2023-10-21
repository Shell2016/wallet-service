package io.ylab.wallet.domain.dto;

/**
 * Dto for transaction request that will be processed to make deposit or withdrawal.
 * @param id must be unique UUID given by user
 * @param type must be string 'deposit' or 'withdraw'
 * @param amount must be numeric and larger than 0
 */
public record TransactionRequest(String id,
                                 String type,
                                 String amount) {
}
