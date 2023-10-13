package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.entity.UserEntity;

/**
 * Maps between domain entities and data access entities.
 */
public class UserDataAccessMapper {

    public User userEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }

    public UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
