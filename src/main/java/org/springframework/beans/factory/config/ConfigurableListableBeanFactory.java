package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ConfigurableBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;


public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, ConfigurableBeanFactory, ListableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);


}
