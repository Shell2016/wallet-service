package io.ylab.wallet.service.application.mapper;

import io.ylab.wallet.service.application.dto.UserDto;
import io.ylab.wallet.service.core.entity.User;

public class UserMapper {

    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getAccount());
    }
}
