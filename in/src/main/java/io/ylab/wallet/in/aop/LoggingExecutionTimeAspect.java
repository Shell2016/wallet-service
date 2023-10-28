package io.ylab.wallet.in.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Aspect for measuring and logging to console method execution time.
 */
@Slf4j
@Aspect
@Component
public class LoggingExecutionTimeAspect {

    /**
     * Pointcut for AuthenticationController authenticate() method.
     */
    @Pointcut("within(io.ylab.wallet.in.controller.AuthenticationController) && execution(* *.authenticate(..))")
    public void authenticate() {
    }

    /**
     * Aspect for logging execution time of AuthenticationController.authenticate() method.
     *
     * @param joinPoint proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("authenticate()")
    public Object authenticateExecutionTimeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Calling method {}", joinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution of method {} finished. Execution time is {}",
                joinPoint.getSignature(),
                endTime - startTime);
        return result;
    }
}
