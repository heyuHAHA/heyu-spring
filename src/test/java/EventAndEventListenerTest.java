import event.CustomEvent;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EventAndEventListenerTest {

    @Test
    public void testEventListener() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event-and-event-listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));

        applicationContext.registerShutdownHook();
    }
}
