package io.ylab.wallet.domain.mapper;


import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.User;

/**
 * Mapper for mapping User to dtos and vice versa.
 */
public class UserMapper {

    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getAccount());
    }
}
