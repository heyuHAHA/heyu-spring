import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InitAndDestroyMethodTest {
    @Test
    public void testInitAndDestroyMethod() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy.xml");
        applicationContext.getBean("person");
        applicationContext.registerShutdownHook();
    }
}
