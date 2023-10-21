package io.ylab.wallet.domain.exception;

/**
 * Thrown after validation errors.
 */
public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super(message);
    }
}
