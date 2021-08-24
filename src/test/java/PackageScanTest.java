import bean.Car;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PackageScanTest {
    @Test
    public void test() throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:package-scan.xml");
        Car car = context.getBean("Car", Car.class);
        Assert.assertNotNull(car);

    }
}
