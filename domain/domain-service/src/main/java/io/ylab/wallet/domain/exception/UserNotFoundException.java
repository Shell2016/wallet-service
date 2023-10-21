package io.ylab.wallet.domain.exception;

/**
 * Thrown if user was not found.
 */
public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
