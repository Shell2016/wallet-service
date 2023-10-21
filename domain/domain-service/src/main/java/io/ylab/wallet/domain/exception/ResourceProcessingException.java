package io.ylab.wallet.domain.exception;

/**
 * Thrown after errors while processing resources.
 */
public class ResourceProcessingException extends DomainException {
    public ResourceProcessingException(String message) {
        super(message);
    }
}
