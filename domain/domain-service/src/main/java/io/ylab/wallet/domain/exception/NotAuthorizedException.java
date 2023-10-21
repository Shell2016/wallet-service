package io.ylab.wallet.domain.exception;

/**
 * Being thrown when accessing not authorized resource.
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
