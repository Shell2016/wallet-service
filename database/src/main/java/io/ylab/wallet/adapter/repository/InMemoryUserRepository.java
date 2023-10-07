package io.ylab.wallet.adapter.repository;


import io.ylab.wallet.database.storage.InMemoryUserStorage;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.port.output.repository.UserRepository;

import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final InMemoryUserStorage inMemoryUserStorage;

    public InMemoryUserRepository(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public User save(User user) {
        inMemoryUserStorage.save(user);
        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        return inMemoryUserStorage.existByUsername(username);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(inMemoryUserStorage.getUser(username));
    }
}
