package org.springframework.beans.factory;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
