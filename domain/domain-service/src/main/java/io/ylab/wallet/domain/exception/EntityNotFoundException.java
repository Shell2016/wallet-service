package io.ylab.wallet.domain.exception;

/**
 * Thrown if entity was not found.
 */
public class EntityNotFoundException extends DomainException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
