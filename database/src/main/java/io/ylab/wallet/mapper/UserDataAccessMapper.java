package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * Maps between domain entities and data access entities.
 */
@Mapper(componentModel = "spring")
public interface UserDataAccessMapper {

    User userEntityToUser(UserEntity userEntity);
    UserEntity userToUserEntity(User user);
}
