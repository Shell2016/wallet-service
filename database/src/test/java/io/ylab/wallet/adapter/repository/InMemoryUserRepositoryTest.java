package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.InMemoryUserStorage;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserRepositoryTest {

    private static final String TESTNAME = "testname";
    private static final String PASSWORD = "password";
    private static final String UUID_USER_STRING = "cc6227ae-6a83-4888-9538-df7062c572fe";
    private static final User USER = new User(UUID.fromString(UUID_USER_STRING), TESTNAME, PASSWORD);
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository = new InMemoryUserRepository(new InMemoryUserStorage());
    }

    @Test
    void checkExistsIfEmpty() {
        assertThat(userRepository.existsByUsername(TESTNAME)).isFalse();
    }

    @Test
    void checkSave() {
        userRepository.save(USER);
        Optional<User> result = userRepository.getById(UUID_USER_STRING);

        assertThat(result.get()).isEqualTo(USER);
    }

    @Test
    void checkExists() {
        userRepository.save(USER);

        assertThat(userRepository.existsByUsername(TESTNAME)).isTrue();
    }

    @Test
    void getByUsernameIfUserDoesNotExist() {
        Optional<User> result = userRepository.getByUsername(TESTNAME);

        assertThat(result).isEmpty();
    }

    @Test
    void getByUsernameIfUserExists() {
        userRepository.save(USER);
        Optional<User> result = userRepository.getByUsername(TESTNAME);

        assertThat(result).isNotEmpty().contains(USER);
    }

    @Test
    void getByIdIfNotExists() {
        Optional<User> result = userRepository.getById(UUID_USER_STRING);

        assertThat(result).isEmpty();
    }

    @Test
    void getByIdIfExists() {
        userRepository.save(USER);
        Optional<User> result = userRepository.getById(UUID_USER_STRING);

        assertThat(result).isNotEmpty().contains(USER);
    }
}
