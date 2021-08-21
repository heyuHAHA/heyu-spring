import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.*;
import service.WorldService;
import service.WorldServiceBeforeAdviceInterceptor;
import service.WorldServiceImpl;
import service.WorldServiceInterceptor;

public class DynamicProxyTest {


    private AdvisedSupport advisedSupport;

    @Before
    public void setup() {
        WorldService worldService = new WorldServiceImpl();  //代理对象
        advisedSupport = new AdvisedSupport(); //封装了代理对象，AspectJExpressionPointcut(切点)，MethodInterceptor(增强过的方法)
        TargetSource targetSource = new TargetSource(worldService);
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* service.WorldService.explode(..))").getMethodMatcher();
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodMatcher(methodMatcher);
        advisedSupport.setMethodInterceptor(methodInterceptor);
    }

    @Test
    public void testJdkDynamicProxy() throws Exception {
        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testCglibProxy() {
        WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testProxyFactory() {

    }

    @Test
    public void testBeforeAdvice() {
        WorldServiceBeforeAdviceInterceptor wsBeforeAdvice = new WorldServiceBeforeAdviceInterceptor();
        MethodBeforeAdviceInterceptor methodBeforeAdviceInterceptor = new MethodBeforeAdviceInterceptor(wsBeforeAdvice);
        advisedSupport.setMethodInterceptor(methodBeforeAdviceInterceptor);

        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explode();

    }



}
