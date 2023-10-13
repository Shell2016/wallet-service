package io.ylab.wallet.exception;

/**
 * Re-thrown after SQLException catch.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
