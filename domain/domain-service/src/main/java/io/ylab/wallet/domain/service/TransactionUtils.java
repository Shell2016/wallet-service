package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.exception.ValidationException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class TransactionUtils {

    public String[] processTransactionInput(String input) {
        String[] s = input.trim().split(" ");
        if (s.length != 2) {
            throw new ValidationException("Неверный формат!");
        }
        String uuid = s[0].trim();
        String amount = s[1].trim();
        try {
            UUID.fromString(uuid);
            new BigDecimal(amount);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неверный формат!");
        }
        return new String[] {uuid, amount};
    }
}
