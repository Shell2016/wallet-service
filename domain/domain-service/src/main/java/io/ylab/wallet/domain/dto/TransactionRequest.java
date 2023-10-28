package io.ylab.wallet.domain.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Dto for transaction request that will be processed to make deposit or withdrawal.
 *
 * @param id     must be unique UUID given by user
 * @param type   must be string 'deposit' or 'withdraw'
 * @param amount must be numeric and larger than 0
 */
public record TransactionRequest(@ApiModelProperty(required = true, notes = "Should be uuid format")
                                 String id,
                                 @ApiModelProperty(required = true, notes = "deposit or withdraw")
                                 String type,
                                 @ApiModelProperty(required = true, notes = "Should be positive number")
                                 String amount) {
}
