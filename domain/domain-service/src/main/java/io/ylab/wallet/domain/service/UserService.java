package io.ylab.wallet.domain.service;


import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service with user business logic.
 */
@RequiredArgsConstructor
@Service
public class UserService {

    /**
     * Repository for persisting user data.
     */
    private final UserRepository userRepository;
    /**
     * Service with account business logic.
     */
    private final AccountService accountService;
    /**
     * For mapping User to dtos and vice versa.
     */
    private final UserMapper userMapper;

    /**
     * Creates user with account in one transaction.
     *
     * @param userRequest with username and password
     * @return userResponse with id and username
     */
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username())) {
            throw new ResourceProcessingException("User already exists!");
        }
        User user = userMapper.userCreateRequestToUser(userRequest);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User savedUser = userRepository.save(user);
        Account account = accountService.save(savedUser);
        savedUser.setAccount(account);
        return userMapper.userToUserResponse(savedUser);
    }

    /**
     * Gets userDto if valid credentials.
     *
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
     *
     * @param id of user that we want to get
     * @return Optional of User
     */
    public Optional<User> getUserById(long id) {
        Account account = accountService.getByUserId(id)
                .orElseThrow(() -> new ResourceProcessingException("Cannot load balance!"));
        Optional<User> optionalUser = userRepository.getById(id);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        user.setAccount(account);
        return Optional.of(user);
    }
}
