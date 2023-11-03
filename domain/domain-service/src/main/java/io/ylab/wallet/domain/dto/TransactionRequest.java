package io.ylab.wallet.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Dto for transaction request that will be processed to make deposit or withdrawal.
 *
 * @param id     must be unique UUID given by user
 * @param type   must be string 'deposit' or 'withdraw'
 * @param amount must be numeric and larger than 0
 */
@Schema(description = "Request for processing transaction")
public record TransactionRequest(@Schema(description = "Should be unique and in uuid format")
                                 @NotBlank
                                 String id,
                                 @NotNull
                                 @Pattern(regexp = "deposit|withdraw")
                                 String type,
                                 @Schema(description = "Should be positive number")
                                 @Positive
                                 String amount) {
}
