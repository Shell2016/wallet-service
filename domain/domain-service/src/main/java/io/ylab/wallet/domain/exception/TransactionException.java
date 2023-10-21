package io.ylab.wallet.domain.exception;

/**
 * Thrown while processing transactions.
 */
public class TransactionException extends DomainException {
    public TransactionException(String message) {
        super(message);
    }
}
