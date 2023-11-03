package io.ylab.wallet.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Dto for user create and auth actions.
 *
 * @param username must be not empty
 * @param password length must be >= 6
 */
@Builder
public record UserRequest(@NotBlank
                          String username,
                          @Min(6L)
                          @Schema(description = "Should be at least 6 characters long")
                          String password) {
}
