package io.ylab.wallet.domain.mapper;


import io.ylab.wallet.domain.dto.*;
import io.ylab.wallet.domain.entity.User;

/**
 * Mapper for mapping User to dtos and vice versa.
 */
public class UserMapper {

    public User userCreateRequestToUser(UserCreateRequest request) {
        return User.builder()
                .username(request.username())
                .password(request.password())
                .build();
    }

    public UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
