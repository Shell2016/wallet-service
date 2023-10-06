package io.ylab.wallet.service.application.service;

import io.ylab.wallet.service.application.dto.UserDto;
import io.ylab.wallet.service.application.mapper.UserMapper;
import io.ylab.wallet.service.application.repository.UserRepository;
import io.ylab.wallet.service.core.entity.User;
import io.ylab.wallet.service.core.exception.ResourceProcessingException;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.userMapper = mapper;
    }

    public UserDto createUser(String username, String password) {
        User user = new User(username, password);
        if (userRepository.existsByUsername(username)) {
            throw new ResourceProcessingException("Пользователь с таким именем уже существует!\n");
        }
        user.setId(UUID.randomUUID());
        UserDto userDto = userMapper.userToUserDto(userRepository.save(user));
        System.out.println("Пользователь успешно создан!");
        return userDto;
    }

    public Optional<UserDto> getUserIfValidCredentials(String username, String password) {
        return userRepository.getByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(userMapper::userToUserDto);
    }
}
