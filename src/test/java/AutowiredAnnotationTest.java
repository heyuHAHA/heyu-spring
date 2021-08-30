import bean.Car;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.bean.Person;

public class AutowiredAnnotationTest {
    @Test
    public void test() throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:autowired-annotation.xml");
        Person person = context.getBean("Person", Person.class);
        Car car = person.getCar();
        Assert.assertNotNull(person);
        System.out.println(person);
        Assert.assertNotNull(car);
        Assert.assertEquals("benz",car.getBrand());
    }

}
