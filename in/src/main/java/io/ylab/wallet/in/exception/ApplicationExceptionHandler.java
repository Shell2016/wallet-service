package io.ylab.wallet.in.exception;

import io.ylab.wallet.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

/**
 * Global exception handler.
 */
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiError> handleValidationException(ValidationException e) {
        return ResponseEntity.badRequest().body(buildApiError(e, BAD_REQUEST));
    }

    @ExceptionHandler(BalanceValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiError> handleBalanceValidationException(BalanceValidationException e) {
        return ResponseEntity.badRequest().body(buildApiError(e, BAD_REQUEST));
    }

    @ExceptionHandler(ResourceProcessingException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiError> handleResourceProcessingException(ResourceProcessingException e) {
        return ResponseEntity.badRequest().body(buildApiError(e, BAD_REQUEST));
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiError> handleTransactionException(TransactionException e) {
        return ResponseEntity.badRequest().body(buildApiError(e, BAD_REQUEST));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(buildApiError(e, NOT_FOUND));
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ApiError> handleAuthException(AuthException e) {
        return ResponseEntity.status(UNAUTHORIZED).body(buildApiError(e, UNAUTHORIZED));
    }

    private ApiError buildApiError(Exception e, HttpStatus httpStatus) {
        return ApiError.builder()
                .message(e.getMessage())
                .status(httpStatus.value())
                .build();
    }
}
