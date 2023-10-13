package io.ylab.wallet.application;

import io.ylab.wallet.adapter.repository.*;
import io.ylab.wallet.database.storage.InMemoryAuditStorage;
import io.ylab.wallet.database.storage.InMemoryTransactionStorage;
import io.ylab.wallet.database.storage.InMemoryUserStorage;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.input.controller.WalletController;
import io.ylab.wallet.domain.port.output.repository.*;
import io.ylab.wallet.domain.service.*;
import io.ylab.wallet.in.adapter.controller.WalletConsoleController;

/**
 * Main class that starts application.
 */
public class Application {
    public static void main(String[] args) {
        ApplicationService app = initApplication();
        app.run();
    }

    /**
     * Creates all needed instances for running application.
     * @return ApplicationService instance
     */
    private static ApplicationService initApplication() {
        UserRepository userRepository = new InMemoryUserRepository(new InMemoryUserStorage());
        TransactionRepository transactionRepository =
                new InMemoryTransactionRepository(new InMemoryTransactionStorage());
        AuditRepository auditRepository = new InMemoryAuditRepository(new InMemoryAuditStorage());
        UserService userService = new UserService(userRepository, new UserMapper());
        TransactionService transactionService = new TransactionService(transactionRepository, userService);
        AuditService auditService = new AuditService(auditRepository);
        WalletController controller = new WalletConsoleController();

        return new ApplicationService(
                controller,
                userService,
                transactionService,
                auditService);
    }
}
