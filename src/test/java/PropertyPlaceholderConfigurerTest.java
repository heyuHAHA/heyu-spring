import bean.Car;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PropertyPlaceholderConfigurerTest {
    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:property-placeholder-configurer.xml");

        Car car = applicationContext.getBean("car",Car.class);
        Assert.assertEquals("benz",car.getBrand());
    }
}
