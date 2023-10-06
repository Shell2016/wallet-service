package io.ylab.wallet.service.database.storage;

import io.ylab.wallet.service.core.entity.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserStorage {
    private final Map<String, User> userMap = new HashMap<>();

    public void save(User user) {
        userMap.put(user.getUsername(), user);
    }

    public boolean existByUsername(String username) {
        return userMap.containsKey(username);
    }

    public User getUser(String username) {
        return userMap.get(username);
    }
}
