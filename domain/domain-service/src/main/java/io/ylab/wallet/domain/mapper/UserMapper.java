package io.ylab.wallet.domain.mapper;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for user-dto mappings.
 */
@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    User userCreateRequestToUser(UserRequest userRequest);

    UserResponse userToUserResponse(User user);
}
