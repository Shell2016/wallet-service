package io.ylab.wallet.in.validation;

import io.ylab.wallet.domain.dto.TransactionRequest;
import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Utility class for validation of user requests.
 */
@UtilityClass
public class ValidationUtils {

    /**
     * Validation of UserRequest input parameters.
     * @param userRequest to validate
     */
    public void validateUserCreateRequest(UserRequest userRequest) {
        if (userRequest.username() == null || userRequest.username().isBlank()) {
            throw new ValidationException("Поле username не должно быть пустым!");
        }
        if (userRequest.password() == null || userRequest.password().trim().length() < 6) {
            throw new ValidationException("Поле password должно быть не менее 6 символов!");
        }
    }

    /**
     * Validation of transactionRequest input parameters.
     * @param transactionRequest to validate
     */
    public void validateTransactionRequest(TransactionRequest transactionRequest) {
        if (transactionRequest.type().isBlank() ||
            !"withdraw".equalsIgnoreCase(transactionRequest.type()) &&
            !"deposit".equalsIgnoreCase(transactionRequest.type())
            ) {
            throw new ValidationException("Поле тип транзакции должно иметь вид 'withdraw' или 'deposit'");
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(transactionRequest.amount());
        } catch (NumberFormatException e) {
            throw new ValidationException("Неверный формат суммы транзакции!");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Сумма должна быть больше нуля!");
        }
        try {
            UUID.fromString(transactionRequest.id());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Id транзакции должен иметь формат uuid");
        }
    }

    /**
     * Validates paths like '/users/1/balance'
     * @param req request
     */
    public void validatePath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length != 3) {
            throw new ValidationException("Неверный формат запроса!");
        }
        String idString = pathParts[1];
        try {
            Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new ValidationException("Id должен иметь числовой формат!");
        }
    }
}
