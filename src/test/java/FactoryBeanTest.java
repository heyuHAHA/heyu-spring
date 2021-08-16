import bean.Car;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class FactoryBeanTest {
    @Test
    public void testFactoryBean() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        Car car = (Car) applicationContext.getBean("car");
        System.out.println(car);
    }
}
