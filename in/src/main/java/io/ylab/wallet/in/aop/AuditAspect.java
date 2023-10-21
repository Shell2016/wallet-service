package io.ylab.wallet.in.aop;

import io.ylab.wallet.domain.dto.TransactionDto;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.security.TokenDetails;
import io.ylab.wallet.domain.service.AuditService;
import io.ylab.wallet.in.util.DependencyContainer;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Aspect for application auditing.
 */
@Aspect
public class AuditAspect {

    /**
     * Service that handles audit logic.
     */
    private final AuditService auditService = DependencyContainer.getAuditService();

    /**
     * Pointcut for authenticationFilter doFilter method.
     */
    @Pointcut("within(io.ylab.wallet.in.filter.AuthenticationFilter) && execution(* *.doFilter(..))")
    public void authenticationFilter() {
    }

    /**
     * Pointcut for method that return error response.
     */
    @Pointcut("within(io.ylab.wallet.in.util.JsonHelper) && execution(* *.buildErrorResponse(..))")
    public void jsonHelperBuildError() {
    }

    /**
     * Pointcut for method that returns json response.
     */
    @Pointcut("within(io.ylab.wallet.in.util.JsonHelper) && execution(* *.buildJsonFromObject(..))")
    public void jsonHelperBuildJson() {
    }

    /**
     * Advice for auditing all requests from users (http method and path).
     *
     * @param joinPoint
     */
    @Before("authenticationFilter()")
    public void auditInvokedUriAndMethod(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) joinPoint.getArgs()[0];
        if (req.getMethod() == null) {
            return;
        }
        String auditInfo = "Invoked API request: " + req.getMethod() + " " + req.getRequestURI();
        auditService.audit(auditInfo);
    }

    /**
     * Advice for auditing errors.
     *
     * @param result for accessing method returned object
     */
    @AfterReturning(value = "jsonHelperBuildError()", returning = "result")
    public void auditErrors(Object result) {
        String auditInfo = "Sent error response: " + result.toString();
        auditService.audit(auditInfo);
    }

    /**
     * Advice for auditing successful user operations.
     *
     * @param joinPoint for accessing method arguments
     */
    @AfterReturning("jsonHelperBuildJson()")
    public void auditResponse(JoinPoint joinPoint) {
        Object object = joinPoint.getArgs()[0];
        Class<?> clazz = object.getClass();
        if (clazz == TokenDetails.class) {
            TokenDetails tokenDetails = (TokenDetails) object;
            String auditInfo = "Successful authentication for user with id: " + tokenDetails.getUserId();
            auditService.audit(auditInfo);
        } else if (clazz == UserResponse.class) {
            UserResponse userResponse = (UserResponse) object;
            String auditInfo = "Successful registration for user with id: " + userResponse.id() + " and username: " +
                               userResponse.username();
            auditService.audit(auditInfo);
        } else if (clazz == TransactionDto.class) {
            TransactionDto transactionDto = (TransactionDto) object;
            String auditInfo = "Successful transaction " + transactionDto.id() +
                               " for user with id " + transactionDto.userId();
            auditService.audit(auditInfo);
        }
    }
}
