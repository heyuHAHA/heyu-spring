package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private BeanDefinitionRegistry beanDefinitionRegistry;
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());

    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, DefaultResourceLoader defaultResourceLoader) {
        this.beanDefinitionRegistry = registry;
        this.resourceLoader = defaultResourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return beanDefinitionRegistry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }


    @Override
    public void loadBeanDefinitions(String[] locations) throws BeansException {
        for(String location :locations) {
            loadBeanDefinitions(location);
        }
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
