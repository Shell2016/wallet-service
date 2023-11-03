package io.ylab.wallet.repository;

import io.ylab.wallet.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcUserRepositoryTest extends JdbcIntegrationTestBase {

    private static final String USERNAME_1 = "user1";
    private static final String PASSWORD = "123456";
    private static final long USER_1_ID = 1L;
    private static final UserEntity USER_1 = UserEntity.builder()
            .id(USER_1_ID)
            .username(USERNAME_1)
            .password(PASSWORD)
            .build();
    private static final String USERNAME_NOT_EXIST = "user_not_exists";
    private static final long USER_ID_NOT_EXISTS = 100L;
    private static final long ID_NEW = 4L;
    private static final String USERNAME_NEW = "new_user";

    private final JdbcUserRepository jdbcUserRepository;

    @Autowired
    public JdbcUserRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcUserRepository = new JdbcUserRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("Check if user exists should return true")
    void existByUsername() {
        boolean exists = jdbcUserRepository.existsByUsername(USERNAME_1);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Check if user exists should return false")
    void existByUsernameFalse() {
        boolean exists = jdbcUserRepository.existsByUsername(USERNAME_NOT_EXIST);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("getByUsername should return optional of user")
    void getByUserName() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getByUsername(USERNAME_1);

        assertThat(optionalUser).contains(USER_1);
    }

    @Test
    @DisplayName("getByUsername should return empty optional")
    void getByUserNameIsEmpty() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getByUsername(USERNAME_NOT_EXIST);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @DisplayName("getById should return optional of user")
    void getById() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getById(USER_1_ID);

        assertThat(optionalUser).contains(USER_1);
    }

    @Test
    @DisplayName("getById should return empty optional")
    void getByIdIsEmpty() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getById(USER_ID_NOT_EXISTS);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @DisplayName("save user")
    void save() {
        UserEntity expectedUser = UserEntity.builder()
                .id(ID_NEW)
                .username(USERNAME_NEW)
                .password("111111")
                .build();

        assertThat(jdbcUserRepository.existsByUsername(USERNAME_NEW)).isFalse();
        UserEntity newUser = jdbcUserRepository.save(UserEntity.builder()
                .username(USERNAME_NEW)
                .password("111111")
                .build());
        assertThat(newUser).isEqualTo(expectedUser);
        assertThat(jdbcUserRepository.existsByUsername(USERNAME_NEW)).isTrue();

    }
}
