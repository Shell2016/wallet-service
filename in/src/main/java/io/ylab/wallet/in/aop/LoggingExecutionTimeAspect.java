package io.ylab.wallet.in.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Aspect for measuring and logging to console method execution time.
 */
@Aspect
public class LoggingExecutionTimeAspect {

    /**
     * Pointcut for AuthenticationFilter.doFilter() method
     */
    @Pointcut("within(io.ylab.wallet.in.filter.AuthenticationFilter) && execution(* *.doFilter(..))")
    public void doFilter() {
    }

    /**
     * Aspect for logging execution time of AuthenticationFilter.doFilter() method.
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("doFilter()")
    public Object doFilterExecutionTimeLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                           " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}
