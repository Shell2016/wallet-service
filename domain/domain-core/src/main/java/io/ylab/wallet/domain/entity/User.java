package io.ylab.wallet.domain.entity;

import lombok.Getter;

import java.util.UUID;

/**
 * User entity.
 */
@Getter
public class User {
    private final UUID id;
    private final String username;
    private final String password;
    /**
     * Account of the user.
     * Initialized via constructor phase with userId.
     */
    private final Account account;

    public User(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.account = new Account(id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
