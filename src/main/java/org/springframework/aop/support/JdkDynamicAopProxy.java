package org.springframework.aop.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.AopProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果切点match 目标类的方法
        if(advisedSupport.getMethodMatcher().matches(method,advisedSupport.getTargetSource().getTarget().getClass())) {
            //代理方法类
            MethodInterceptor methodInterceptor = advisedSupport.getMethodInterceptor();
            //调用代理方法类的方法
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advisedSupport.getTargetSource().getTarget(),method,args));
        }
        return method.invoke(advisedSupport.getTargetSource().getTarget(),args);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),advisedSupport.getTargetSource().getTargetClass(),this);
    }
}
