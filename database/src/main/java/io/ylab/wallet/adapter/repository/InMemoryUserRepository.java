package io.ylab.wallet.adapter.repository;


import io.ylab.wallet.database.storage.InMemoryUserStorage;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.port.output.repository.UserRepository;

import java.util.Optional;

/**
 * In-memory implementation of UserRepository.
 */
public class InMemoryUserRepository implements UserRepository {

    /**
     * Data storage for user information.
     */
    private final InMemoryUserStorage inMemoryUserStorage;

    public InMemoryUserRepository(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    /**
     * Saves user to database.
     * @param user to save
     * @return saved user
     */
    @Override
    public User save(User user) {
        inMemoryUserStorage.save(user);
        return user;
    }

    /**
     * Checks if user exist by provided username
     * @param username to check
     * @return true if user exist in database
     */
    @Override
    public boolean existsByUsername(String username) {
        return inMemoryUserStorage.existByUsername(username);
    }

    /**
     * Gets user by username.
     * @param username of user to get
     * @return Optional of user
     */
    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(inMemoryUserStorage.getUserByUsername(username));
    }

    /**
     * Gets user by id.
     * @param id of user to get
     * @return Optional of user
     */
    @Override
    public Optional<User> getById(String id) {
        return Optional.ofNullable(inMemoryUserStorage.getUserById(id));
    }
}
