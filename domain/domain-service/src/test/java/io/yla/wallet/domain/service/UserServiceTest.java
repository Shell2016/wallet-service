package io.yla.wallet.domain.service;

import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import io.ylab.wallet.domain.service.UserService;
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

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserMapper userMapper = new UserMapper();
    UserService userService = new UserService(userRepository, userMapper);

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

        Optional<UserDto> result = userService.getUserIfValidCredentials(USERNAME, PASSWORD);

        assertThat(result.get()).isEqualTo(expectedUserDto);
    }

    @Test
    void getUserIfInvalidCredentials() {
        when(userRepository.getByUsername(USERNAME)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserIfValidCredentials(USERNAME, "wrongpass");

        assertThat(result).isEmpty();
    }
}
