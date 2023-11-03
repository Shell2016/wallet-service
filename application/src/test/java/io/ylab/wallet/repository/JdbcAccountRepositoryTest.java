package io.ylab.wallet.repository;

import io.ylab.wallet.entity.AccountEntity;
import io.ylab.wallet.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JdbcAccountRepositoryTest extends JdbcIntegrationTestBase {

    private final JdbcAccountRepository jdbcAccountRepository;

    @Autowired
    public JdbcAccountRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcAccountRepository = new JdbcAccountRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("saving new account successful if user with given id exists")
    void saveSuccess() {
        AccountEntity accountToSave = AccountEntity.builder()
                .user(UserEntity.builder()
                        .id(3L)
                        .build())
                .balance(BigDecimal.ZERO)
                .build();

        assertThatThrownBy(() -> jdbcAccountRepository.save(accountToSave))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("saving new account without existing user with given id should throw exception")
    void saveThrowsException() {
        AccountEntity accountToSave = AccountEntity.builder()
                .user(UserEntity.builder()
                        .id(100L)
                        .build())
                .balance(BigDecimal.ZERO)
                .build();

        assertThatThrownBy(() -> jdbcAccountRepository.save(accountToSave))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("update successful")
    void update() {
        AccountEntity account = AccountEntity.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(9999.99))
                .build();

        assertThat(jdbcAccountRepository.getByUserId(1L).get().getBalance())
                .isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
        assertThat(jdbcAccountRepository.updateBalance(account)).isTrue();
        assertThat(jdbcAccountRepository.getByUserId(1L).get().getBalance()).isEqualTo(BigDecimal.valueOf(9999.99));
    }

    @Test
    @DisplayName("update not existing account")
    void updateNotExistingAccount() {
        AccountEntity account = AccountEntity.builder()
                .id(4L)
                .balance(BigDecimal.valueOf(9999.99))
                .build();

        assertThat(jdbcAccountRepository.updateBalance(account)).isFalse();
        assertThat(jdbcAccountRepository.getByUserId(4L)).isEmpty();
    }

    @Test
    @DisplayName("getByUserId return optional of account")
    void getByUserIdSuccess() {
        AccountEntity account = AccountEntity.builder()
                .id(2L)
                .balance(BigDecimal.valueOf(500.00))
                .build();

        Optional<AccountEntity> optionalAccount = jdbcAccountRepository.getByUserId(2L);
        assertThat(optionalAccount).contains(account);
        System.out.println(optionalAccount.get());
    }

    @Test
    @DisplayName("getByUserId returns empty optional")
    void getByUserIdFail() {
        assertThat(jdbcAccountRepository.getByUserId(4L)).isEmpty();
    }
}
