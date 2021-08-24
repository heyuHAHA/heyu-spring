import bean.Car;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ValueAnnotationTest {

    @Test
    public void testValueAnnotation() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:value-annotation.xml");

        Car car = applicationContext.getBean("Car", Car.class);

        Assert.assertEquals("benz", car.getBrand());
    }
}
