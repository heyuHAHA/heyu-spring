package service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.support.AdvisedSupport;

import java.lang.reflect.Method;

public class WorldServiceBeforeAdviceInterceptor implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("BeforeAdvice: do something before the earth explodes");
    }
}
