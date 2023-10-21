package io.ylab.wallet.in.util;

import io.ylab.wallet.adapter.*;
import io.ylab.wallet.domain.port.output.repository.*;
import io.ylab.wallet.domain.security.SecurityService;
import io.ylab.wallet.domain.service.*;
import io.ylab.wallet.mapper.*;
import io.ylab.wallet.repository.*;
import lombok.experimental.UtilityClass;

/**
 * Temporary dependency container for convenient dependency injection (until we get spring IoC Container)
 */
@UtilityClass
public class DependencyContainer {

    private final JdbcAccountRepository JDBC_ACCOUNT_REPOSITORY = new JdbcAccountRepository();
    private final JdbcTransactionRepository JDBC_TRANSACTION_REPOSITORY = new JdbcTransactionRepository();
    private final JdbcAuditRepository JDBC_AUDIT_REPOSITORY = new JdbcAuditRepository();
    private final JdbcUserRepository JDBC_USER_REPOSITORY = new JdbcUserRepository(JDBC_ACCOUNT_REPOSITORY);

    private final AccountDataAccessMapper ACCOUNT_DATA_ACCESS_MAPPER = new AccountDataAccessMapper();
    private final TransactionDataAccessMapper TRANSACTION_DATA_ACCESS_MAPPER = new TransactionDataAccessMapper();
    private final AuditDataAccessMapper AUDIT_DATA_ACCESS_MAPPER = new AuditDataAccessMapper();
    private final UserDataAccessMapper USER_DATA_ACCESS_MAPPER = new UserDataAccessMapper();

    private final AccountRepository ACCOUNT_REPOSITORY =
            new AccountRepositoryImpl(JDBC_ACCOUNT_REPOSITORY, ACCOUNT_DATA_ACCESS_MAPPER);
    private final TransactionRepository TRANSACTION_REPOSITORY =
            new TransactionRepositoryImpl(JDBC_TRANSACTION_REPOSITORY, TRANSACTION_DATA_ACCESS_MAPPER);
    private final AuditRepository AUDIT_REPOSITORY =
            new AuditRepositoryImpl(JDBC_AUDIT_REPOSITORY, AUDIT_DATA_ACCESS_MAPPER);
    private final UserRepository USER_REPOSITORY =
            new UserRepositoryImpl(JDBC_USER_REPOSITORY, USER_DATA_ACCESS_MAPPER);

    private final AccountService ACCOUNT_SERVICE = new AccountService(ACCOUNT_REPOSITORY);
    private final UserService USER_SERVICE = new UserService(USER_REPOSITORY, ACCOUNT_REPOSITORY);
    private final TransactionService TRANSACTION_SERVICE =
            new TransactionService(TRANSACTION_REPOSITORY, USER_SERVICE, ACCOUNT_SERVICE);
    private final AuditService AUDIT_SERVICE = new AuditService(AUDIT_REPOSITORY);
    private final SecurityService SECURITY_SERVICE = new SecurityService(USER_REPOSITORY);
    private final JsonHelper JSON_HELPER = new JsonHelper();

    public AccountService getAccountService() {
        return ACCOUNT_SERVICE;
    }

    public UserService getUserService() {
        return USER_SERVICE;
    }

    public TransactionService getTransactionService() {
        return TRANSACTION_SERVICE;
    }

    public SecurityService getSecurityService() {
        return SECURITY_SERVICE;
    }

    public AuditService getAuditService() {
        return AUDIT_SERVICE;
    }

    public JsonHelper getJsonHelper() {
        return JSON_HELPER;
    }
}
