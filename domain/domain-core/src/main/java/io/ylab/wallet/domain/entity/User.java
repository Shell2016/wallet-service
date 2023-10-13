package io.ylab.wallet.domain.entity;

import lombok.*;

/**
 * User entity.
 */
@Getter
@Setter
@Builder
public class User {
    private long id;
    private final String username;
    private final String password;
    /**
     * AccountId of the user.
     */
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
