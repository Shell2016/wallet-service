package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.AccountRepository;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private static final long USER_ID = 1L;
    private static final String USERNAME = "Ivan";
    private static final String PASSWORD = "123456";
    private static final long ACCOUNT_ID = 1L;
    private static final UserResponse EXPECTED_USER_RESPONSE = UserResponse.builder()
            .id(USER_ID)
            .username(USERNAME)
            .build();
    private static final Account ACCOUNT = Account.builder()
            .id(ACCOUNT_ID)
            .build();
    private static final User USER = User.builder()
            .id(USER_ID)
            .username(USERNAME)
            .password(PASSWORD)
            .account(ACCOUNT)
            .build();
    private static final UserRequest USER_REQUEST = UserRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final UserService userService = new UserService(userRepository, accountRepository, userMapper);

    @Test
    void createUser() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(USER);

        UserResponse userResponse = userService.createUser(USER_REQUEST);

        assertThat(userResponse).isEqualTo(EXPECTED_USER_RESPONSE);
    }

    @Test
    @DisplayName("createUser() with existing username")
    void createExistingUserShouldThrowException() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> userService.createUser(USER_REQUEST))
                .isInstanceOf(ResourceProcessingException.class)
                .hasMessage("Пользователь с таким именем уже существует!");
    }

    @Test
    void getUserIfValidCredentials() {
        when(userRepository.getByUsername(USERNAME)).thenReturn(Optional.of(USER));

        Optional<UserResponse> result = userService.getUserResponseIfValidCredentials(USERNAME, PASSWORD);

        assertThat(result).contains(EXPECTED_USER_RESPONSE);
    }

    @Test
    void getUserIfInvalidCredentials() {
        when(userRepository.getByUsername(USERNAME)).thenReturn(Optional.of(USER));

        Optional<UserResponse> result = userService.getUserResponseIfValidCredentials(USERNAME, "wrongpass");

        assertThat(result).isEmpty();
    }

    @Test
    void getUserByIdIfNotExists() {
        when(userRepository.getById(USER_ID)).thenReturn(Optional.empty());
        when(accountRepository.getByUserId(USER_ID)).thenReturn(Optional.of(ACCOUNT));

        Optional<User> result = userService.getUserById(USER_ID);

        assertThat(result).isEmpty();
    }

    @Test
    void getUserByIdIfExists() {
        when(userRepository.getById(USER_ID)).thenReturn(Optional.of(USER));
        when(accountRepository.getByUserId(USER_ID)).thenReturn(Optional.of(ACCOUNT));

        Optional<User> result = userService.getUserById(USER_ID);

        assertThat(result).contains(USER);
    }
}
