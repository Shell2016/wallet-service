package io.ylab.wallet.in.validation;

import io.ylab.wallet.domain.dto.TransactionRequest;
import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ValidationUtilsTest {

    private static final String TRANSACTION_ID = "28aade72-45fe-49f6-a4bf-101f7cd89941";
    private static final String TYPE_WITHDRAW = "withdraw";
    private static final String AMOUNT = "1000";
    private static final TransactionRequest TRANSACTION_REQUEST_VALID =
            new TransactionRequest(TRANSACTION_ID, TYPE_WITHDRAW, AMOUNT);
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_TYPE =
            new TransactionRequest(TRANSACTION_ID, "type", AMOUNT);
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_ID =
            new TransactionRequest("1", TYPE_WITHDRAW, AMOUNT);
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_AMOUNT_TEXT =
            new TransactionRequest(TRANSACTION_ID, TYPE_WITHDRAW, "amounttext");
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_AMOUNT_ZERO =
            new TransactionRequest(TRANSACTION_ID, TYPE_WITHDRAW, "0");
    private static final TransactionRequest TRANSACTION_REQUEST_INVALID_AMOUNT_LESS_THAN_ZERO =
            new TransactionRequest(TRANSACTION_ID, TYPE_WITHDRAW, "-100");
    private static final String USERNAME_1 = "user1";
    private static final String PASSWORD = "123456";
    private static final UserRequest USER_REQUEST = UserRequest.builder()
            .username(USERNAME_1)
            .password(PASSWORD)
            .build();
    private static final UserRequest USER_REQUEST_USERNAME_NULL = UserRequest.builder()
            .password(PASSWORD)
            .build();
    private static final UserRequest USER_REQUEST_USERNAME_EMPTY = UserRequest.builder()
            .username(" ")
            .password(PASSWORD)
            .build();
    private static final UserRequest USER_REQUEST_PASSWORD_NULL = UserRequest.builder()
            .username(USERNAME_1)
            .build();
    private static final UserRequest USER_REQUEST_PASSWORD_LENGHT_LESS_THAN_6 = UserRequest.builder()
            .username(USERNAME_1)
            .password("12345")
            .build();
    private static final String INVALID_PATH1 = "/d/balance";
    private static final String INVALID_PATH2 = "/1";
    private static final String INVALID_PATH3 = "/1/balance/transactions";
    private static final String INVALID_PATH4 = "";
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    @Test
    @DisplayName("validate transaction: input should not throw exception")
    void validTransactionInput() {
        assertThatNoException()
                .isThrownBy(() -> ValidationUtils.validateTransactionRequest(TRANSACTION_REQUEST_VALID));
    }

    @ParameterizedTest
    @DisplayName("validate transaction: invalid inputs should throw exception")
    @MethodSource("getInvalidTransactionDtoArguments")
    void testInvalidTransactionRequests(TransactionRequest transactionRequest) {
        assertThatThrownBy(() -> ValidationUtils.validateTransactionRequest(transactionRequest))
                .isInstanceOf(ValidationException.class);
    }

    private static Stream<Arguments> getInvalidTransactionDtoArguments() {
        return Stream.of(
                Arguments.of(TRANSACTION_REQUEST_INVALID_ID),
                Arguments.of(TRANSACTION_REQUEST_INVALID_AMOUNT_TEXT),
                Arguments.of(TRANSACTION_REQUEST_INVALID_AMOUNT_ZERO),
                Arguments.of(TRANSACTION_REQUEST_INVALID_AMOUNT_LESS_THAN_ZERO),
                Arguments.of(TRANSACTION_REQUEST_INVALID_TYPE)
        );
    }

    @Test
    @DisplayName("validate user request: valid input should not throw exception")
    void validateUserCreateRequestValidInput() {
        assertThatNoException()
                .isThrownBy(() -> ValidationUtils.validateUserRequest(USER_REQUEST));
    }

    @ParameterizedTest
    @DisplayName("validate user request: invalid inputs should throw exception")
    @MethodSource("getInvalidUserRequestArguments")
    void validateUserCreateRequestInvalidInput(UserRequest userRequest) {
        assertThatThrownBy(() -> ValidationUtils.validateUserRequest(userRequest))
                .isInstanceOf(ValidationException.class);
    }

    private static Stream<Arguments> getInvalidUserRequestArguments() {
        return Stream.of(
                Arguments.of(USER_REQUEST_PASSWORD_LENGHT_LESS_THAN_6),
                Arguments.of(USER_REQUEST_PASSWORD_NULL),
                Arguments.of(USER_REQUEST_USERNAME_NULL),
                Arguments.of(USER_REQUEST_USERNAME_EMPTY)
        );
    }

    @Test
    @DisplayName("validate path: valid input should not throw exception")
    void validatePathValidInput() {
        when(request.getPathInfo()).thenReturn("/1/balance");

        assertThatNoException().isThrownBy(() -> ValidationUtils.validatePath(request));
    }

    @ParameterizedTest
    @DisplayName("validate path: invalid input should throw exception")
    @MethodSource("getInvalidPathArguments")
    void validatePathInvalidInput(String path) {
        when(request.getPathInfo()).thenReturn(path);

        assertThatThrownBy(() -> ValidationUtils.validatePath(request))
                .isInstanceOf(ValidationException.class);
    }

    private static Stream<Arguments> getInvalidPathArguments() {
        return Stream.of(
                Arguments.of(INVALID_PATH1),
                Arguments.of(INVALID_PATH2),
                Arguments.of(INVALID_PATH3),
                Arguments.of(INVALID_PATH4)
        );
    }
}
