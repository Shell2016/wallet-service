package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    public static final UUID USER_UUID = UUID.fromString("adde1e02-1784-4973-956c-80d064309d55");
    public static final String USERNAME = "Ivan";
    public static final String PASSWORD = "123456";

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserMapper userMapper = new UserMapper();
    private final UserService userService = new UserService(userRepository, userMapper);

    private UserDto expectedUserDto;
    private User user;

    @BeforeEach
    void init() {
        expectedUserDto = UserDto.builder()
                .id(USER_UUID)
                .account(new Account(USER_UUID))
                .build();

        user = new User(USER_UUID, USERNAME, PASSWORD);
    }

    @Test
    void userToUserDtoMapperTest() {
        assertThat(userMapper.userToUserDto(user)).isEqualTo(expectedUserDto);
    }

    @Test
    void createUser() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = userService.createUser(USERNAME, PASSWORD);
        assertThat(userDto).isEqualTo(expectedUserDto);
    }

    @Test
    void createExistingUserShouldThrowException() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(USERNAME, PASSWORD))
                .isInstanceOf(ResourceProcessingException.class)
                .hasMessage("Пользователь с таким именем уже существует!\n");
    }

    @Test
    void getUserIfValidCredentials() {
        when(userRepository.getByUsername(USERNAME)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserDtoIfValidCredentials(USERNAME, PASSWORD);

        assertThat(result).contains(expectedUserDto);
    }

    @Test
    void getUserIfInvalidCredentials() {
        when(userRepository.getByUsername(USERNAME)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserDtoIfValidCredentials(USERNAME, "wrongpass");

        assertThat(result).isEmpty();
    }

    @Test
    void getBalance() {
        when(userRepository.getById(USER_UUID.toString())).thenReturn(Optional.of(user));
        String balance = userService.getBalance(USER_UUID.toString());

        assertThat(balance).isEqualTo("0.00");
    }

    @Test
    void getUserByIdIfNotExists() {
        when(userRepository.getById(USER_UUID.toString())).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(USER_UUID.toString());

        assertThat(result).isEmpty();
    }

    @Test
    void getUserByIdIfExists() {
        when(userRepository.getById(USER_UUID.toString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(USER_UUID.toString());

        assertThat(result).contains(user);
    }

    @Test
    void updateUser() {
        userService.updateUser(user);

        verify(userRepository).save(user);
    }
}
