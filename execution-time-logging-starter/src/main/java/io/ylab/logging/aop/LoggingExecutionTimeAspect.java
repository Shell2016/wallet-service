package io.ylab.logging.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Aspect for measuring and logging to console method execution time.
 */
@Slf4j
@Aspect
public class LoggingExecutionTimeAspect {

    /**
     * Pointcut for controllers methods.
     */
    @Pointcut("within(io.ylab.wallet.*.controller.*) && execution(* *(..))")
    public void controllerMethod() {
    }

    /**
     * Aspect for logging execution time of method.
     *
     * @param joinPoint proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("controllerMethod()")
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
