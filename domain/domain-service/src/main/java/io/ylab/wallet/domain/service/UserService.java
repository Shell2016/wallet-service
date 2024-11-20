package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.entity.User;

import java.util.Optional;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    Optional<UserResponse> getUserResponseIfValidCredentials(String username, String password);

    Optional<User> getUserById(long id);
}
