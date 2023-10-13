package io.ylab.wallet.domain.exception;

/**
 * Thrown when result of withdrawal operation would be less then 0.
 */
public class BalanceValidationException extends DomainException {
    public BalanceValidationException(String message) {
        super(message);
    }
}
