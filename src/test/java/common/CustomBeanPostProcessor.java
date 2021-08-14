package common;

import bean.Car;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("car".equals(beanName)) {
            ((Car)bean).setBrand("benz");
        }
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) throws BeansException {
       System.out.println("CustomerBeanPostProcessor#postProcessorAfterInitialization");
       return bean;
    }
}
