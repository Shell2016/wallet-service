package io.ylab.wallet.application;

import io.ylab.wallet.adapter.*;
import io.ylab.wallet.domain.mapper.UserMapper;
import io.ylab.wallet.domain.port.input.controller.WalletController;
import io.ylab.wallet.domain.port.output.repository.*;
import io.ylab.wallet.domain.service.*;
import io.ylab.wallet.in.adapter.controller.WalletConsoleController;
import io.ylab.wallet.liquibase.MigrationUtils;
import io.ylab.wallet.mapper.*;
import io.ylab.wallet.repository.*;

/**
 * Main class that starts application.
 */
public class Application {
    public static void main(String[] args) {
        runMigrations();
        initApplication().run();
    }

    /**
     * runs liquibase migrations
     */
    private static void runMigrations() {
        MigrationUtils.runMigrations();
    }

    /**
     * Creates all needed instances for running application.
     * @return ApplicationService instance
     */
    private static ApplicationService initApplication() {
        JdbcAccountRepository jdbcAccountRepository = new JdbcAccountRepository();
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(jdbcAccountRepository);
        JdbcAuditRepository jdbcAuditRepository = new JdbcAuditRepository();
        JdbcTransactionRepository jdbcTransactionRepository = new JdbcTransactionRepository();
        UserRepository userRepository = new UserRepositoryImpl(jdbcUserRepository, new UserDataAccessMapper());
        AccountRepository accountRepository =
                new AccountRepositoryImpl(jdbcAccountRepository, new AccountDataAccessMapper());
        TransactionRepository transactionRepository =
                new TransactionRepositoryImpl(jdbcTransactionRepository, new TransactionDataAccessMapper());
        AuditRepository auditRepository = new AuditRepositoryImpl(jdbcAuditRepository, new AuditDataAccessMapper());

        UserService userService = new UserService(userRepository, accountRepository, new UserMapper());
        AccountService accountService = new AccountService(accountRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, userService, accountService);
        AuditService auditService = new AuditService(auditRepository);
        WalletController controller = new WalletConsoleController();

        return new ApplicationService(
                controller,
                userService,
                accountService,
                transactionService,
                auditService);
    }
}
