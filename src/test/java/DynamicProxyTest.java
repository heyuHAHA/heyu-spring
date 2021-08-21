import org.junit.Test;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AdvisedSupport;
import org.springframework.aop.support.CglibAopProxy;
import org.springframework.aop.support.JdkDynamicAopProxy;
import org.springframework.aop.support.TargetSource;
import service.WorldService;
import service.WorldServiceImpl;
import service.WorldServiceInterceptor;

public class DynamicProxyTest {

    @Test
    public void testJdkDynamicProxy() throws Exception {
        WorldService worldService = new WorldServiceImpl();  //代理对象
        AdvisedSupport advisedSupport = new AdvisedSupport(); //封装了代理对象，AspectJExpressionPointcut(切点)，MethodInterceptor(增强过的方法)
        TargetSource targetSource = new TargetSource(worldService);
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* service.WorldService.explode(..))").getMethodMatcher();
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodMatcher(methodMatcher);
        advisedSupport.setMethodInterceptor(methodInterceptor);

        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testCglibProxy() {
        WorldService worldService = new WorldServiceImpl();  //代理对象
        AdvisedSupport advisedSupport = new AdvisedSupport(); //封装了代理对象，AspectJExpressionPointcut(切点)，MethodInterceptor(增强过的方法)
        TargetSource targetSource = new TargetSource(worldService);
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* service.WorldService.explode(..))").getMethodMatcher();
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodMatcher(methodMatcher);
        advisedSupport.setMethodInterceptor(methodInterceptor);

        WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }



}
