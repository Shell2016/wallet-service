package io.ylab.wallet.domain.service;


import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

/**
 * Service with user business logic.
 */
@RequiredArgsConstructor
public class UserService {

    /**
     * Repository for persisting user data.
     */
    private final UserRepository userRepository;
    /**
     * For mapping User to dtos and vice versa.
     */
    private final UserMapper userMapper;

    /**
     * Creates new user.
     * @param username of new user
     * @param password of new user
     * @return UserDto of created user
     */
    public UserDto createUser(String username, String password) {
        User user = new User(UUID.randomUUID() ,username, password);
        if (userRepository.existsByUsername(username)) {
            throw new ResourceProcessingException("Пользователь с таким именем уже существует!\n");
        }
        UserDto userDto = userMapper.userToUserDto(userRepository.save(user));
        System.out.println("Пользователь успешно создан!");
        return userDto;
    }

    /**
     * Gets userDto if valid credentials.
     * @param username to login
     * @param password to login
     * @return Optional of userDto if valid credentials, empty Optional otherwise.
     */
    public Optional<UserDto> getUserDtoIfValidCredentials(String username, String password) {
        return userRepository.getByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(userMapper::userToUserDto);
    }

    /**
     * Gets current user's balance.
     * @return account balance as string
     */
    public String getBalance(String id) {
        return userRepository.getById(id)
                .map(user -> user.getAccount().getBalance().toString())
                .orElseThrow(() -> new ResourceProcessingException("Не удалось загрузить баланс!"));
    }

    /**
     * Gets user.
     * @param id of user that we want to get
     * @return Optional of User
     */
    public Optional<User> getUserById(String id) {
        return userRepository.getById(id);
    }

    /**
     * Updates user.
     * @param user to update
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
