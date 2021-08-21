package org.springframework.aop.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;

public class MethodBeforeAdviceInterceptor  implements MethodInterceptor {

    private MethodBeforeAdvice methodBeforeAdvice;

    public MethodBeforeAdviceInterceptor() {

    }

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice methodBeforeAdvice) {
        this.methodBeforeAdvice = methodBeforeAdvice;
    }
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
       //在执行代理方法前，先执行before advice操作
        this.methodBeforeAdvice.before(methodInvocation.getMethod(),methodInvocation.getArguments(),methodInvocation.getThis());
        return methodInvocation.proceed();
    }

    public void setAdvice(MethodBeforeAdvice advice) {
        this.methodBeforeAdvice = advice;
    }
}
