package io.ylab.wallet.adapter;

import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import io.ylab.wallet.mapper.UserDataAccessMapper;
import io.ylab.wallet.repository.JdbcUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adapter between UserRepository and JdbcUserRepository.
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    /**
     * Concrete repository that responsible for processing user data.
     */
    private final JdbcUserRepository jdbcUserRepository;
    /**
     * Maps between domain entities and data access entities.
     */
    private final UserDataAccessMapper userMapper;

    @Override
    public User save(User user){
        return userMapper.userEntityToUser(
                jdbcUserRepository.save(userMapper.userToUserEntity(user)));
    }

    @Override
    public boolean existsByUsername(String username) {
        return jdbcUserRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return jdbcUserRepository.getByUsername(username)
                .map(userMapper::userEntityToUser);
    }

    @Override
    public Optional<User> getById(long id) {
        return jdbcUserRepository.getById(id)
                .map(userMapper::userEntityToUser);
    }
}
