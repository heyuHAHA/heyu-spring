package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例Bean
     */
    void destroySingletons() throws BeansException;
}
