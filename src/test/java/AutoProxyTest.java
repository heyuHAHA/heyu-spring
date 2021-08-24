import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.WorldService;

public class AutoProxyTest {
    @Test
    public void testAutoProxy() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:auto-proxy.xml");

        //获取代理对象
        WorldService worldService = (WorldService) applicationContext.getBean("worldService");
        Assert.assertNotNull(worldService);
        worldService.explode();
    }
}
