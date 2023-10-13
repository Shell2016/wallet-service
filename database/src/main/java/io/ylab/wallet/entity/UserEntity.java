package io.ylab.wallet.entity;

import lombok.*;

/**
 * Data access entity.
 */
@Getter
@Setter
@ToString
@Builder
public class UserEntity {

    private long id;
    private String username;
    private String password;
    private AccountEntity account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + username.hashCode();
        return result;
    }
}
