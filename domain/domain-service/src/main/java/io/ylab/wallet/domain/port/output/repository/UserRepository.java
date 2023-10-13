package io.ylab.wallet.domain.port.output.repository;

import io.ylab.wallet.domain.entity.User;

import java.util.Optional;

/**
 * Interface for interacting with user data storage.
 * Acts as output port in onion architecture.
 */
public interface UserRepository {

    User save(User user);

    boolean existsByUsername(String username);

    Optional<User> getByUsername(String username);

    Optional<User> getById(long id);
}
