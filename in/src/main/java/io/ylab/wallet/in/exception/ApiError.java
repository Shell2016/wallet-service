package io.ylab.wallet.in.exception;

import lombok.Builder;

/**
 * Error dto that serves as response for API errors.
 * @param message Exception message.
 * @param status HttpStatus as int
 */
@Builder
public record ApiError(String message,
                       int status) {
}
