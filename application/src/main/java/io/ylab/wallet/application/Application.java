package io.ylab.wallet.application;

import io.ylab.wallet.adapter.repository.InMemoryUserRepository;
import io.ylab.wallet.database.storage.InMemoryUserStorage;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.input.controller.WalletController;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import io.ylab.wallet.domain.service.ApplicationService;
import io.ylab.wallet.domain.service.UserService;
import io.ylab.wallet.in.adapter.controller.WalletConsoleController;

public class Application {
    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository(new InMemoryUserStorage());
        UserService userService = new UserService(userRepository, new UserMapper());
        WalletController controller = new WalletConsoleController();
        ApplicationService app = new ApplicationService(controller, userService);

        app.run();
    }
}
