package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public interface BeanFactoryPostProcessor {
    /**
     * 在所有BeanDefinition加载完成后，但在bean实例化前，提供修改BeanDefinition属性值的机制
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory (ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
