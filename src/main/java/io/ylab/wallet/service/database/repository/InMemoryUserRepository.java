package io.ylab.wallet.service.database.repository;

import io.ylab.wallet.service.application.repository.UserRepository;
import io.ylab.wallet.service.core.entity.User;
import io.ylab.wallet.service.database.storage.InMemoryUserStorage;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

    private final InMemoryUserStorage inMemoryUserStorage;

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
