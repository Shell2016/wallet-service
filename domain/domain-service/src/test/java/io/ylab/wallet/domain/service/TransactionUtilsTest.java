package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TransactionUtilsTest {

    public static final String UUID_INVALID = "123456789 500";
    public static final String AMOUNT_INVALID = "737e3e32-a482-41b8-bcd5-ea92fe12325d abc";
    public static final String VALID_INPUT = "737e3e32-a482-41b8-bcd5-ea92fe12325d 500";
    public static final String UUID = "737e3e32-a482-41b8-bcd5-ea92fe12325d";
    public static final String AMOUNT = "500";

    public static final String VALIDATION_EXCEPTION_MESSAGE = "Неверный формат!";

    @Test
    void processInputShouldThrowExceptionIfUuidIsNotValid() {
        assertThatThrownBy(() -> TransactionUtils.processTransactionInput(UUID_INVALID))
                .isInstanceOf(ValidationException.class)
                .hasMessage(VALIDATION_EXCEPTION_MESSAGE)
                .hasNoCause();
    }

    @Test
    void processInputShouldThrowExceptionIfAmountIsNotValid() {
        assertThatThrownBy(() -> TransactionUtils.processTransactionInput(AMOUNT_INVALID))
                .isInstanceOf(ValidationException.class)
                .hasMessage(VALIDATION_EXCEPTION_MESSAGE)
                .hasNoCause();
    }

    @Test
    void processInputShouldNotThrowException() {
        assertThatNoException().isThrownBy(() -> TransactionUtils.processTransactionInput(VALID_INPUT));
    }

    @Test
    void checkOutputOfProcessInputMethod() {
        String[] result = TransactionUtils.processTransactionInput(VALID_INPUT);

        assertThat(result[0]).isEqualTo(UUID);
        assertThat(result[1]).isEqualTo(AMOUNT);
    }
}
