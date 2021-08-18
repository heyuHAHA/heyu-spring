package aop;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import service.HelloService;

import java.lang.reflect.Method;

public class PointcutExpressionTest {
    @Test
    public void testPointcutExpression() throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* service.HelloService.*(..))");
        Class<HelloService> clazz = HelloService.class;
        Method method = clazz.getDeclaredMethod("sayHello");

        System.out.println(pointcut.matches(method,clazz));
        System.out.println(pointcut.matches(clazz));
    }
}
