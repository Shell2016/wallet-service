package io.ylab.wallet.in.aop;

import io.ylab.wallet.domain.dto.*;
import io.ylab.wallet.domain.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Aspect for application auditing.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    /**
     * Service that handles audit logic.
     */
    private final AuditService auditService;

    /**
     * Pointcut for UserService.createUser() method.
     */
    @Pointcut("within(io.ylab.wallet.domain.service.UserService) && execution(* *.createUser(..))")
    public void userRegister() {
    }

    /**
     * Pointcut for AccountService.getBalance() method.
     */
    @Pointcut("within(io.ylab.wallet.domain.service.AccountService) && execution(* *.getBalance(..))")
    public void accountService() {
    }

    /**
     * Pointcut for TransactionService.processTransaction() method.
     */
    @Pointcut("within(io.ylab.wallet.domain.service.TransactionService) && execution(* *.processTransaction(..))")
    public void processTransaction() {
    }

    /**
     * Pointcut for SecurityService.authenticate() method.
     */
    @Pointcut("within(io.ylab.wallet.domain.security.SecurityService) && execution(* *.authenticate(..))")
    public void authenticate() {
    }

    /**
     * Aspect for audit of user register process.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("userRegister()")
    public Object auditUserRegister(ProceedingJoinPoint joinPoint) throws Throwable {
        var userRequest = (UserRequest) joinPoint.getArgs()[0];
        String auditInfo = "Attempt to register new user with username: " + userRequest.username();
        log.info(auditInfo);
        auditService.audit(auditInfo);
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            auditInfo = "Register user error: " + e.getMessage();
            log.error(auditInfo);
            auditService.audit(auditInfo);
            throw e;
        }
        auditInfo = "New user created: " + result;
        log.info(auditInfo);
        auditService.audit(auditInfo);
        return result;
    }

    /**
     * Aspect for audit of balance requests.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("accountService()")
    public Object auditGetBalance(ProceedingJoinPoint joinPoint) throws Throwable {
        var userId = (long) joinPoint.getArgs()[0];
        String auditInfo = "Balance request for user with id: " + userId;
        log.info(auditInfo);
        auditService.audit(auditInfo);
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            auditInfo = "Balance request error: " + e.getMessage();
            log.error(auditInfo);
            auditService.audit(auditInfo);
            throw e;
        }
        BalanceResponse balanceResponse = (BalanceResponse) result;
        log.info("Balance received for user {}", userId);
        auditInfo = "User " + userId + " current balance: " + balanceResponse.balance();
        auditService.audit(auditInfo);
        return result;
    }

    /**
     * Aspect for transaction processing audit.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("processTransaction()")
    public Object auditProcessTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        TransactionRequest transactionRequest = (TransactionRequest) args[0];
        long userId = (long) args[1];
        String auditInfo =
                "Starting transaction processing for user " + userId + ", transaction: " + transactionRequest;
        log.info(auditInfo);
        auditService.audit(auditInfo);
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            auditInfo = "Error while processing transaction: " + e.getMessage();
            log.error(auditInfo);
            auditService.audit(auditInfo);
            throw e;
        }
        auditInfo = "Processed transaction: " + result;
        log.info(auditInfo);
        auditService.audit(auditInfo);
        return result;
    }

    /**
     * Aspect for successful authentication audit.
     *
     * @param joinPoint
     */
    @AfterReturning("authenticate()")
    public void auditAuthentication(JoinPoint joinPoint) {
        String username = (String) joinPoint.getArgs()[0];
        String auditInfo = "JWT token generated and sent to user " + username;
        log.info(auditInfo);
        auditService.audit(auditInfo);
    }
}
