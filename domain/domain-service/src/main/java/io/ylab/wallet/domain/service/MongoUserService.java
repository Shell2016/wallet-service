package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("mongo")
public class MongoUserService extends UserServiceImpl {

    public MongoUserService(UserRepository userRepository,
                            AccountService accountService,
                            UserMapper userMapper) {
        super(userRepository, accountService, userMapper);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        UserResponse user = super.createUser(userRequest);
        // TODO: 11/20/24 publish event
        return user;
    }

    @Override
    public Optional<UserResponse> getUserResponseIfValidCredentials(String username, String password) {
        return super.getUserResponseIfValidCredentials(username, password);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return super.getUserById(id);
    }
}
