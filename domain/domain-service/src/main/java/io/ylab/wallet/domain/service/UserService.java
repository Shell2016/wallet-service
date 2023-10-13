package io.ylab.wallet.domain.service;


import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.AccountRepository;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Service with user business logic.
 */
@RequiredArgsConstructor
public class UserService {

    /**
     * Repository for persisting user data.
     */
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
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
    public UserResponse createUser(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        if (userRepository.existsByUsername(username)) {
            throw new ResourceProcessingException("Пользователь с таким именем уже существует!\n");
        }
        User savedUser = userRepository.save(user);
        System.out.println("Пользователь успешно создан!");
        return userMapper.userToUserResponse(savedUser);
    }

    /**
     * Gets userDto if valid credentials.
     * @param username to login
     * @param password to login
     * @return Optional of userDto if valid credentials, empty Optional otherwise.
     */
    public Optional<UserResponse> getUserResponseIfValidCredentials(String username, String password) {
        return userRepository.getByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(userMapper::userToUserResponse);
    }

    /**
     * Gets user with account.
     * @param id of user that we want to get
     * @return Optional of User
     */
    public Optional<User> getUserById(long id) {
        Account account = accountRepository.getByUserId(id)
                .orElseThrow(() -> new ResourceProcessingException("Не удалось загрузить аккаунт!"));
        Optional<User> optionalUser = userRepository.getById(id);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        user.setAccount(account);
        return Optional.of(user);
    }
}
