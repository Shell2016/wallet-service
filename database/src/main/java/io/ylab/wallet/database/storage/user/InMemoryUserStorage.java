package io.ylab.wallet.database.storage.user;


import io.ylab.wallet.domain.entity.User;

import java.util.*;

public class InMemoryUserStorage {
    private final Map<String, User> usernameKeyUserMap = new HashMap<>();
    private final Map<String, User> idKeyUserMap = new HashMap<>();

    public void save(User user) {
        usernameKeyUserMap.put(user.getUsername(), user);
        idKeyUserMap.put(user.getId().toString(), user);
    }

    public boolean existByUsername(String username) {
        return usernameKeyUserMap.containsKey(username);
    }

    public User getUserByUsername(String username) {
        return usernameKeyUserMap.get(username);
    }

    public User getUserById(String id) {
        return idKeyUserMap.get(id);
    }
}
