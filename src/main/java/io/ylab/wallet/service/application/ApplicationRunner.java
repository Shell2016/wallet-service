package io.ylab.wallet.service.application;

import io.ylab.wallet.service.application.controller.WalletController;
import io.ylab.wallet.service.application.mapper.UserMapper;
import io.ylab.wallet.service.application.repository.UserRepository;
import io.ylab.wallet.service.application.service.ApplicationService;
import io.ylab.wallet.service.application.service.UserService;
import io.ylab.wallet.service.database.repository.InMemoryUserRepository;
import io.ylab.wallet.service.database.storage.InMemoryUserStorage;
import io.ylab.wallet.service.in.WalletConsoleController;

public class ApplicationRunner {
    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository(new InMemoryUserStorage());
        UserService userService = new UserService(userRepository, new UserMapper());
        WalletController controller = new WalletConsoleController();
        ApplicationService app = new ApplicationService(controller, userService);
        app.run();
    }
}
