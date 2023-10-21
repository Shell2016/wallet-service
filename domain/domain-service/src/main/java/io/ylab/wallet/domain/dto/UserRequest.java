package io.ylab.wallet.domain.dto;

import lombok.Builder;

/**
 * Dto for user create and auth actions.
 * @param username must be not empty
 * @param password length must be >= 6
 */
@Builder
public record UserRequest(String username,
                          String password) {
}
