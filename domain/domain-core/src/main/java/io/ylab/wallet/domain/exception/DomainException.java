package io.ylab.wallet.domain.exception;

/**
 * Base exception class for wallet service.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
