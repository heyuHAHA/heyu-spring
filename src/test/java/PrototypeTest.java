import bean.Car;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PrototypeTest {
    @Test
    public void prototypeTest() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:prototype-bean.xml");

        Car car1 =  applicationContext.getBean("car",Car.class);
        Car car2 =  applicationContext.getBean("car",Car.class);
        System.out.println(car1 == car2);
    }
}
