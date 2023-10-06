package io.ylab.wallet.service.application.repository;

import io.ylab.wallet.service.core.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    boolean existsByUsername(String username);

    Optional<User> getByUsername(String username);

}
