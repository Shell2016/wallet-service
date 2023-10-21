package io.ylab.wallet.domain.exception;

/**
 * Exception that is being thrown after failed authentication.
 */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
