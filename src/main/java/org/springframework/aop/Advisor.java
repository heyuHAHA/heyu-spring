package org.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Advisor 是pointcut和Advice的结合
 */
public interface Advisor {
    Advice getAdvice();
}
