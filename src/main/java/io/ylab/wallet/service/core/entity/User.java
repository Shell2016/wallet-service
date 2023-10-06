package io.ylab.wallet.service.core.entity;

import lombok.Builder;

import java.util.UUID;

public class User extends BaseEntity<UUID> {
    private final String username;
    private final String password;
    private final Account account = new Account();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Account getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
