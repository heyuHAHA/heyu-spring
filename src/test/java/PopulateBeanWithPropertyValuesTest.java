import bean.Car;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.ioc.bean.Person;

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void applyPropertyToBean() throws BeansException {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.add(new PropertyValue("name","heyu"));
        propertyValues.add(new PropertyValue("age",18));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class,propertyValues);
        beanFactory.registerBeanDefinition("person",beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
    }

    @Test
    public void testPopulateBeanWithBean() throws BeansException {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //
        PropertyValues carP = new PropertyValues();
        carP.add(new PropertyValue("brand","benz"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class,carP);
        beanFactory.registerBeanDefinition("car",carBeanDefinition);

        //
        PropertyValues perP = new PropertyValues();
        perP.add(new PropertyValue("name","heyu"));
        perP.add(new PropertyValue("age",18));
        perP.add(new PropertyValue("car",new BeanReference("car")));
        BeanDefinition personBeanDefinition = new BeanDefinition(Person.class,perP);
        beanFactory.registerBeanDefinition("person",personBeanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        Car car = person.getCar();
        System.out.println(car);
    }
}
